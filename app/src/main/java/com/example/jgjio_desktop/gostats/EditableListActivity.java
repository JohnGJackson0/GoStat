package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class EditableListActivity extends AppCompatActivity {
    private EditableListAdapter mEditableDataRowListRecyclerViewAdapter;
    private RecyclerView mEditableDataRowList;
    private List<DataPoint> mDataList = new ArrayList<DataPoint>();
    private String mName;
    AppDatabase mDb;
    private int mListId;
    EditableListViewModel editableListViewModel;

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDb = AppDatabase.getAppDatabase(this);

        setListID();

        Log.d("EditableListActivity: ", "ID RECIEVED: " + Integer.toString(mListId));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditableDataRowListRecyclerViewAdapter.updateDatabase();
                finish();
            }
        });

        editableListViewModel = ViewModelProviders.of(this).get(EditableListViewModel.class);

        setTitle(editableListViewModel.getListName(mListId));

        configureRecyclerView();
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

    private void configureRecyclerView() {
        mEditableDataRowList = findViewById(R.id.rv_dataRowList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEditableDataRowList.setLayoutManager(layoutManager);
        mEditableDataRowList.setHasFixedSize(true);
        mEditableDataRowListRecyclerViewAdapter = new EditableListAdapter(mListId, this);
        mEditableDataRowList.setAdapter(mEditableDataRowListRecyclerViewAdapter);
        mEditableDataRowList.setNestedScrollingEnabled(false);

        editableListViewModel.getDataPointsInList(mListId).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {
                mEditableDataRowListRecyclerViewAdapter.updateList(dataPoints);
            }
        });

    }

    void displayInputInvalidDialog(int position) {
        Log.d("EditableListActivity","displayInvalidDialogCalled.");
        Log.d("posInvalidInput",Integer.toString(position));
    }
}
