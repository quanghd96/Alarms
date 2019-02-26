package com.practice.mcasey.myapplication.StopWatch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.practice.mcasey.myapplication.R;

import org.hsqldb.lib.StopWatch;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StopWatchFragment extends Fragment {

    @BindView(R.id.stopwatch_text) TextView mStopwatchText;
    StopWatch mStopWatch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        ButterKnife.bind(this, view);
        mStopWatch = new StopWatch();
        mStopWatch.start();
        mStopwatchText.setText(String.valueOf(mStopWatch.elapsedTime()));
        return view;
    }
}
