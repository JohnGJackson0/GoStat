package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;

public class SummaryStatisticsSelectListActivity extends AppCompatActivity {

    private RecyclerView mListSelection;
    private ShowSummaryStatisticsListSelectionAdapter mListSelectionAdapter;
    private SummaryStatisticsViewModel mSummaryStatisticsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_statistics_select_list);

        setTitle(R.string.select_list_title_for_summary_statistics);

        mListSelection = findViewById(R.id.rv_selection_summary_statistics);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListSelection.setLayoutManager(layoutManager);
        mListSelectionAdapter = new ShowSummaryStatisticsListSelectionAdapter(this);
        mListSelection.setAdapter(mListSelectionAdapter);

        mSummaryStatisticsViewModel = ViewModelProviders.of(this).get(SummaryStatisticsViewModel.class);

        mSummaryStatisticsViewModel.getAllLists().observe(this, new Observer<List<StatisticalList>>() {
            @Override
            public void onChanged(@Nullable List<StatisticalList> statisticalLists) {
                mListSelectionAdapter.update(statisticalLists);


            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
