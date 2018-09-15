package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowSummaryStatisticsActivity extends AppCompatActivity {

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private double mListId;
    private TextView mListName;
    private SummaryStatisticsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_summary_statistics);
        mListName = findViewById(R.id.list_name_summary_statistics);

        setListID();

        mViewModel = ViewModelProviders.of(this).get(SummaryStatisticsViewModel.class);

        mListName.setText(mViewModel.getListName(mListId));


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
}
