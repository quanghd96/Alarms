package com.practice.mcasey.myapplication.TriggerAlarm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.practice.mcasey.myapplication.AlarmList.AlarmListFragment;
import com.practice.mcasey.myapplication.MainActivity;
import com.practice.mcasey.myapplication.Model.Alarm;
import com.practice.mcasey.myapplication.R;
import com.practice.mcasey.myapplication.Services.AlarmService;

import org.parceler.Parcels;

import java.time.Clock;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TriggerAlarmFragment extends Fragment implements TriggerAlarmView {

    TriggerAlarmPresenter mPresenter;
    View mView;
    Bundle mBundle;

    //@BindView(R.id.alarm_trigger_current_time) Clock mCurrentTime;
    @BindView(R.id.alarm_trigger_description) TextView mTriggerDescription;
    @BindView(R.id.alarm_trigger_off_btn) Button mOffBtn;

    @Override
    public void onResume() {
        super.onResume();
        if(getArguments() != null){
            mBundle = getArguments();
            String description = mBundle.getString(AlarmService.ALARM_TRIGGERED);
            mTriggerDescription.setText(description);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_trigger_alarm, container, false);
        ButterKnife.bind(this, mView);
        mPresenter = new TriggerAlarmPresenter(this);
        return mView;
    }

    @OnClick (R.id.alarm_trigger_off_btn)
    public void turnOffAlarm(){
        mPresenter.turnOffAlarm();
    }

    @Override
    public void updateTimeText() {

    }

    @Override
    public void onClickTurnOffAlarm() {
        getActivity().stopService(MainActivity.sNotificationService);
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(0, R.anim.slide_out_down_long)
                .replace(R.id.fragment_container, new AlarmListFragment()).commit();
    }

    public void updateCurrentTime(){
        mPresenter.updateTime();
    }
}
