package com.example.jgjio_desktop.gostats;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;
import java.util.Objects;

public class GraphHistogramFragment extends Fragment {
    public final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 15;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private int mListID;
    private int mAssociatedListID;

    private Button mSnapShotButton;

    GraphView graphView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.histogram_graph_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        resizeFragment(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        populateGraphWithFrequencyTable(rootView);

        mAssociatedListID = getViewModel().getAssociatedListID(mListID);

        mSnapShotButton = rootView.findViewById(R.id.snapshot_button);

        mSnapShotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED)
                            takeSnapShot();

                } else {
                    takeSnapShot();
                }
            }
        });
        return rootView;
    }

    private void takeSnapShot() {
        getViewModel().getStaticListName(mAssociatedListID);
        graphView.takeSnapshotAndShare(getActivity(), getViewModel().getStaticListName(mAssociatedListID)+ " histogram", "GoStats Histogram");
    }

    private void populateGraphWithFrequencyTable(View view) {
        graphView = view.findViewById(R.id.graph);
        BarGraphSeries<com.jjoe64.graphview.series.DataPoint> series = new BarGraphSeries<>();

        getViewModel().getFrequencyIntervalsInTable(mListID).observe(this, new Observer<List<FrequencyInterval>>() {
            @Override
            public void onChanged(@Nullable List<FrequencyInterval> frequencyIntervals) {
                int i=0;
                int maxFrequency = 0;
                series.setDataWidth(frequencyIntervals.get(0).getWidth());

                graphView.getViewport().setXAxisBoundsManual(true);
                graphView.getViewport().setMinX(frequencyIntervals.get(0).getMinRange()-2);
                graphView.getViewport().setMaxX(frequencyIntervals.get(frequencyIntervals.size()-1).getMaxRange()+1);
                series.setSpacing(0);

                graphView.getViewport().setYAxisBoundsManual(true);
                graphView.getViewport().setMinY(0);

                for (FrequencyInterval freqInterval : frequencyIntervals) {
                    double middleInterval = (freqInterval.getMaxRange() + freqInterval.getMinRange())/2;
                    series.appendData(new com.jjoe64.graphview.series.DataPoint(middleInterval, freqInterval.getFrequency()), false, 500000);
                    i++;
                    if (freqInterval.getFrequency() > maxFrequency) maxFrequency = freqInterval.getFrequency();
                }

                graphView.getViewport().setMaxY(maxFrequency +1);


                //try to make the graph look good at edge cases
                if (maxFrequency > 20000 && frequencyIntervals.get(0).getWidth() > 20000) {
                    graphView.getGridLabelRenderer().setNumVerticalLabels(3);
                    graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
                    graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

                } else if (maxFrequency > 20000) {
                    graphView.getGridLabelRenderer().setNumVerticalLabels(3);
                    graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);

                } else if(frequencyIntervals.get(0).getWidth() > 20000) {
                    graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
                    graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
                }

                graphView.getGridLabelRenderer().setGridColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                graphView.getGridLabelRenderer().setVerticalAxisTitle("Frequency");
                graphView.getGridLabelRenderer().setHorizontalAxisTitle("Interval");
                graphView.getGridLabelRenderer().setHorizontalAxisTitleTextSize(getResources().getDimension(R.dimen.text_list_item_titles));
                graphView.getGridLabelRenderer().setVerticalAxisTitleTextSize(getResources().getDimension(R.dimen.text_list_item_titles));


                graphView.getGridLabelRenderer().setVerticalAxisTitleColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryVariant));
                graphView.getGridLabelRenderer().setHorizontalAxisTitleColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryVariant));

                graphView.getGridLabelRenderer().setHorizontalLabelsColor(ContextCompat.getColor(getActivity(), R.color.colorSecondary));
                graphView.getGridLabelRenderer().setVerticalLabelsColor(ContextCompat.getColor(getActivity(), R.color.colorSecondary));


                graphView.setTitleTextSize(getResources().getDimension(R.dimen.text_size_title));

                graphView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_theme_background_color));
            }
        });

        graphView.getViewport().setScalable(false);
        graphView.addSeries(series);
        graphView.setTitleColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        int associatedListID = getViewModel().getAssociatedListID(mListID);

        getViewModel().getName(associatedListID).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //last char of S has the list ID
                getActivity().setTitle("Graphing "+ s);

                graphView.setTitle(s + " Histogram");
            }
        });



        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return ContextCompat.getColor(getActivity(), R.color.colorPrimaryVariant);
            }
        });
    }

    public static GraphHistogramFragment newInstance(int listId) {
        GraphHistogramFragment fragment = new GraphHistogramFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    private void resizeFragment(View rootView, int newWidth, int newHeight) {
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(newWidth, newHeight);
        rootView.setLayoutParams(p);
        rootView.requestLayout();

    }


    private GraphHistogramViewModel getViewModel() {
        return ViewModelProviders.of(this).get(GraphHistogramViewModel.class);
    }
}
