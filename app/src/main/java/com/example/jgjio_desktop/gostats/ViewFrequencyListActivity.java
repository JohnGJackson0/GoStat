package com.example.jgjio_desktop.gostats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ViewFrequencyListActivity extends AppCompatActivity {
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private int mListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListID();
        setContentView(R.layout.view_frequency_list_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ViewFrequencyListTemplateFragment.newInstance(mListId))
                    .commitNow();
        }
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
}
