package com.example.jgjio_desktop.gostats;

import android.os.Bundle;
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

    private int mListId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        if (bundle.getString("listName") !=  null) {
            mName = bundle.getString("listName");
            setTitle("Editing " + mName);
        }

        //TODO fix so that catches exceptions or validates

        if (bundle.getString("listId") !=  null) {
            mListId = Integer.parseInt(bundle.getString("listId"));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListHolder.getInstance().setDataList(mDataList);
                finish();
            }
        });

        configureRecyclerView();
    }

    private void configureRecyclerView() {
        mEditableDataRowList = findViewById(R.id.rv_dataRowList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEditableDataRowList.setLayoutManager(layoutManager);
        mEditableDataRowList.setHasFixedSize(true);
        mEditableDataRowListRecyclerViewAdapter = new EditableListAdapter(mDataList, mListId);
        mEditableDataRowList.setAdapter(mEditableDataRowListRecyclerViewAdapter);
        mEditableDataRowList.setNestedScrollingEnabled(false);
    }

    private int createDataRow() {
        mEditableDataRowListRecyclerViewAdapter.addItem();
        return mEditableDataRowListRecyclerViewAdapter.getItemCount();
    }
}
