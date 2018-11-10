package com.example.jgjio_desktop.gostats;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//TODO edit List button
//TODO fix layout bug, not correctly showing last entry

public class ViewSingleEditableListActivity extends AppCompatActivity {
    private int mListId;
    private ViewableListAdapter mViewableListAdapter;
    private RecyclerView mViewableListRecyclerView;
    private TextView mListName;
    private TextView mListIdText;
    private Button mEditList;
    private Button mChangeListName;
    private Button mViewOneVarStats;
    private Button mJumpTo;
    private ViewSingleEditableListViewModel mListViewModel;
    LinearLayoutManager linearLayoutManager;


    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_single_list);
        mListName = findViewById(R.id.listName);
        mListIdText = findViewById(R.id.list_id);
        mEditList = findViewById(R.id.editList);
        mChangeListName = findViewById(R.id.change_list_name);
        mViewOneVarStats = findViewById(R.id.view_one_var_stats);
        mJumpTo = findViewById(R.id.jump_to);

        mListId = getIntent().getExtras().getInt(ViewListDetailsAdapter.EXTRA_LIST_ID);
        mListViewModel = ViewModelProviders.of(this).get(ViewSingleEditableListViewModel.class);
        mViewableListRecyclerView = findViewById(R.id.rv_single_list);

        linearLayoutManager = new LinearLayoutManager(this);
        mViewableListRecyclerView.setLayoutManager(linearLayoutManager);
        mViewableListAdapter = new ViewableListAdapter();
        mListViewModel.getListById(mListId).observe(this, mViewableListAdapter::submitList);
        mViewableListRecyclerView.setAdapter(mViewableListAdapter);
        mViewableListRecyclerView.setHasFixedSize(true);


        mListName.setText(mListViewModel.getName(mListId));

        mListIdText.setText("ID: " + Integer.toString(mListId));

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

        mJumpTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToDialog();
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

    //todo: validate input for this and changing a list name and creating a list name
    private void jumpToDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewSingleEditableListActivity.this);
        builder.setTitle("Input Index to Jump to");

        final View viewInflated = LayoutInflater.from(ViewSingleEditableListActivity.this).inflate(R.layout.dialog_inquire_jump_to_amount, (ViewGroup) findViewById(R.id.inquire_jump_to_amount), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.jump_to_amount_input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int jumpToAmount = Integer.parseInt(input.getText().toString());
                jumpTo(jumpToAmount-1); //Statistical Lists start at 1
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

    private void changeListNameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewSingleEditableListActivity.this);
        builder.setTitle("Input a List New Name");

        final View viewInflated = LayoutInflater.from(ViewSingleEditableListActivity.this).inflate(R.layout.dialog_inquire_list_name, (ViewGroup) findViewById(R.id.inquire_list_name), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.list_name_input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String text = input.getText().toString();
                changeListName(text);
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
        mListViewModel.updateListName(newName, mListId);
        mListName.setText(newName);
    }


    private void jumpTo(int amount) {
        mViewableListRecyclerView.scrollToPosition(amount);
    }
}
