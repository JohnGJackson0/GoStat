package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class ViewFrequencyListFragment extends Fragment {
    private int mListID;

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_frequency_list_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        configureRecyclerView(rootView);
        setMetaDetails(rootView);
        Button button = rootView.findViewById(R.id.frequency_table_execute_function_button);
        Spinner functionSelector = rootView.findViewById(R.id.frequency_table_functions_spinner);

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

    private void configureRecyclerView(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_interval_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        FrequencyIntervalAdapter adapter = new FrequencyIntervalAdapter();
        recyclerView.setAdapter(adapter);
        getViewModel().getListById(mListID).observe(this, adapter::submitList);
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

    public static ViewFrequencyListFragment newInstance(int listId) {
        ViewFrequencyListFragment fragment = new ViewFrequencyListFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    private ViewFrequencyListViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewFrequencyListViewModel.class);
    }

    void startGraphFragment() {
        //todo

        getFragmentManager().beginTransaction()
                .replace(R.id.view_window, BarHistogramGraphFragment.newInstance(mListID))
                .commit();


    }

    void deleteList(){
        getViewModel().deleteList(mListID);
        getActivity().finish();
    }
}
