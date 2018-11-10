package com.example.jgjio_desktop.gostats.ui.viewfrequencylist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jgjio_desktop.gostats.R;

public class ViewFrequencyListFragment extends Fragment {

    private ViewFrequencyListViewModel mViewModel;

    public static ViewFrequencyListFragment newInstance() {
        return new ViewFrequencyListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_frequency_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ViewFrequencyListViewModel.class);
        // TODO: Use the ViewModel
    }

}
