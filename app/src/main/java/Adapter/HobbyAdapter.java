package Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.asus.newdailyscore.HistoryScore;
import com.example.asus.newdailyscore.R;

import java.util.List;

import Date.Hobby;

/**
 * Created by asus on 2017/8/14.
 */

public class HobbyAdapter extends RecyclerView.Adapter<HobbyAdapter.ViewHolder> {

    private List<Hobby> mHobbyList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView totalScore;
        ImageButton finish;
        TextView name;

        public ViewHolder(View view){
            super(view);
            totalScore = (TextView) view.findViewById(R.id.totalscore_item);
            finish = (ImageButton) view.findViewById(R.id.image_item);
            name = (TextView) view.findViewById(R.id.hobby_name_item);
        }
    }

    public HobbyAdapter(List<Hobby>list){
        mHobbyList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hobby_item,parent,false);
        final ViewHolder viewholder = new ViewHolder(view);
        viewholder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = viewholder.getAdapterPosition();
                final Hobby hobby = mHobbyList.get(position);
                Intent intent = new Intent(v.getContext(), HistoryScore.class);
                intent.putExtra("name",hobby.getName());
                v.getContext().startActivity(intent);
            }
        });
        viewholder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = viewholder.getAdapterPosition();
                Hobby hobby = mHobbyList.get(position);
               removeHobby(position,v.getContext());
                return false;
            }
        });
        viewholder.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewholder.getAdapterPosition();
                Hobby hobby = mHobbyList.get(position);
                hobby.setIsFinish();
                if(hobby.isSaved())hobby.save();
                if(hobby.getIsFinish()){
                    if(hobby.getPerScore() > 0){
                        viewholder.finish.setImageResource(R.drawable.ic_wb_sunny_amber_700_24dp);
                    }
                    else
                        viewholder.finish.setImageResource(R.drawable.ic_cloud_black_24dp);
                }else {
                    if(hobby.getPerScore() > 0){
                        viewholder.finish.setImageResource(R.drawable.ic_wb_sunny_grey_500_24dp);
                    }else
                        viewholder.finish.setImageResource(R.drawable.ic_cloud_grey_500_24dp);
                }
                viewholder.totalScore.setText(Integer.valueOf(hobby.getTotalScore()).toString());
            }
        });
        return viewholder;
    }

    protected void removeHobby(final int position, Context context){
        final Hobby hobby = mHobbyList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("删除习惯");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hobby.delete();
                mHobbyList.remove(position);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Hobby hobby = mHobbyList.get(position);
        boolean test = hobby.isSaved();
        holder.name.setText(hobby.getName());
        if(hobby.getIsFinish()){
            if(hobby.getPerScore() > 0){
                holder.finish.setImageResource(R.drawable.ic_wb_sunny_amber_700_24dp);
            }
            else
                holder.finish.setImageResource(R.drawable.ic_cloud_black_24dp);
        }else {
            if(hobby.getPerScore() > 0){
                holder.finish.setImageResource(R.drawable.ic_wb_sunny_grey_500_24dp);
            }else
                holder.finish.setImageResource(R.drawable.ic_cloud_grey_500_24dp);
        }
        holder.totalScore.setText(Integer.valueOf(hobby.getTotalScore()).toString());
    }

    @Override
    public int getItemCount() {
        return mHobbyList.size();
    }
}
