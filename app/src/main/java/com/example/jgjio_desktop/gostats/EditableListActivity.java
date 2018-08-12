package com.example.jgjio_desktop.gostats;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class EditableListActivity extends AppCompatActivity {
    private RecyclerViewEditableListAdapter mEditableDataRowListRecyclerViewAdapter;
    private RecyclerView mEditableDataRowList;
    private List<DataPoint> dataList = new ArrayList<DataPoint>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        if (bundle.getString("listName") !=  null) {
            setTitle("Editing " + bundle.getString("listName"));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        configureRecyclerView();
        //createDataRow();
    }

    private void configureRecyclerView() {
        mEditableDataRowList = findViewById(R.id.rv_dataRowList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEditableDataRowList.setLayoutManager(layoutManager);
        mEditableDataRowList.setHasFixedSize(true);
        mEditableDataRowListRecyclerViewAdapter = new RecyclerViewEditableListAdapter(dataList);
        mEditableDataRowList.setAdapter(mEditableDataRowListRecyclerViewAdapter);
        mEditableDataRowList.setNestedScrollingEnabled(false);
    }

    private int createDataRow() {
        mEditableDataRowListRecyclerViewAdapter.addItem();
        return mEditableDataRowListRecyclerViewAdapter.getItemCount();
    }
}
