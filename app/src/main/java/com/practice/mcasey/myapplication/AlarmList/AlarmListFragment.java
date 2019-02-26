package com.practice.mcasey.myapplication.AlarmList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.practice.mcasey.myapplication.CreateAlarm.CreateAlarmFragment;
import com.practice.mcasey.myapplication.MainActivity;
import com.practice.mcasey.myapplication.Model.Alarm;
import com.practice.mcasey.myapplication.Model.AlarmSingleton;
import com.practice.mcasey.myapplication.R;
import com.practice.mcasey.myapplication.Services.AlarmService;
import com.practice.mcasey.myapplication.Services.NotificationService;
import com.practice.mcasey.myapplication.TriggerAlarm.TriggerAlarmFragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmListFragment extends Fragment implements AlarmListView {

    public static final String NOTIFICATION_ALARM_TIME = "NOTIFICATION_ALARM_TIME";
    public static final String NOTIFICATION_ALARM_DESC = "NOTIFICATION_ALARM_DESC";

    Bundle mBundle;
    View mView;
    AlarmRecyclerAdapter mAdapter;
    List<Alarm> mAlarms = new ArrayList<>();
    Uri URIRingtone;

    public static Ringtone ringtone;

    public Intent mAlarmService;

    private AlarmListPresenter mPresenter;

    @BindView(R.id.alarm_recycler_view) RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        URIRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_alarm_list, container, false);
        ButterKnife.bind(this, mView);
        ringtone = RingtoneManager.getRingtone(getContext(), URIRingtone);
        mPresenter = new AlarmListPresenter(this);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAlarmService = new Intent(getActivity(), AlarmService.class);
        if(mAlarms != null && !mAlarms.isEmpty())
            getActivity().startService(mAlarmService);
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_create, menu);
        MenuItem createItem = menu.findItem(R.id.menu_new_alarm);
        createItem.setTitle(R.string.create_alarm);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_new_alarm:
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
                        .replace(R.id.fragment_container, new CreateAlarmFragment()).commit();
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI(){
        mAlarms = AlarmSingleton.getInstance(getContext()).getAlarms();
        mPresenter.sortAlarms(mAlarms);
        Log.i("LIST", mAlarms.toString());
        if(mAdapter == null){
            mAdapter = new AlarmRecyclerAdapter(mAlarms, getContext());
            mRecyclerView.setAdapter(mAdapter);
        }
        else
            mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new AlarmRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick() {
                mBundle = new Bundle();
                mBundle.putParcelable(CreateAlarmFragment.CREATE_ALARM,
                        Parcels.wrap(mAlarms.get(mAdapter.adapterPosition)));
                Fragment fragment = new CreateAlarmFragment();
                fragment.setArguments(mBundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }

            @Override
            public void onDelete() {
                AlarmSingleton.getInstance(getContext()).deleteAlarm(mAlarms.get(mAdapter.adapterPosition));
                if(mAlarms.size()==1){
                    mAlarms.clear();
                    mAdapter.notifyDataSetChanged();
                }
                else{
                    mAlarms.remove(mAdapter.adapterPosition);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        getActivity().stopService(mAlarmService);
        if(!mAlarms.isEmpty())
            getActivity().startService(mAlarmService);
    }

    @Override
    public void onClickCreateNewAlarm() {

    }

    @Override
    public void onClickAlarm() {

    }

    @Override
    public void onClickDisable() {

    }

    public static class AlarmReceiver extends BroadcastReceiver{

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.sNotificationService = new Intent(context, NotificationService.class);

            Uri ringtoneUri = MediaStore.Audio.Media.getContentUriForPath("content://media/Carbon/audio/media");
            Ringtone  r = RingtoneManager.getRingtone(context, ringtoneUri);

            Fragment fragment = new TriggerAlarmFragment();
            String description = intent.getStringExtra(AlarmService.ALARM_TRIGGERED);
            Bundle bundle = new Bundle();
            bundle.putString(AlarmService.ALARM_TRIGGERED, description);
            fragment.setArguments(bundle);
            MainActivity.fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            r.play();

            MainActivity.sNotificationService.putExtra(NOTIFICATION_ALARM_TIME, intent.getStringExtra(AlarmService.NOTIFICATION_ALARM_TIME));
            MainActivity.sNotificationService.putExtra(NOTIFICATION_ALARM_DESC, description);
            context.startService(MainActivity.sNotificationService);

        }
    }
}
