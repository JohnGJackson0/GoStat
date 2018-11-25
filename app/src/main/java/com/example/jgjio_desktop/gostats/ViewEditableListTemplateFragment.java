package com.example.jgjio_desktop.gostats;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewEditableListTemplateFragment extends Fragment {
    private int mListID;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private View mRootView;
    private ViewEditableListFragment dataPointsFragment;

    TextView mListName;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.view_editable_list_template_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        setMetaDetails(mRootView);
        showDataPointsFragment();

        Button button = mRootView.findViewById(R.id.editable_list_execute_function_button);
        Spinner functionSelector = mRootView.findViewById(R.id.editable_list_functions_spinner);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int spinnerPosition = functionSelector.getSelectedItemPosition();

                switch (spinnerPosition) {
                    case 0:
                        StartEditListIntent();
                        break;
                    case 1:
                        showDataPointsFragment();
                        break;
                    case 2:
                        showChangeListNameDialog();
                        break;
                    case 3:
                        showOneVarStats();
                        break;
                    case 4:
                        showCreateFrequencyTableFragment();
                        break;
                    case 5:
                        showJumpToDialog();
                        break;
                }
            }
        });

        return mRootView;
    }

    @Override
    public void onResume() {
        getViewModel().deleteDisabledDataPoints(mListID);
        super.onResume();
    }

    @Override
    public void onStart(){
        getViewModel().deleteDisabledDataPoints(mListID);
        super.onStart();
    }

    private void setMetaDetails(View rootView) {
        mListName = rootView.findViewById(R.id.list_name_view);
        TextView id = rootView.findViewById(R.id.list_id_view);

        mListName.setText(getViewModel().getListName(mListID));
        id.setText("id " + Integer.toString(mListID));
    }

    public static ViewEditableListTemplateFragment newInstance(int listID) {
        ViewEditableListTemplateFragment fragment = new ViewEditableListTemplateFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listID);
        fragment.setArguments(args);
        return fragment;
    }

    private ViewEditableListTemplateViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewEditableListTemplateViewModel.class);
    }

    void showDataPointsFragment() {
        dataPointsFragment = ViewEditableListFragment.newInstance(mListID);
        //todo
        getFragmentTransaction().replace(R.id.view_window,  dataPointsFragment, "Editable List")
                .commit();
    }


    private FragmentTransaction getFragmentTransaction() {
        return getFragmentManager().beginTransaction();
    }

    private void StartEditListIntent () {
        Intent intent = new Intent(getActivity(), EditableListActivity.class);
        intent.putExtra(EXTRA_LIST_ID, mListID);
        startActivity(intent);

    }

    private void showChangeListNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Input a List New Name");

        final View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_inquire_list_name, (ViewGroup) getView().findViewById(R.id.inquire_list_name), false);
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

    private void showOneVarStats() {
        getFragmentTransaction().replace(R.id.view_window, ShowSummaryStatisticsFragment.newInstance(mListID))
                .commit();
    }

    private void showCreateFrequencyTableFragment() {
        getFragmentTransaction().replace(R.id.view_window,  CreateFrequencyTableFragment.newInstance(mListID), "Frequency Table")
                .commit();
    }

    private void showJumpToDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Input Index to Jump to");

        final View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_inquire_jump_to_amount, (ViewGroup) getView().findViewById(R.id.inquire_jump_to_amount), false);
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

    private void changeListName(String newName){
        getViewModel().updateListName(newName, mListID);
        mListName.setText(newName);
    }

    private void jumpTo(int amount) {
        dataPointsFragment.jumpTo(amount);
    }
}
