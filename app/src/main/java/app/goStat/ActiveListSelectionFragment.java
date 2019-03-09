package app.goStat;

import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ActiveListSelectionFragment extends Fragment {
    private ViewListDetailsAdapter mListDetailsAdapter;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView mListDetailsRecyclerView;
        rootView = inflater.inflate(R.layout.fragment_clickable_lists_details_rv_container, container, false);
        mListDetailsRecyclerView = rootView.findViewById(R.id.rv_list_details);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mListDetailsRecyclerView.setLayoutManager(linearLayoutManager);
        mListDetailsAdapter = new ViewListDetailsAdapter(getActivity(), this);
        mListDetailsRecyclerView.setAdapter(mListDetailsAdapter);
        registerForContextMenu(mListDetailsRecyclerView);
        getViewModel().getListById().observe(this, mListDetailsAdapter::submitList);
        return rootView;
    }

    private ActiveListSelectionViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ActiveListSelectionViewModel.class);
    }
}
