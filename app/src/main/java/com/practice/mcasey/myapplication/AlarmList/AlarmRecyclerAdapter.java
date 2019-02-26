package com.practice.mcasey.myapplication.AlarmList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.practice.mcasey.myapplication.Model.Alarm;
import com.practice.mcasey.myapplication.Model.AlarmSingleton;
import com.practice.mcasey.myapplication.R;

import java.util.List;

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onClick();
        void onDelete();
    }

    public int adapterPosition = 0;
    List<Alarm> mAlarms;
    Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public AlarmRecyclerAdapter(List<Alarm> alarms, Context context) {
        mAlarms = alarms;
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_alarm, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Alarm alarm = mAlarms.get(i);
        adapterPosition = viewHolder.getAdapterPosition();
        viewHolder.mTime.setText(alarm.getTime());
        viewHolder.mDescription.setText(alarm.getAlarmDescription());
        viewHolder.mEnabled.setChecked(alarm.isEnabled());
        viewHolder.mEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AlarmSingleton.getInstance(mContext).deleteAlarm(alarm);
                //notifyDataSetChanged();
            }
        });
        viewHolder.mEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == false){
                    alarm.setEnabled(false);
                    mOnItemClickListener.onDelete();

                }
            }
        });
        viewHolder.mDays.setText(alarm.getDays());
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTime;
        TextView mDescription;
        TextView mDays;
        Switch mEnabled;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTime = itemView.findViewById(R.id.tv_list_time);
            mDescription = itemView.findViewById(R.id.tv_list_description);
            mDays = itemView.findViewById(R.id.tv_list_day);
            mEnabled = itemView.findViewById(R.id.switch_list_enabled);
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onClick();
        }
    }
}
