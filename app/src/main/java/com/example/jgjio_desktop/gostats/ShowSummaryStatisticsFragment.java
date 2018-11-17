package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

public class ShowSummaryStatisticsFragment extends Fragment {
    private int mListID;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    private ShowSummaryStatisticsViewModel mViewModel;

    public static ShowSummaryStatisticsFragment newInstance(int listID) {
        ShowSummaryStatisticsFragment fragment = new ShowSummaryStatisticsFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        View rootView = inflater.inflate(R.layout.show_summary_statstics_fragment, container, false);
        fillOneVariableStatistics(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    private ShowSummaryStatisticsViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ShowSummaryStatisticsViewModel.class);
    }

    private void fillOneVariableStatistics(View rootView) {
        TextView mean;
        TextView sigma;
        TextView sigmaSquared;
        TextView sampleStandardDeviation;
        TextView standardDeviation;
        TextView numberItems;
        TextView min;
        TextView quartileOne;
        TextView median;
        TextView quartileThree;
        TextView max;

        mean = rootView.findViewById(R.id.summary_statistics_mean_value);
        sigma = rootView.findViewById(R.id.summary_statistics_sigma_x_value);
        sigmaSquared = rootView.findViewById(R.id.summary_statistics_sigma_x_squared_value);
        sampleStandardDeviation = rootView.findViewById(R.id.summary_statistics_sample_standard_deviation_value);
        standardDeviation = rootView.findViewById(R.id.summary_statistics_standard_deviation_value);
        numberItems = rootView.findViewById(R.id.summary_statistics_number_items_value);
        min = rootView.findViewById(R.id.summary_statistics_min_value);
        quartileOne = rootView.findViewById(R.id.summary_statistics_quartile_one_value);
        median = rootView.findViewById(R.id.summary_statistics_median_value);
        quartileThree = rootView.findViewById(R.id.summary_statistics_quartile_three_value);
        max = rootView.findViewById(R.id.summary_statistics_max_value);

        DescriptiveStatistics descStat = new DescriptiveStatistics();

        getViewModel().getList(mListID).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {
                for(DataPoint v :dataPoints) {
                    if (v.isEnabled()) {
                        descStat.addValue(v.getValue());
                    }
                }
                mean.setText(Double.toString(descStat.getMean()));
                sigma.setText(Double.toString(descStat.getSum()));
                sigmaSquared.setText(Double.toString(descStat.getSumsq()));
                sampleStandardDeviation.setText(Double.toString(descStat.getStandardDeviation()));
                standardDeviation.setText(Double.toString(Math.sqrt(descStat.getPopulationVariance())));
                numberItems.setText(Long.toString(descStat.getN()));
                min.setText(Double.toString(descStat.getMin()));
                quartileOne.setText(Double.toString(descStat.getPercentile(25)));
                median.setText(Double.toString(descStat.getPercentile(50)));
                quartileThree.setText(Double.toString(descStat.getPercentile(75)));
                max.setText(Double.toString(descStat.getMax()));
            }
        });

    }


}
