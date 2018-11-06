package com.example.jgjio_desktop.gostats;


import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.List;

public class PointGraphFragment extends Fragment {

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private int mListID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.graph_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        populateGraph(rootView);
        return rootView;
    }

    private void populateGraph(View view) {
        GraphView graphView = (GraphView) view.findViewById(R.id.graph);
        GraphViewModel vm = ViewModelProviders.of(this).get(GraphViewModel.class);
        PointsGraphSeries<com.jjoe64.graphview.series.DataPoint> series = new PointsGraphSeries<com.jjoe64.graphview.series.DataPoint>();
        vm.getList(mListID).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<com.example.jgjio_desktop.gostats.DataPoint> dataPoints) {
                int i = 0;
                for(DataPoint v :dataPoints) {
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

    public static PointGraphFragment newInstance(int listId) {
        PointGraphFragment fragment = new PointGraphFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }
}
