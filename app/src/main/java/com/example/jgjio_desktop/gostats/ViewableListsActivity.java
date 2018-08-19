package com.example.jgjio_desktop.gostats;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class ViewableListsActivity extends AppCompatActivity {
    AppDatabase mDb;

    private EditText mListNameInput;
    private TextView mListShow;

    private RecyclerView mListItems;
    private ViewableListAdapter mViewableListRecyclerViewAdapter;
    private ListViewModel mDataPointListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListShow = findViewById(R.id.list_show_text);

        mListNameInput = findViewById(R.id.list_name_input);

        mDb = AppDatabase.getAppDatabase(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureNewListDialog();
            }
        });

        configureViewableListRecyclerView();
    }

    private void configureViewableListRecyclerView() {
        mListItems = findViewById(R.id.rv_list_items);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListItems.setLayoutManager(layoutManager);
        mListItems.setHasFixedSize(true);

        mViewableListRecyclerViewAdapter = new ViewableListAdapter();
        mListItems.setAdapter(mViewableListRecyclerViewAdapter);

    }
    //TODO does onResume always keep data up to date?
    //TODO when observing data update database?
    @Override
    public void onResume() {
        super.onResume();

        int numberOfLists = mDb.statisticalListDao().getListCount();

        if (numberOfLists == 0 ) {
            mListShow.setVisibility(View.VISIBLE);
            mListItems.setVisibility(View.INVISIBLE);
        } else {
            mListShow.setVisibility(View.INVISIBLE);
            mListItems.setVisibility(View.VISIBLE);

        }

        for(int i = 1 ; i <= numberOfLists; i++) {
            List<DataPoint> mDataPointsInList = mDb.dataPointDao().getList(i);
            String name = mDb.statisticalListDao().getListName(i);


            ListViewModel newList = new ListViewModel();

            newList.getList().setValue(mDataPointsInList);
            newList.getName().setValue(name);

        }

        mDataPointListModel = ViewModelProviders.of(this).get(ListViewModel.class);

        final Observer<List<DataPoint>> listObserver = new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(final List<DataPoint> newList) {
                //TODO DISPLAY ALL LISTS

            }
        };


        mDataPointListModel.getList().observe(this, listObserver);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_lists, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startEditableListIntent(String name) {
        int newListId = getNewListId();

        Intent intent = new Intent(getApplicationContext(),EditableListActivity.class);
        intent.putExtra("listName", name);

        intent.putExtra("listId", newListId);

        mDb.statisticalListDao().insert(new StatisticalList(newListId, name));

        startActivity(intent);
    }

    public int getNewListId() {
        if (mDb.statisticalListDao().getListCount() == 0) {
            return 1;
        } else {
            return mDb.statisticalListDao().loadAllLists().size() + 1;
        }
    }

    private void configureNewListDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewableListsActivity.this);
        builder.setTitle("Input a List Name");

        final View viewInflated = LayoutInflater.from(ViewableListsActivity.this).inflate(R.layout.inquire_list_name_dialog, (ViewGroup) findViewById(R.id.inquire_list_name), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.list_name_input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String mText = input.getText().toString();

                startEditableListIntent(mText);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
