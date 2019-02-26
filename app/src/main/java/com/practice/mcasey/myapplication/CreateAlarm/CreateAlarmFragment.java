package com.practice.mcasey.myapplication.CreateAlarm;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.practice.mcasey.myapplication.AlarmList.AlarmListFragment;
import com.practice.mcasey.myapplication.Model.Alarm;
import com.practice.mcasey.myapplication.Model.AlarmSingleton;
import com.practice.mcasey.myapplication.R;

import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class CreateAlarmFragment extends Fragment implements CreateAlarmView{

    private static final int ZERO = 00;
    public static final String CREATE_ALARM = "CREATE_ALARM";
    public static final int TONE_PICKER = 1;

    LayoutInflater mLayoutInflater;
    ViewGroup mViewGroup;

    CreateAlarmPresenter mPresenter;
    Alarm mAlarm;

    Bundle mBundle;
    PopupWindow mTimePickerPopupWindow;
    PopupWindow mDayPickerPopupWindow;
    View mView;
    View mTimePickerView;
    View mDayPickerView;

    @BindView(R.id.create_alarm_time_tv) TextView mTime;
    @BindView(R.id.create_alarm_description_et) EditText mDescription;
    @BindView(R.id.create_alarm_days_tv) TextView mDays;
    @BindView(R.id.create_alarm_btn) Button mCreateBtn;

    TimePicker mTimePicker;
    RecyclerView mDaysRecyclerView;
    DayRecyclerAdapter mDayAdapter;

    Button mTimeCancelBtn;
    Button mTimeOkBtn;
    Button mDaysCancelBtn;
    Button mDaysOkBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mViewGroup = container;
        mView = mLayoutInflater.inflate(R.layout.fragment_create_alarm, mViewGroup, false);
        ButterKnife.bind(this, mView);
        mPresenter = new CreateAlarmPresenter(this, getContext());
        mPresenter.currentTime();
        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_create, menu);
        MenuItem cancelItem = menu.findItem(R.id.menu_new_alarm);
        cancelItem.setTitle(R.string.cancel_btn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_new_alarm: getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations( R.anim.slide_in_up, R.anim.slide_out_down)
                    .replace(R.id.fragment_container, new AlarmListFragment()).commit();
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAlarm = new Alarm();
        mPresenter.addDays();
        if(getArguments() != null){
            mBundle = getArguments();
            mAlarm = Parcels.unwrap(mBundle.getParcelable(CreateAlarmFragment.CREATE_ALARM));
            mTime.setText(mAlarm.getTime());
            mDescription.setText(mAlarm.getAlarmDescription());
            mDays.setText(mAlarm.getDays());
            mCreateBtn.setText("Update");
        }
    }

    @OnClick(R.id.create_alarm_time_tv)
    void onTimeClick(){
        mTimePickerView = mLayoutInflater.inflate(R.layout.popup_time_picker, mViewGroup, false);
        mTimePickerPopupWindow = new PopupWindow(mTimePickerView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mTimePicker = mTimePickerView.findViewById(R.id.time_picker_popup);
        mTimePickerPopupWindow.showAtLocation(mView, Gravity.CENTER, ZERO, ZERO);
        mTimeCancelBtn = mTimePickerView.findViewById(R.id.time_picker_cancel_btn);
        mTimeCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimePickerPopupWindow.dismiss();
            }
        });
        mTimeOkBtn = mTimePickerView.findViewById(R.id.time_picker_ok_btn);
        mTimeOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.updateTime(mAlarm, mTimePicker.getHour(), mTimePicker.getMinute());
                mTimePickerPopupWindow.dismiss();
            }
        });
    }

    @OnTextChanged(R.id.create_alarm_description_et)
    void onDescriptionChange(CharSequence charSequence){
        mAlarm.setAlarmDescription(charSequence.toString());
    }

    @OnClick(R.id.create_alarm_days_tv)
    void onDaysClick(){
        if(mDayPickerView == null){
            mDayPickerView = mLayoutInflater.inflate(R.layout.popup_day_picker, mViewGroup, false);
            mDayPickerPopupWindow = new PopupWindow(mDayPickerView,
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            mDaysRecyclerView = mDayPickerView.findViewById(R.id.day_list_recycler);
            mDaysRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            if(mDayAdapter == null){
                mDayAdapter = new DayRecyclerAdapter(CreateAlarmPresenter.sDays);
                mDaysRecyclerView.setAdapter(mDayAdapter);
            }
            mDayAdapter.notifyDataSetChanged();
        }
        mDayPickerPopupWindow.showAtLocation(mView, Gravity.CENTER, ZERO, ZERO);
        mDayAdapter.setOnItemClickListener(new DayRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick() {

            }
        });
        mDaysCancelBtn = mDayPickerView.findViewById(R.id.days_cancel_btn);
        mDaysCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDayPickerPopupWindow.dismiss();
            }
        });
        mDaysOkBtn = mDayPickerView.findViewById(R.id.days_ok_btn);
        mDaysOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.updateDays();
                mDayPickerPopupWindow.dismiss();
            }
        });
    }

    @OnClick(R.id.create_alarm_btn)
    void onCreateClick(){
        if(mCreateBtn.getText().equals("Create")){
            mPresenter.createAlarm(mDescription.getText().toString(), mTime.getText().toString(),
                    mAlarm.getUUID(), mDays.getText().toString(), mAlarm);
        }
        else{
            Log.i("UPDATE", mDescription.getText().toString());
            mPresenter.updateAlarm(mDescription.getText().toString(), mTime.getText().toString(),
                    mDays.getText().toString(), mAlarm);
        }
    }

    @Override
    public void onClickCancelAlarm() {

    }

    @Override
    public void updateTime(String time) {
        mTime.setText(time);

    }

    @Override
    public void updateDescription() {

    }

    @Override
    public void updateDays() {
        mDays.setText(mPresenter.updateDaysText(mDayAdapter.mSelectedDays));
    }

    @Override
    public void onClickCreateAlarm() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AlarmListFragment()).commit();
    }
}
