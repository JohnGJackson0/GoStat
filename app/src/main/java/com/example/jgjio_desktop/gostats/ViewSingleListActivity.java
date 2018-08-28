package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

//TODO edit List button
//TODO fix layout bug, not correctly showing last entry

public class ViewSingleListActivity extends AppCompatActivity {
    private double mListId;
    private ViewableListAdapter mViewableListAdapter;
    private RecyclerView mViewableListRecyclerView;
    private TextView mListName;
    private TextView mListIdText;
    private Button mEditList;

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_single_list);
        mListName = findViewById(R.id.listName);
        mListIdText = findViewById(R.id.listId);
        mEditList = findViewById(R.id.editList);

        mListId = getIntent().getExtras().getDouble(ViewListDetailsAdapter.EXTRA_LIST_ID);


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

        mListName.setText("Name " + mListViewModel.getName(mListId));

        mListIdText.setText("ID: " + Double.toString(mListId));

        mEditList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editList(mListId);
            }
        });
    }

    void editList(double listId) {
        Log.d("ListID: ", Double.toString(listId));
        Intent intent = new Intent(this, EditableListActivity.class);
        intent.putExtra(EXTRA_LIST_ID, listId);
        startActivity(intent);
    }
}
