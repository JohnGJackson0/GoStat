package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewFrequencyListTemplateFragment extends Fragment {
    private int mListID;

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_frequency_list_template_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        setMetaDetails(rootView);
        startViewFragment();

        Button button = rootView.findViewById(R.id.editable_list_execute_function_button);
        Spinner functionSelector = rootView.findViewById(R.id.editable_list_functions_spinner);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int spinnerPosition = functionSelector.getSelectedItemPosition();

                switch (spinnerPosition) {
                    case 0:
                        startGraphFragment();
                        break;
                    case 1:
                        deleteList();
                        break;
                }
            }
        });

        return rootView;
    }

    private void setMetaDetails(View rootView) {
        TextView name = rootView.findViewById(R.id.list_name_view);
        name.setText(getViewModel().getListName(mListID));
        TextView id = rootView.findViewById(R.id.list_id_view);
        id.setText("id " + Integer.toString(mListID));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static ViewFrequencyListTemplateFragment newInstance(int listId) {
        ViewFrequencyListTemplateFragment fragment = new ViewFrequencyListTemplateFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    private ViewFrequencyListTemplateViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewFrequencyListTemplateViewModel.class);
    }

    void startGraphFragment() {
        //todo needs to use getFragment Manager
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,  BarHistogramGraphFragment.newInstance(mListID), "BarFragment")
                .commit();

        fragmentTransaction.addToBackStack(null);
    }

    void startViewFragment() {
        //todo
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.view_window,  FrequencyListFragment.newInstance(mListID), "FrequencyList")
                .commit();

    }

    void deleteList(){
        getViewModel().deleteList(mListID);
        getActivity().finish();
    }
}
