package com.example.jgjio_desktop.gostats;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private Button mChangeListName;
    private Button mViewOneVarStats;
    private ViewSingleListViewModel mListViewModel;


    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_single_list);
        mListName = findViewById(R.id.listName);
        mListIdText = findViewById(R.id.listId);
        mEditList = findViewById(R.id.editList);
        mChangeListName = findViewById(R.id.changeListName);
        mViewOneVarStats = findViewById(R.id.view_one_var_stats);

        mListId = getIntent().getExtras().getDouble(ViewListDetailsAdapter.EXTRA_LIST_ID);


        mViewableListRecyclerView = findViewById(R.id.rv_single_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mViewableListRecyclerView.setLayoutManager(linearLayoutManager);
        mViewableListAdapter = new ViewableListAdapter(this);
        mViewableListRecyclerView.setAdapter(mViewableListAdapter);

        mListViewModel = ViewModelProviders.of(this).get(ViewSingleListViewModel.class);

        mListViewModel.getList(mListId).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {

                mViewableListAdapter.updateList(dataPoints);
            }
        });

        mListName.setText(mListViewModel.getName(mListId));

        mListIdText.setText("ID: " + Double.toString(mListId));

        mEditList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editList();
            }
        });

        mChangeListName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                changeListNameDialog();
            }
        });

        mViewOneVarStats.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                viewOneVarStats();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //getActionBar().setHomeButtonEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void editList() {
        Intent intent = new Intent(this, EditableListActivity.class);
        intent.putExtra(EXTRA_LIST_ID, mListId);
        startActivity(intent);
    }

    void viewOneVarStats() {
        Intent intent = new Intent(this, ShowSummaryStatisticsActivity.class);
        intent.putExtra(EXTRA_LIST_ID, mListId);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeListNameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewSingleListActivity.this);
        builder.setTitle("Input a List New Name");

        final View viewInflated = LayoutInflater.from(ViewSingleListActivity.this).inflate(R.layout.dialog_inquire_list_name, (ViewGroup) findViewById(R.id.inquire_list_name), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.list_name_input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String mText = input.getText().toString();
                changeListName(mText);
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

    private void changeListName(String newName) {
        mListViewModel.updateListName(newName, (long) mListId);
        mListName.setText(newName);
    }

}
