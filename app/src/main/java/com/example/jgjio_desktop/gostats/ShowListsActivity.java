package com.example.jgjio_desktop.gostats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;

public class ShowListsActivity extends AppCompatActivity {
    EditText mListNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListNameInput = findViewById(R.id.list_name_input);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureNewListDialog();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startEditableListIntent(String name) {
        Intent intent = new Intent(getApplicationContext(),EditableListActivity.class);
        intent.putExtra("listName", name);
        startActivity(intent);
    }


    private void configureNewListDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ShowListsActivity.this);
        builder.setTitle("Input a List Name");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        final View viewInflated = LayoutInflater.from(ShowListsActivity.this).inflate(R.layout.inquire_list_name_dialog, (ViewGroup) findViewById(R.id.inquire_list_name), false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.list_name_input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
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
