package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import org.w3c.dom.Text;

import java.util.List;

public class ShowSummaryStatisticsActivity extends AppCompatActivity {

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private double mListId;

    private SummaryStatisticsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_summary_statistics);
        mViewModel = ViewModelProviders.of(this).get(SummaryStatisticsViewModel.class);

        setListID();
        fillListMeta();
        fillOneVariableStatistics();



        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //EXTRA_LIST_ID can come from multiple Views so that
    //changing name from one activity and this, may break
    //sending the list from other activities to this
    private boolean setListID() {
        Bundle bundle = getIntent().getExtras();

        if (bundle.getDouble(EXTRA_LIST_ID) !=  0) {
            mListId = bundle.getDouble(EXTRA_LIST_ID);
            return true;
        } else {
            return false;
        }
    }

    private void fillListMeta() {
        TextView listName;
        TextView listId;

        listName = findViewById(R.id.list_name_summary_statistics);
        listId = findViewById(R.id.list_id_summary_statistics);
        listName.setText(mViewModel.getListName(mListId));
        listId.setText(String.valueOf(mListId));
    }

    private void fillOneVariableStatistics() {
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

        mean = findViewById(R.id.summary_statistics_mean_value);
        sigma = findViewById(R.id.summary_statistics_sigma_x_value);
        sigmaSquared = findViewById(R.id.summary_statistics_sigma_x_squared_value);
        sampleStandardDeviation = findViewById(R.id.summary_statistics_sample_standard_deviation_value);
        standardDeviation = findViewById(R.id.summary_statistics_standard_deviation_value);
        numberItems = findViewById(R.id.summary_statistics_number_items_value);
        min = findViewById(R.id.summary_statistics_min_value);
        quartileOne = findViewById(R.id.summary_statistics_quartile_one_value);
        median = findViewById(R.id.summary_statistics_median_value);
        quartileThree = findViewById(R.id.summary_statistics_quartile_three_value);
        max = findViewById(R.id.summary_statistics_max_value);

        DescriptiveStatistics descStat = new DescriptiveStatistics();

        mViewModel.getList(mListId).observe(this, new Observer<List<DataPoint>>() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
