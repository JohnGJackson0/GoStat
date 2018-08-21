package com.example.jgjio_desktop.gostats;

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

public class ViewSingleListActivity extends AppCompatActivity {
    private int mListId;
    private ViewableListAdapter mViewableListAdapter;
    private RecyclerView mViewableListRecyclerView;
    private TextView mListName;
    private TextView mListIdText;

    private List<DataPoint> mDataPointsInListSelected;



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

        ListViewModel mListViewModel;

        mListViewModel = ViewModelProviders.of(this).get(ListViewModel.class);

        mListViewModel.getList(mListId).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {
                mViewableListAdapter.updateList(dataPoints);
            }
        });

        mListViewModel.getAllLists().observe(this, new Observer<List<StatisticalList>>() {
            @Override
            public void onChanged(@Nullable List<StatisticalList> allLists) {
                mListName.setText("Name: " + allLists.get(mListId).getName());
                mListIdText.setText("ID: " + Integer.toString(allLists.get(mListId).getId()));
            }
        });

    }
}
