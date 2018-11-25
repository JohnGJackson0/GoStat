package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


public class EditableListActivity extends AppCompatActivity implements EditableListAdapter.OnLastEditTextOnEnterCallBack {
    private EditableListAdapter mEditableDataRowListRecyclerViewAdapter;
    private RecyclerView mEditableListRecyclerView;
    private int mListId;
    EditableListViewModel editableListViewModel;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_editable_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getListID();
        configureEditableListRecyclerView();

        setTitle("Editing List");
    }

    //EXTRA_LIST_ID can come from multiple Views so that
    //changing name from one activity and this, may break
    //sending the list from other activities to this
    private boolean getListID() {
        Bundle bundle = getIntent().getExtras();

        if (bundle.getInt(EXTRA_LIST_ID) !=  0) {
            mListId = bundle.getInt(EXTRA_LIST_ID);
            return true;
        } else {
           return false;
        }
    }

    private void configureEditableListRecyclerView() {
        mEditableListRecyclerView = findViewById(R.id.rv_editable_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEditableListRecyclerView.setLayoutManager(layoutManager);
        mEditableListRecyclerView.setHasFixedSize(true);
        mEditableDataRowListRecyclerViewAdapter = new EditableListAdapter(this);
        getViewModel().getListById(mListId).observe(this, mEditableDataRowListRecyclerViewAdapter::submitList);
        mEditableListRecyclerView.setAdapter(mEditableDataRowListRecyclerViewAdapter);

        mEditableDataRowListRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                if (itemCount == 1) {
                    mEditableListRecyclerView.smoothScrollToPosition(mEditableDataRowListRecyclerViewAdapter.getItemCount());
                    mEditableDataRowListRecyclerViewAdapter.setLastEditTextToRequestFocus();
                }
            }
        });
    }

    @Override
    public void onStart() {
        if(getViewModel().getNumberOfItemsInList(mListId) == 0)
            createDataElement();

        super.onStart();
    }

    @Override
    public void onDestroy(){
        updateRoom();
        super.onDestroy();
    }

    @Override
    public void onStop(){
        updateRoom();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        updateRoom();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        updateRoom();
        finish();
        return true;
    }

    @Override
    public void createDataElement() {
        updateRoom();
        DataPoint newDataPoint = new DataPoint(mListId, false, 0.0);
        getViewModel().insertDataPoint(newDataPoint);
    }

    //dataPoints that are appended to the list already are updated.
    //We just need to update the changes

    //Also, inserting DataPoints simply replace, because we use OnConflictStrategy.REPLACE in the Dao 
    protected void updateRoom() {
        getViewModel().insertDataPoints(mEditableDataRowListRecyclerViewAdapter.getPendingUpdates());
    }

    public void deleteDisabledDataPoints() {
        getViewModel().deleteDisabledDataPointsFromList(mListId);
    }

    private EditableListViewModel getViewModel() {
        return ViewModelProviders.of(this).get(EditableListViewModel.class);
    }

}
