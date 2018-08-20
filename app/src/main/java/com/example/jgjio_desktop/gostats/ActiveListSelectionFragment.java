package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

public class ActiveListSelectionFragment extends Fragment {
    protected View mRvListDetails;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewListDetailsAdapter mListDetailsAdapter;
        RecyclerView mListDetailsRecyclerView;

        View rootView = inflater.inflate(R.layout.fragment_clickable_lists_details_rv_container, container, false);

        mListDetailsRecyclerView = rootView.findViewById(R.id.rv_list_details);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);


        //TODO change span size so that long names = 1 span size and small names take 2 span sizes
        //NOTE SPAN SIZE SHOULD NEVER BE MORE THAN SPAN COUNT
        //EXAMPLE:
        //@Override
        //public int getSpanSize(int position) {
        //return (position%3 == 0 ? 2: 1); }

        mListDetailsRecyclerView.setLayoutManager(gridLayoutManager);
        mListDetailsAdapter = new ViewListDetailsAdapter(getActivity());
        mListDetailsRecyclerView.setAdapter(mListDetailsAdapter);

        ListViewModel mListViewModel;

        mListViewModel = ViewModelProviders.of(this).get(ListViewModel.class);

        mListViewModel.getAllLists().observe(this, new Observer <List<StatisticalList>>() {
            @Override
            public void onChanged(@Nullable final List<StatisticalList> lists) {
                mListDetailsAdapter.setLists(lists);
            }
        });





        return rootView;
    }




}
