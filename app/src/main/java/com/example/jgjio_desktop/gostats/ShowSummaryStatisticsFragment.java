package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;

public class ShowSummaryStatisticsFragment extends Fragment implements View.OnClickListener {
    private int mListID;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    private Button mCopyToClipboard;
    private String toText = "";

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

        mCopyToClipboard = rootView.findViewById(R.id.copy_to_clipboard_button);
        mCopyToClipboard.setOnClickListener(this);

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
                toText="1 variable Stats : Go! Statistics\n" +
                        getString(R.string.summary_mean_label) + " " + Double.toString(descStat.getMean()) + "\n" +
                        getString(R.string.summary_sigma_label) + "  " + Double.toString(descStat.getSum()) + "\n" +
                        getString(R.string.summary_sigma_squared_label) + "  " + Double.toString(descStat.getSumsq()) + "\n" +
                        getString(R.string.summary_sample_standard_deviation_label) + "  " + Double.toString(descStat.getStandardDeviation()) + "\n" +
                        getString(R.string.summary_standard_deviation_label) + "  " + Double.toString(Math.sqrt(descStat.getPopulationVariance())) + "\n" +
                        getString(R.string.summary_number_of_items_label) + "  " + Long.toString(descStat.getN()) + "\n" +
                        getString(R.string.summary_min_label) + "  " + Double.toString(descStat.getMin()) + "\n" +
                        getString(R.string.summary_quartile_one_label) + "  " + Double.toString(descStat.getPercentile(25))+ "\n" +
                        getString(R.string.summary_median_label) + "  " + Double.toString(descStat.getPercentile(50)) + "\n" +
                        getString(R.string.summary_quartile_three_label) + "  " + Double.toString(descStat.getPercentile(75)) + "\n" +
                        getString(R.string.summary_max_label) + "  " + Double.toString(descStat.getMax()) + "\n";


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


    private void copyToClipboard(String copyText) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("1-var-stats", copyText);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onClick(View view) {
        copyToClipboard(toText);
        showCopyToClipboardMessage();
    }

    private void showCopyToClipboardMessage() {
        Toast toast = Toast.makeText(getActivity(),
                "Copied To Clipboard",
                Toast.LENGTH_SHORT);

        View view = toast.getView();
        TextView text = view.findViewById(R.id.message);

        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        toast.show();

    }
}
