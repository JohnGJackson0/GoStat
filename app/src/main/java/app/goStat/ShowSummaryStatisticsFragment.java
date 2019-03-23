package app.goStat;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.List;

public class ShowSummaryStatisticsFragment extends Fragment implements View.OnClickListener {
    private int mListID;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private Button mCopyToClipboard;
    private String mToText = "";
    private View mRootView;
    DescriptiveStatistics mDescStat;

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
        mRootView = inflater.inflate(R.layout.show_summary_statstics_fragment, container, false);
        mCopyToClipboard = mRootView.findViewById(R.id.copy_to_clipboard_button);
        mCopyToClipboard.setOnClickListener(this);
        fillOneVariableStatistics(mRootView);
        return mRootView;
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

        getViewModel().getList(mListID).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {
                mDescStat = new DescriptiveStatistics();
                for(DataPoint v :dataPoints) {
                    if (v.isEnabled()) {
                        mDescStat.addValue(v.getValue().doubleValue());
                    }
                }
                mToText =
                        getResources().getString(R.string.text_prefix_for_copy_to_clipboard_summary_statistics) + "\n" +
                        getString(R.string.text_the_label_for_representing_x_bar) + " " + Double.toString(mDescStat.getMean()) + "\n" +
                        getString(R.string.text_the_label_for_representing_summation_of_x) + "  " + Double.toString(mDescStat.getSum()) + "\n" +
                        getString(R.string.text_the_label_for_representing_summation_of_x_squared) + "  " + Double.toString(mDescStat.getSumsq()) + "\n" +
                        getString(R.string.text_the_label_for_representing_sample_standard_deviation_of_x) + "  " + Double.toString(mDescStat.getStandardDeviation()) + "\n" +
                        getString(R.string.text_the_label_for_representing_standard_deviation_of_x) + "  " + Double.toString(Math.sqrt(mDescStat.getPopulationVariance())) + "\n" +
                        getString(R.string.text_the_label_for_representing_the_number_of_items_of_x) + "  " + Long.toString(mDescStat.getN()) + "\n" +
                        getString(R.string.text_the_label_for_representing_the_minimum_of_x) + "  " + Double.toString(mDescStat.getMin()) + "\n" +
                        getString(R.string.text_the_label_for_representing_the_quartile_one_of_the_data) + "  " + Double.toString(mDescStat.getPercentile(25))+ "\n" +
                        getString(R.string.text_the_label_for_representing_the_median_of_the_data) + "  " + Double.toString(mDescStat.getPercentile(50)) + "\n" +
                        getString(R.string.text_the_label_for_representing_the_quartile_three_of_the_data) + "  " + Double.toString(mDescStat.getPercentile(75)) + "\n" +
                        getString(R.string.text_the_label_for_representing_the_maximum_of_x) + "  " + Double.toString(mDescStat.getMax()) + "\n";

                mean.setText(Double.toString(mDescStat.getMean()));
                sigma.setText(Double.toString(mDescStat.getSum()));
                sigmaSquared.setText(Double.toString(mDescStat.getSumsq()));
                sampleStandardDeviation.setText(Double.toString(mDescStat.getStandardDeviation()));
                standardDeviation.setText(Double.toString(Math.sqrt(mDescStat.getPopulationVariance())));
                numberItems.setText(Long.toString(mDescStat.getN()));
                min.setText(Double.toString(mDescStat.getMin()));
                quartileOne.setText(Double.toString(mDescStat.getPercentile(25)));
                median.setText(Double.toString(mDescStat.getPercentile(50)));
                quartileThree.setText(Double.toString(mDescStat.getPercentile(75)));
                max.setText(Double.toString(mDescStat.getMax()));
            }
        });
    }

    private void copyToClipboard(String copyText) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getResources().getString(R.string.meta_label_copy_to_clipboard_one_var_stats), copyText);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onClick(View view) {
        copyToClipboard(mToText);
        showCopyToClipboardMessage();
    }

    private void showCopyToClipboardMessage() {
        Toast toast = Toast.makeText(getActivity(),
                getResources().getString(R.string.toast_copy_to_clipboard_generic),
                Toast.LENGTH_SHORT);
        View view = toast.getView();
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}
