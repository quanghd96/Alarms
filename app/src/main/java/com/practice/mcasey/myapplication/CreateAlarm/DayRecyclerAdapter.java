package com.practice.mcasey.myapplication.CreateAlarm;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.practice.mcasey.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DayRecyclerAdapter extends RecyclerView.Adapter<DayRecyclerAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onClick();
    }

    List<String> mDays;
    List<String> mSelectedDays = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public DayRecyclerAdapter(List<String> days){mDays = days;}

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_day, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final String day = mDays.get(i);
        viewHolder.mDay.setText(day);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewHolder.mIsChecked){
                    viewHolder.mCheck.setVisibility(View.INVISIBLE);
                    viewHolder.mIsChecked = false;
                    removeDay(day);
                }else{
                    viewHolder.mCheck.setVisibility(View.VISIBLE);
                    viewHolder.mIsChecked = true;
                    selectDay(day);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    private void selectDay(String day){
        if(!mSelectedDays.contains(day))
            mSelectedDays.add(day);
    }

    private void removeDay(String day){
        if(mSelectedDays.contains(day))
            mSelectedDays.remove(day);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mDay;
        ImageView mCheck;
        boolean mIsChecked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDay = itemView.findViewById(R.id.day_tv);
            mCheck = itemView.findViewById(R.id.day_checkbox);
            mCheck.setVisibility(View.INVISIBLE);
        }
    }
}
