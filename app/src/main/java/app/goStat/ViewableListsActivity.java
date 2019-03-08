package app.goStat;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import app.goStat.R;

import java.util.List;

public class ViewableListsActivity extends AppCompatActivity {
    private TextView mCreateListInstructions;
    View viewListDetails;
    View instructionsContainer;

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    public static final String ACTION_ID = "com.example.jgjio_desktop.gostats.extra.action_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewListDetails = findViewById(R.id.view_list_details_fragment);
        instructionsContainer = findViewById(R.id.instructions_container);

        mCreateListInstructions = findViewById(R.id.list_show_text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureNewListDialog();
            }
        });

        getViewModel().getAllLists().observe(this, new Observer<List<StatisticalList>>() {
            @Override
            public void onChanged(@Nullable List<StatisticalList> statisticalLists) {
                if (statisticalLists.size() == 0) {
                    mCreateListInstructions.setVisibility(View.VISIBLE);
                    viewListDetails.setVisibility(View.GONE);
                } else {
                    instructionsContainer.setVisibility(View.GONE);
                    mCreateListInstructions.setVisibility(View.GONE);
                    viewListDetails.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void removeList(StatisticalList statList) {
        getViewModel().deleteList(statList.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_lists, menu);
        return true;
    }

    //Create a list given the name and return the ID
    private int createList(String name) {
        int lastEntry = (int) getViewModel().insertStatisticalList(new StatisticalList(0, name, false));

        //find last entry

        return lastEntry;
    }

    private void startEditableListIntent(String name) {
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

    private ViewableListsViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewableListsViewModel.class);
    }
}
