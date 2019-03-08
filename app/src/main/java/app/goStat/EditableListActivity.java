package app.goStat;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import app.goStat.R;

import java.math.BigDecimal;


public class EditableListActivity extends AppCompatActivity implements EditableListAdapter.OnLastEditTextOnEnterCallBack, EditableListAdapter.NewViewHolderReceiverCallBack {
    private EditableListAdapter mEditableDataRowListRecyclerViewAdapter;
    private RecyclerView mEditableListRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private int mListId;
    private boolean initialDataPointAlreadyInserted = false;
    int mPrefetchIndexOfNewlyInsertedItem = 0;
    private EditableListAdapter.NumberViewHolder mActiveViewHolder;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_editable_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getListID();
        configureEditableListRecyclerView();

        Observer observer = new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long numberOfItems) {
                if(initialDataPointAlreadyInserted == false && numberOfItems == 0) {
                    Log.d("EditableListActivity", "onChanged: inserting the first point");
                    createDataElement();
                }
                getViewModel().getNumberOfItemsInList(mListId).removeObserver(this);
            }
        };
        getViewModel().getNumberOfItemsInList(mListId).observe(this, observer);
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
        mLinearLayoutManager = new LinearLayoutManager(this);
        mEditableListRecyclerView.setLayoutManager(mLinearLayoutManager);
        mEditableListRecyclerView.setHasFixedSize(true);
        mEditableDataRowListRecyclerViewAdapter = new EditableListAdapter(this);
        getViewModel().getListById(mListId).observe(this, mEditableDataRowListRecyclerViewAdapter::submitList);
        mEditableListRecyclerView.setAdapter(mEditableDataRowListRecyclerViewAdapter);
        //switch to true for bottom-up list
        mLinearLayoutManager.setStackFromEnd(false);
        mLinearLayoutManager.setReverseLayout(false);
        mEditableListRecyclerView.setPreserveFocusAfterLayout(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy(){
        deleteDisabledDataPoints();
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
        mPrefetchIndexOfNewlyInsertedItem = mEditableDataRowListRecyclerViewAdapter.getItemCount();
        initialDataPointAlreadyInserted = true;
        Log.d("EditableListActivity", "createDataElement: updating non-appending points");
        updateRoom();
        DataPoint newDataPoint = new DataPoint(mListId, false, new BigDecimal("0.0"));
        getViewModel().insertDataPoint(newDataPoint);
        Log.d("EditableListActivity", "createDataElement: calling the ViewModel to insert new point");
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

    @Override
    public void receiveNewestViewHolder(EditableListAdapter.NumberViewHolder vh) {
        mActiveViewHolder = vh;
        Log.d("EditableListActivity", "receiveNewestViewHolder: requesting focus of newest ViewHolder at index(pos+1): " + Integer.toString(mActiveViewHolder.getAdapterPosition()+1));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mPrefetchIndexOfNewlyInsertedItem != 0) {
                    Log.d("EditableListActivity", "run:  requesting focus from index " + Integer.toString(mPrefetchIndexOfNewlyInsertedItem));
                    mActiveViewHolder.mEditableDataPoint.requestFocus();
                    mLinearLayoutManager.scrollToPosition(mPrefetchIndexOfNewlyInsertedItem);
                }
            }
        }, 500);
    }
}
