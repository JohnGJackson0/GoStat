package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.app.AlertDialog.*;

public class EditableListActivity extends AppCompatActivity {
    private EditableListAdapter mEditableDataRowListRecyclerViewAdapter;
    private RecyclerView mEditableDataRowList;
    private double mListId;
    EditableListViewModel editableListViewModel;

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_editable_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setListID();

        editableListViewModel = ViewModelProviders.of(this).get(EditableListViewModel.class);

        setTitle(editableListViewModel.getListName(mListId));

        configureRecyclerView();
    }


    //EXTRA_LIST_ID can come from multiple Views so that
    //changing name from one activity and this, may break
    //sending the list from other activities to this
    private boolean setListID() {
        Bundle bundle = getIntent().getExtras();

        if (bundle.getDouble(EXTRA_LIST_ID) !=  0) {
            mListId = bundle.getDouble(EXTRA_LIST_ID);
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
        mEditableDataRowListRecyclerViewAdapter = new EditableListAdapter(this);
        editableListViewModel.getListById((long) mListId).observe(this, mEditableDataRowListRecyclerViewAdapter::submitList);


        mEditableDataRowList.setAdapter(mEditableDataRowListRecyclerViewAdapter);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        Builder builder = new Builder(this);

        builder.setTitle("Save Changes?");
        builder.setMessage("Do you want to save your changes? ");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mEditableDataRowListRecyclerViewAdapter.update();
                finish();
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.show();
    }

    public double getListID() {
        return mListId;
    }
}
