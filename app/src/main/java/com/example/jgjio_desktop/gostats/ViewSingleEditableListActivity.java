package com.example.jgjio_desktop.gostats;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
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
    private int mListID;
    private TextView mListName;
    private TextView mListIdText;
    private ViewSingleEditableListViewModel mListViewModel;


    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_single_list);
        mListName = findViewById(R.id.listName);
        mListIdText = findViewById(R.id.list_id);
        mListID = getIntent().getExtras().getInt(ViewListDetailsAdapter.EXTRA_LIST_ID);
        mListViewModel = ViewModelProviders.of(this).get(ViewSingleEditableListViewModel.class);

        mListName.setText(mListViewModel.getName(mListID));

        mListIdText.setText("ID: " + Integer.toString(mListID));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showDataPoints();
    }

    private void showDataPoints() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.view_window, ViewEditableListFragment.newInstance(mListID))
        .commit();
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





    //METHODS

    void editList() {
        Intent intent = new Intent(this, EditableListActivity.class);
        intent.putExtra(EXTRA_LIST_ID, mListID);
        startActivity(intent);
    }

    void viewOneVarStats() {
        Intent intent = new Intent(this, ShowSummaryStatisticsActivity.class);
        intent.putExtra(EXTRA_LIST_ID, mListID);
        startActivity(intent);
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

    private void changeListName(String newName) {
        mListViewModel.updateListName(newName, mListID);
        mListName.setText(newName);
    }


    private void jumpTo(int amount) {
        //todo reimplement
    }
}
