package com.example.jgjio_desktop.gostats;

import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class FrequencyListFragment  extends Fragment {

    private int mListID;

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frequency_list_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        configureRecyclerView(rootView);
        return rootView;
    }

    public static FrequencyListFragment newInstance(int listId) {
        FrequencyListFragment fragment = new FrequencyListFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
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

    private FrequencyListFragmentViewModel getViewModel() {
        return ViewModelProviders.of(this).get(FrequencyListFragmentViewModel.class);
    }

}
