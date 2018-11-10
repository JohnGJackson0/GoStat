package com.example.jgjio_desktop.gostats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jgjio_desktop.gostats.ui.viewfrequencylist.ViewFrequencyListFragment;

public class ViewFrequencyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_frequency_list_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ViewFrequencyListFragment.newInstance())
                    .commitNow();
        }
    }
}
