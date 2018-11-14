package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewEditableListFragment extends Fragment {

    private int mListID;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_editable_list_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        startRecyclerView(rootView);
        return rootView;
    }

    private void startRecyclerView(View rootView) {

        ViewableListAdapter adapter;
        RecyclerView recyclerView;


        recyclerView = rootView.findViewById(R.id.rv_single_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ViewableListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        getViewModel().getListById(mListID).observe(this, adapter::submitList);
    }

    public static ViewEditableListFragment newInstance(int listID) {
        ViewEditableListFragment fragment = new ViewEditableListFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private ViewEditableListFragmentViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewEditableListFragmentViewModel.class);
    }

}
