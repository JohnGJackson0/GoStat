package app.goStat.view.graphHistogram;


import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.List;
import app.goStat.model.FrequencyInterval;
import app.goStat.R;

public class GraphHistogramFragment extends Fragment {
    public final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 15;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private int mListID;
    BarChart mBarChart;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // due to lifecycle getActivity() or getContext() will return null before this is called.
        View rootView = inflater.inflate(R.layout.histogram_graph_fragment, container, false);
        getActivity().setTitle(getResources().getString(R.string.fragment_label_graph_histogram));
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        mBarChart = (BarChart) rootView.findViewById(R.id.graph);
        graphHistogramChart(rootView);
        shareListener(rootView);
        resizeFragment(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return rootView;
    }

    private void shareListener(View root) {
        Button share = (Button) root.findViewById(R.id.snapshot_button);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //screenshot not yet supported
                //mBarChart.saveToGallery("GoStatHistogram",50);
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

    private void graphHistogramChart(View root){
        int primary = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        int secondary = ContextCompat.getColor(getContext(), R.color.colorSecondary);

        mBarChart.getLegend().setEnabled(false);
        mBarChart.getDescription().setEnabled(false);
        getViewModel().getOnCreatedAssociatedListName(mListID).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mBarChart.getDescription().setEnabled(true);
                mBarChart.getDescription().setText(s);
                //mBarChart.getDescription().setTextSize(getResources().getDimension(R.dimen.text_graph_description));
                //mBarChart.getDescription().setTextColor(secondary);
            }
        });

        getViewModel().getFrequencyIntervalsInTable(mListID).observe(this, new Observer<List<FrequencyInterval>>() {
            @Override
            public void onChanged(@Nullable List<FrequencyInterval> frequencyIntervals) {
                List<BarEntry> barEntries = new ArrayList<>();

                float i = 1f;
                ArrayList<String> bins = new ArrayList<>();

                for (FrequencyInterval freqInterval : frequencyIntervals) {
                    int value = freqInterval.getFrequency();
                    barEntries.add(new BarEntry((float) i, value));
                    i++;
                }

                BarDataSet dataSet = new BarDataSet(barEntries, "example label");
                dataSet.setColor(primary);
                BarData barData = new BarData(dataSet);
                barData.setBarWidth(1);
                mBarChart.setData(barData);
                mBarChart.invalidate(); // refresh
            }
        });
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
