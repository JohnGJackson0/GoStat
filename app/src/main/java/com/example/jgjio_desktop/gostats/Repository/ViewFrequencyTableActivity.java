package com.example.jgjio_desktop.gostats.Repository;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.jgjio_desktop.gostats.R;

public class ViewFrequencyTableActivity extends AppCompatActivity {
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private int mListId;
    String mCopyToClipboardText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListID();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.view_frequency_table_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ViewFrequencyTableTemplateFragment.newInstance(mListId))
                    .commitNow();
        }
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

    //EXTRA_LIST_ID can come from multiple Views so that
    //changing name from one activity and this, may break
    //sending the list from other activities to this
    private boolean setListID() {
        Bundle bundle = getIntent().getExtras();

        if (bundle.getInt(EXTRA_LIST_ID) !=  0) {
            mListId = bundle.getInt(EXTRA_LIST_ID);
            return true;
        } else {
            return false;
        }
    }

    private ViewFrequencyTableViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewFrequencyTableViewModel.class);
    }

}