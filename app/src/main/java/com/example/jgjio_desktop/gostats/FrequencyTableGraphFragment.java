package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FrequencyTableGraphFragment extends Fragment {

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    public static final String NUMBER_OF_BINS = "com.example.jgjio_desktop.gostats.extra.NUMBER_OF_BINS";

    private int mListID;
    private int mNumberOfBins;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.graph_fragment, container, false);
        initializeBundle();
        createFrequencyTable();
        quickDebug();
        return rootView;
    }

    private void initializeBundle() {
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        mNumberOfBins = getArguments().getInt(NUMBER_OF_BINS);
    }

    private void quickDebug() {
        Log.d("histogramGraphData", "bin width: " + Double.toString(getBinWidth()));
        Log.d("histogramGraphData1", "# of bins : " + Integer.toString(mNumberOfBins));
        Log.d("histogramGraphData3", "list id : " + Double.toString(mListID));
    }

    public static FrequencyTableGraphFragment newInstance(int listId, int numberOfBins) {
        FrequencyTableGraphFragment fragment = new FrequencyTableGraphFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        args.putInt(NUMBER_OF_BINS, numberOfBins);
        fragment.setArguments(args);
        return fragment;
    }

    private void populateGraph(View view) {
        GraphView graphView = (GraphView) view.findViewById(R.id.graph);

        HistogramGraphViewModel vm = ViewModelProviders.of(this).get(HistogramGraphViewModel.class);

        BarGraphSeries<com.jjoe64.graphview.series.DataPoint> series = new BarGraphSeries<com.jjoe64.graphview.series.DataPoint>();

        vm.getList(mListID).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<com.example.jgjio_desktop.gostats.DataPoint> dataPoints) {
                int i = 0;
                for(DataPoint v :dataPoints) {
                    double val = v.getValue();
                    if (v.isEnabled()) {
                        series.appendData(new com.jjoe64.graphview.series.DataPoint(i, v.getValue()), false, 500000);
                        i++;
                    }
                }
            }
        });

        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);
        graphView.addSeries(series);
    }

    //todo add names to store min and max freq
    private void createFrequencyTable() {
        FrequencyTable frequencyTable = getFrequencyIntervals();

        final LiveData<List<DataPoint>> listObservable = getViewModel().getList(mListID);

        listObservable.observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {
                listObservable.removeObserver(this);
                for(ExclusiveEndMixedFrequencyInterval i : frequencyTable.get()) {
                    for(DataPoint val : dataPoints) {
                        Log.d("Testing, ", "dsfs");

                        if (val.getValue() >= i.getMin() && val.getValue() < i.getMax()) {
                            i.addAFrequency();
                        }
                    }
                }

                addFrequencyTable(frequencyTable);
            }
        });


    }

    //todo check for within range of min max
    //todo convert live data to static or remove observer
    //todo check if max, min from vm counts disabled datapoints as possible min, max and check for other areas for that error

    private void addFrequencyTable(FrequencyTable frequencyTable) {

        StatisticalList newList = new StatisticalList(0, "FREQUENCY TABLE FOR LIST ID:" + Integer.toString(mListID), true);

        int newListID = getViewModel().insertStatisticalList(newList);

        List<FrequencyInterval> newFrequencyIntervals = new ArrayList<>();

        for(ExclusiveEndMixedFrequencyInterval freqInterval : frequencyTable.get()) {
            newFrequencyIntervals.add(new FrequencyInterval(0, freqInterval.getFrequency(),
                    freqInterval.getMin(), freqInterval.getMax(), true, false, newListID));
        }

        getViewModel().insertFrequencyIntervals(newFrequencyIntervals);

    }

    private FrequencyTable getFrequencyIntervals() {
        double binWidth = getBinWidth();
        double min = getMinValue();
        ExclusiveEndMixedFrequencyInterval[] frequencyIntervals = new ExclusiveEndMixedFrequencyInterval[mNumberOfBins];

        for(int i = 0; i < mNumberOfBins; i++) {
            Log.d("Frequency interval ", "interval # " +  Integer.toString(i));
            Log.d("Frequency interval min ", Double.toString((min +(binWidth*i))));
            Log.d("Frequency interval max ", Double.toString((min +(binWidth*(i+1)))));
            frequencyIntervals[i] = new ExclusiveEndMixedFrequencyInterval(0, (min +(binWidth*i)),  (min +(binWidth*(i+1))));
        }

        FrequencyTable val = new FrequencyTable(Arrays.asList(frequencyIntervals));

        return val;
    }


    private double getMaxValue(){
        return getViewModel().getMaxValue(mListID);
    }

    private double getMinValue(){
        return getViewModel().getMinValue(mListID);
    }

    private long getListLength() {
        return getViewModel().getNumberOfDataPointsInList(mListID);
    }

    private double getBinWidth(){
        double max = getViewModel().getMaxValue(mListID);
        double min = getViewModel().getMinValue(mListID);
        double rangeOfValues = max - min;

        return (rangeOfValues / mNumberOfBins)+.1;
    }

    private HistogramGraphViewModel getViewModel() {
        return ViewModelProviders.of(this).get(HistogramGraphViewModel.class);
    }


}
