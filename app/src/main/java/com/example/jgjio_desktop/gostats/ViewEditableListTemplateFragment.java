package com.example.jgjio_desktop.gostats;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewEditableListTemplateFragment extends Fragment {
    private int mListID;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private View mRootView;
    private ViewEditableListFragment dataPointsFragment;
    TextView mListName;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.view_editable_list_template_fragment, container, false);
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

    private void setMetaDetails(View rootView) {
        mListName = rootView.findViewById(R.id.list_name_view);
        TextView id = rootView.findViewById(R.id.list_id_view);

        getViewModel().getListName(mListID).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mListName.setText(s);
            }
        });

        mListName.setMovementMethod(new ScrollingMovementMethod());

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
        addJumpToOnSpinner();
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
        removeJumpToOnSpinner();
        getFragmentTransaction().replace(R.id.view_window, ShowSummaryStatisticsFragment.newInstance(mListID))
                .commit();
    }

    private void showCreateFrequencyTableFragment() {
        removeJumpToOnSpinner();
        if(getViewModel().getStaticNumberOfDataPointsInList(mListID) == 0) {
            Toast toast = Toast.makeText(getActivity(),
                    R.string.no_data_histogram_error_toast_message,
                    Toast.LENGTH_LONG);

            View view = toast.getView();
            int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);

            view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            toast.show();
        } else {
            getFragmentTransaction().replace(R.id.view_window,  CreateFrequencyTableFragment.newInstance(mListID), "Frequency Table")
                    .commit();
        }
    }

    private void removeJumpToOnSpinner(){
        Spinner functionsSpinner = mRootView.findViewById(R.id.editable_list_functions_spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.editable_list_functions_no_jump_to)){

            @Override
            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = (TextView)view.findViewById(android.R.id.text1);
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                return view;

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = (TextView)view.findViewById(android.R.id.text1);
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                return view;

            }
        };

        functionsSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void addJumpToOnSpinner() {
        Spinner functionsSpinner = mRootView.findViewById(R.id.editable_list_functions_spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.editable_list_functions_no_jump_to)) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                return view;

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                return view;

            }
        };
        functionsSpinner.setAdapter(spinnerArrayAdapter);
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
