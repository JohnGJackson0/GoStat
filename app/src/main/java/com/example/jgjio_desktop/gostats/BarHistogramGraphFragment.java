package com.example.jgjio_desktop.gostats;

import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;

import java.util.List;

public class BarHistogramGraphFragment extends Fragment {

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private int mListID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.graph_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);

        if (getViewModel().isListAFrequencyTable(mListID)) {
            populateGraphWithFrequencyTable(rootView);
        }

        return rootView;
    }

    private void populateGraphWithFrequencyTable(View view) {
        GraphView graphView = view.findViewById(R.id.graph);

        BarGraphSeries<com.jjoe64.graphview.series.DataPoint> series = new BarGraphSeries<>();

        getViewModel().getFrequencyIntervalsInTable(mListID).observe(this, new Observer<List<FrequencyInterval>>() {
            @Override
            public void onChanged(@Nullable List<FrequencyInterval> frequencyIntervals) {
                int i=0;
                series.setDataWidth(frequencyIntervals.get(0).getWidth()-.00001);

                graphView.getViewport().setXAxisBoundsManual(true);
                graphView.getViewport().setMinX(frequencyIntervals.get(0).getMinRange()-1);
                graphView.getViewport().setMaxX(frequencyIntervals.get(frequencyIntervals.size()-1).getMaxRange()+1);
                series.setSpacing(10);

                graphView.getViewport().setYAxisBoundsManual(true);
                graphView.getViewport().setMinY(0);
                graphView.getViewport().setMaxY(getViewModel().getMaxFrequency(mListID)+1);



                for (FrequencyInterval freqInterval : frequencyIntervals) {

                    double middleInterval = (freqInterval.getMaxRange() + freqInterval.getMinRange())/2;
                    series.appendData(new com.jjoe64.graphview.series.DataPoint(middleInterval, freqInterval.getFrequency()), false, 500000);
                    i++;

                    Log.d("sdfsdfsdf", Double.toString(middleInterval));
                    Log.d("sdfsdfsdf", Double.toString(freqInterval.getFrequency()));
                    Log.d("sdfsdfsdf", Double.toString(series.getDataWidth()));

                }


            }
        });

        graphView.getViewport().setScalable(false);
        graphView.addSeries(series);
    }

    public static BarHistogramGraphFragment newInstance(int listId) {
        BarHistogramGraphFragment fragment = new BarHistogramGraphFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    private BarHistogramGraphViewModel getViewModel() {
        return ViewModelProviders.of(this).get(BarHistogramGraphViewModel.class);
    }
}
