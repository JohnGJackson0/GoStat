package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateFrequencyTableFragment extends Fragment {

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private int mListID;
    private Button mGenerateBin;
    private Button mGraphHistogram;
    private EditText mBinInput;
    private TextView mErrorMessage;
    private int mNumberOfBins;
    private TextView mInstructions;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.histogram_graph_settings_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        initializeLayoutComponents(rootView);

        if (getViewModel().isFrequencyTable(mListID)) {
            clearLayout();
            displayAlreadyHistogramErrorMessage(rootView);

        } else {
            calcNumberOfBins();
            createOnClickListeners();
        }

        return rootView;
    }

    private void initializeLayoutComponents(View rootView) {
        mGenerateBin = rootView.findViewById(R.id.generate_bin_number_button);
        mGraphHistogram = rootView.findViewById(R.id.graph_histogram_button);
        mBinInput = rootView.findViewById(R.id.bin_number_input);
        mErrorMessage = rootView.findViewById(R.id.histogram_settings_error_message);
        mInstructions = rootView.findViewById(R.id.instructions_histogram_settings);
    }

    private void clearLayout() {
        mInstructions.setVisibility(View.GONE);
        mGenerateBin.setVisibility(View.GONE);
        mGraphHistogram.setVisibility(View.GONE);
        mBinInput.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
    }

    private void displayAlreadyHistogramErrorMessage(View view) {
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText(R.string.already_histogram_error_message);
    }

    //Square-root choice
    //this is used by many statistics programs like excel
    //there are several different alternatives we could possibly support here
    //https://en.wikipedia.org/wiki/Histogram#Number_of_bins_and_width

    private void calcNumberOfBins() {
        CreateFrequencyTableViewModel vm = ViewModelProviders.of(this).get(CreateFrequencyTableViewModel.class);
        long listLength = vm.getNumberOfDataPointsInList(mListID);
        mNumberOfBins = (int) Math.ceil(Math.sqrt(listLength));
    }

    public static CreateFrequencyTableFragment newInstance(int listId) {
        CreateFrequencyTableFragment fragment = new CreateFrequencyTableFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    private void createOnClickListeners() {
        mGenerateBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinInput.setText(Integer.toString(mNumberOfBins));
            }
        });

        mGraphHistogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBinInput.getText().toString().isEmpty()) {
                    mErrorMessage.setText(R.string.histogram_bin_input_error_message);
                    mErrorMessage.setVisibility(View.VISIBLE);
                } else {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, FrequencyTableGraphFragment.newInstance(mListID, mNumberOfBins));
                    ft.commit();
                }
            }
        });
    }

    private CreateFrequencyTableViewModel getViewModel() {
        return ViewModelProviders.of(this).get(CreateFrequencyTableViewModel.class);
    }

}
