package layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.asus.newdailyscore.MainActivity;
import com.example.asus.newdailyscore.R;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.ParseException;
import java.util.List;

import Adapter.HobbyAdapter;
import Date.Hobby;


public class HobbyFragment extends Fragment {
    List<Hobby> hobbyList;
    HobbyAdapter mHobbyAdapter;
    RecyclerView hobbyView;
    HobbyAddReceiver receiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_hobby,container,false);
            hobbyView = (RecyclerView) view.findViewById(R.id.hobby_list);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            hobbyView.setLayoutManager(manager);
            getRecycleView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.asus.dailyscore.HOBBYADD");
        receiver = new HobbyAddReceiver();
        getActivity().registerReceiver(receiver,intentFilter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);
    }

    public void getRecycleView(){
        Connector.getDatabase();
        hobbyList = DataSupport.findAll(Hobby.class);
            mHobbyAdapter = new HobbyAdapter(hobbyList);
            hobbyView.setAdapter(mHobbyAdapter);
        }


    public void refreshList(){
        hobbyList.add(DataSupport.findLast(Hobby.class));
        mHobbyAdapter.notifyDataSetChanged();
    }

    protected void removeHobby(final int position){
        final Hobby hobby = hobbyList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("删除习惯");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hobbyList.remove(position);
                mHobbyAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public class HobbyAddReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            hobbyList.add(DataSupport.findLast(Hobby.class));
           mHobbyAdapter.notifyDataSetChanged();
        }
    }

}
