package com.example.asus.newdailyscore;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import Date.Hobby;
import layout.HobbyFragment;
import layout.ForestFragment;
import layout.ScoreFragment;

public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener {
    private AHBottomNavigation mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        intiView();
    }

    private void intiView() {
            mNavigation = (AHBottomNavigation)  findViewById(R.id.bottom_navigation);
            AHBottomNavigationItem hobbyItem = new AHBottomNavigationItem("习惯",
                    R.drawable.ic_directions_run_blue_700_24dp,R.color.blue_700);
            AHBottomNavigationItem scoreItem = new AHBottomNavigationItem("分数",
                    R.drawable.ic_school_blue_700_24dp,R.color.amber_700);
            AHBottomNavigationItem forestItem = new AHBottomNavigationItem("森林",
                    R.drawable.ic_local_florist_blue_700_24dp,R.color.green_700);
            mNavigation.addItem(hobbyItem);
            mNavigation.addItem(scoreItem);
            mNavigation.addItem(forestItem);
            mNavigation.setOnTabSelectedListener(this);
            mNavigation.setCurrentItem(0);
    }

    private void replace(Fragment fragment) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction mTransaction = manager.beginTransaction();
            mTransaction.replace(R.id.fragment,fragment);
            mTransaction.commit();
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        switch (position){
            case 0:
                mNavigation.setAccentColor(getResources().getColor(R.color.blue_700));
                replace(new HobbyFragment());
                break;
            case 1:
                mNavigation.setAccentColor(getResources().getColor(R.color.amber_700));
                replace(new ScoreFragment());
                break;
            case 2:
                mNavigation.setAccentColor(getResources().getColor(R.color.green_700));
                replace(new ForestFragment());
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                addHobbyDialog();
                break;
            default:
                break;
        }
        return true;
    }

    private void addHobbyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("新增习惯");
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_hobby_layout,null);
        builder.setView(view);
        final EditText hobbyName=(EditText)view.findViewById(R.id.new_edit_hobby_name);
        final EditText hobbyScore=(EditText)view.findViewById(R.id.new_set_hobby_score);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(hobbyName.getText().length() == 0){
                    Toast.makeText(MainActivity.this,"习惯名称不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(hobbyScore.getText().length() == 0){
                    Toast.makeText(MainActivity.this,"习惯分数不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    Hobby newHobby = new Hobby(
                            hobbyName.getText().toString(),Integer.parseInt(hobbyScore.getText().toString()));
                    Toast.makeText(MainActivity.this,"成功创建习惯",Toast.LENGTH_LONG).show();
                    newHobby.save();
                    Intent intent = new
                            Intent("com.example.asus.dailyscore.HOBBYADD");
                    sendBroadcast(intent);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
}
