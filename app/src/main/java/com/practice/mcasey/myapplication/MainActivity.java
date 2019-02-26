package com.practice.mcasey.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.mcasey.myapplication.AlarmList.AlarmListFragment;
import com.practice.mcasey.myapplication.CountdownTimer.CountdownFragment;
import com.practice.mcasey.myapplication.Services.NotificationService;
import com.practice.mcasey.myapplication.StopWatch.StopWatchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static Intent sNotificationService;

    public static FragmentManager fm;

    @BindView(R.id.alarm_tab) public TextView mAlarmsTab;
    @BindView(R.id.countdown_tab) public TextView mCountDownTab;
    @BindView(R.id.stopwatch_tab) public TextView mStopWatchTab;

    Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        mFragment = fm.findFragmentById(R.id.fragment_container);
        if(mFragment == null){
            mFragment = new AlarmListFragment();
            //getFragmentTransaction(mFragment).commit();
            fm.beginTransaction().add(R.id.fragment_container, mFragment).commit();
        }
        ButterKnife.bind(this);
        mAlarmsTab.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mAlarmsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment = new AlarmListFragment();
                mAlarmsTab.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mCountDownTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                mStopWatchTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                getFragmentTransaction(mFragment).commit();
            }
        });

        mCountDownTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment = new CountdownFragment();
                mAlarmsTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                mCountDownTab.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mStopWatchTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                getFragmentTransaction(mFragment).commit();
            }
        });

        mStopWatchTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment = new StopWatchFragment();
                mAlarmsTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                mCountDownTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                mStopWatchTab.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                getFragmentTransaction(mFragment).commit();
            }
        });
    }

    private FragmentTransaction getFragmentTransaction(Fragment fragment){
        return fm.beginTransaction().replace(R.id.fragment_container, fragment);
    }
}