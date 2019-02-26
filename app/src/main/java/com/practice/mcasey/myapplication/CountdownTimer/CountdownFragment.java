package com.practice.mcasey.myapplication.CountdownTimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.mcasey.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CountdownFragment extends Fragment {

    private static final int INTERVAL = 1000;

    @BindView(R.id.countdown_tv) TextView mTimerTV;
    @BindView(R.id.time_enter) EditText mTimerET;
    @BindView(R.id.cancel_timer) Button mCancel;
    @BindView(R.id.start_timer) Button mStart;
    CountDownTimer mTimer;
    int mTimeInt;
    View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_countdown_timer, container, false);
        ButterKnife.bind(this, mView);
        getInput();
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
            Toast.makeText(getActivity(), "ADD", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.start_timer)
    public void onStartTimer(){
        if(mStart.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start))){
            setMillisInFuture(mTimerET.getText().toString());
            createTimer();
            mStart.setText(R.string.pause);
            mTimer.start();
        } else if(mStart.getText().toString().equalsIgnoreCase(getResources().getString(R.string.resume))){
            createTimer();
            mStart.setText(R.string.pause);
            mTimer.start();
        } else if(mStart.getText().toString().equalsIgnoreCase(getResources().getString(R.string.pause))){
            String pausedTimer = mTimerTV.getText().toString();
            setMillisInFuture(pausedTimer);
            mStart.setText(R.string.resume);
            mTimer.cancel();
            //TODO set timer to paused time
        }
    }

    @OnClick(R.id.cancel_timer)
    public void onCancelTimer(){
        mTimer.cancel();
        mStart.setText(R.string.start);
        mTimerTV.setText(R.string.countdown_tab);
        mTimerET.setText("");
    }

    private void getInput(){
        mTimerET.addTextChangedListener(new TextWatcher() {

            boolean backspace = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                backspace = i2 == 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

                //TODO set protocols to not allow out of bounds things (i.e. 65 seconds etc)
                StringBuilder sb = new StringBuilder(s.toString());
                boolean updateView = false;
                if(sb.length() == 3 && !backspace){
                    if(sb.charAt(1) != ':'){
                        sb = sb.insert(1, ':');
                        updateView = true;
                    }
                } else if(sb.length() == 5 && !backspace){
                    if(sb.charAt(1) == ':'){
                        sb = sb.deleteCharAt(1);
                        sb = sb.insert(2, ':');
                        updateView = true;
                    }
                } else if(sb.length() == 6 && !backspace){
                    if(sb.charAt(2) == ':'){
                        sb = sb.deleteCharAt(2);
                        sb = sb.insert(1, ':');
                        sb = sb.insert(4, ':');
                        updateView = true;
                    }
                } else if(sb.length() == 7 && !backspace){
                    if(sb.charAt(1) != ':' && sb.charAt(0) != 'D'){
                        sb = sb.deleteCharAt(2).insert(2, ':');
                        sb = sb.deleteCharAt(5).insert(4, ':');
                        updateView = true;
                    }
                } else if(sb.length() == 8 && !backspace){
                    if(sb.charAt(1) == ':'){
                        sb = sb.deleteCharAt(1).insert(2, ':');
                        sb = sb.deleteCharAt(4).insert(5, ':');
                    }
                }
                if (updateView) {
                    mTimerET.setText(sb);
                    mTimerET.setSelection(mTimerET.getText().length());
                    StringBuilder sbString = sb;
                    for(int i=0;i<sbString.length();i++){
                        if(sbString.charAt(i) == ':')
                            sbString.deleteCharAt(i);
                    }
                    mTimeInt = Integer.parseInt(sbString.toString());
                }
            }
        });
    }

    private void createTimer(){
        mTimer = new CountDownTimer(mTimeInt, INTERVAL){
            @Override
            public void onTick(long l) {
                setTimerTextView(l);
            }

            @Override
            public void onFinish() {
                mTimerTV.setText(R.string.done);
                mStart.setText(R.string.start);
            }
        };
    }

    private void setMillisInFuture(String millisInFutureString){
        millisInFutureString = stripString(millisInFutureString);
        mTimeInt = 0;
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        if(millisInFutureString.length()==1)
            seconds = Integer.parseInt(millisInFutureString
                    .substring(millisInFutureString.length()-1, millisInFutureString.length()));
        else if(millisInFutureString.length()>=2)
            seconds = Integer.parseInt(millisInFutureString
                    .substring(millisInFutureString.length()-2, millisInFutureString.length()));
        if(millisInFutureString.length()==3)
            minutes = Integer.parseInt(millisInFutureString
                    .substring(millisInFutureString.length()-3, millisInFutureString.length()-2));
        else if(millisInFutureString.length()>=4)
            minutes = Integer.parseInt(millisInFutureString
                    .substring(millisInFutureString.length()-4, millisInFutureString.length()-2));
        if(millisInFutureString.length()==5)
            hours = Integer.parseInt(millisInFutureString
                    .substring(millisInFutureString.length()-5, millisInFutureString.length()-4));
        else if(millisInFutureString.length()==6)
            hours = Integer.parseInt(millisInFutureString
                    .substring(millisInFutureString.length()-6, millisInFutureString.length()-4));
        mTimeInt += hours * 60;
        mTimeInt += minutes * 60;
        mTimeInt += seconds;
        mTimeInt = mTimeInt * INTERVAL;
    }

    private String stripString(String time){
        return time.replaceAll(":", "");
    }

    private void setTimerTextView(long l){
        String l_seconds = String.valueOf(l%60000);
        String l_minutes = String.valueOf(l/60000);
        String l_hours = String.valueOf(l/3600000);
        if(l_seconds.length()==5)
            l_seconds = l_seconds.substring(0,2);
        else if(l_seconds.length()==3)
            l_seconds = getResources().getString(R.string.double_zero);
        else
            l_seconds = R.string.zero+l_seconds.substring(0,1);
        String a = stripString(mTimerET.getText().toString());
        if(a.length()==1){
            mTimerTV.setText(R.string.zero+l_seconds.substring(0,2));
        }else if(a.length()==2){
            mTimerTV.setText(l_seconds.substring(0,2));
        }else if(a.length()==3){
            mTimerTV.setText(l_minutes+":"+l_seconds.substring(0,2));
        }else if(a.length()==4){
            mTimerTV.setText(l_minutes+":"+l_seconds.substring(0,2));
        }else if(a.length()==5){
            mTimerTV.setText(l_hours+":"+l_minutes+":"+l_seconds.substring(0,2));
        }else if(a.length()==6){
            mTimerTV.setText(l_hours+":"+l_minutes+":"+l_seconds.substring(0,2));
        }
    }
}
