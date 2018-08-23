package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

//TODO edit List button
//TODO fix layout bug, not correctly showing last entry

public class ViewSingleListActivity extends AppCompatActivity {
    private int mListId;
    private ViewableListAdapter mViewableListAdapter;
    private RecyclerView mViewableListRecyclerView;
    private TextView mListName;
    private TextView mListIdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_single_list);
        mListName = findViewById(R.id.listName);
        mListIdText = findViewById(R.id.listId);

        mListId = getIntent().getExtras().getInt(ViewListDetailsAdapter.EXTRA_LIST_ID);


        mViewableListRecyclerView = findViewById(R.id.rv_single_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mViewableListRecyclerView.setLayoutManager(linearLayoutManager);
        mViewableListAdapter = new ViewableListAdapter(this);
        mViewableListRecyclerView.setAdapter(mViewableListAdapter);

        ViewSingleListViewModel mListViewModel;

        mListViewModel = ViewModelProviders.of(this).get(ViewSingleListViewModel.class);

        mListViewModel.getList(mListId).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {

                mViewableListAdapter.updateList(dataPoints);
            }
        });

        mListViewModel.getName(mListId).observe(this, new Observer<String> () {
            @Override
            public void onChanged(@Nullable String name) {
                mListName.setText("Name: " + name);
            }


        });
        mListIdText.setText("ID: " + Integer.toString(mListId));

    }
}
