package com.example.jgjio_desktop.gostats;

import android.app.AlertDialog;
import android.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelStore;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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

//TODO change all concatenated strings to use string builder
//TODO create and move all resources to their correct places
//TODO make sure all variables are private
//TODO check for name clashes, null inputs and to long of inputs


public class ViewableListsActivity extends AppCompatActivity {
    private Fragment mListDetails;
    private TextView mCreateListInstructions;

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCreateListInstructions = findViewById(R.id.list_show_text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureNewListDialog();
            }
        });

        ActiveListViewModel activeListViewModel;

        activeListViewModel = ViewModelProviders.of(this).get(ActiveListViewModel.class);

        activeListViewModel.getAllLists().observe(this, new Observer<List<StatisticalList>>() {
            @Override
            public void onChanged(@Nullable List<StatisticalList> statisticalLists) {
                if (statisticalLists.size() == 0) {
                    mCreateListInstructions.setVisibility(View.VISIBLE);
                } else {
                    mCreateListInstructions.setVisibility(View.INVISIBLE);
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_lists, menu);
        return true;
    }

    @Override
    protected void onStart() {
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

    //Create a list given the name and return the ID
    public int createList(String name) {
        StatisticalList newList = new StatisticalList(0, name);

        ActiveListViewModel mListViewModel;

        mListViewModel = ViewModelProviders.of(this).get(ActiveListViewModel.class);

        //Insert Statistical List uses an Async Task, that might not
        // be finished by the time we want to get the last entry using
        // sql

        int lastEntry = mListViewModel.getIdOfLastListEntry() + 1;

        mListViewModel.insertStatisticalList(newList);

        return lastEntry;
    }

    public void startEditableListIntent(String name) {
        int listId = createList(name);

        Intent intent = new Intent(getApplicationContext(),EditableListActivity.class);

        intent.putExtra(EXTRA_LIST_ID, listId);

        startActivity(intent);
    }


    private void configureNewListDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewableListsActivity.this);
        builder.setTitle("Input a List Name");

        final View viewInflated = LayoutInflater.from(ViewableListsActivity.this).inflate(R.layout.dialog_inquire_list_name, (ViewGroup) findViewById(R.id.inquire_list_name), false);
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
