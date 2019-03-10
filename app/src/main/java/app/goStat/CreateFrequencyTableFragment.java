package app.goStat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateFrequencyTableFragment extends Fragment {

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private int mListID;
    private Button mGenerateBin;
    private Button mCreateFrequencyTable;
    private EditText mBinInput;
    private TextView mErrorMessage;
    private int mNumberOfRecommendedBins;
    private int mNumberOfBins;
    private TextView mInstructions;
    private double mMinOfDataSet = Double.MAX_VALUE;
    private double mMaxOfDataSet = 0.;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_frequency_table_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        initializeLayoutComponents(rootView);
        getViewModel().getNumberOfDataPointsInList(mListID).observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long listLength) {
                calcNumberOfRecommendedBins(listLength);
            }
        });
        createOnClickListeners();
        return rootView;
    }

    private void initializeLayoutComponents(View rootView) {
        mGenerateBin = rootView.findViewById(R.id.generate_bin_number_button);
        mCreateFrequencyTable = rootView.findViewById(R.id.create_frequency_table_button);
        mBinInput = rootView.findViewById(R.id.bin_number_input);
        mErrorMessage = rootView.findViewById(R.id.histogram_settings_error_message);
        mInstructions = rootView.findViewById(R.id.instructions_histogram_settings);
    }

    //square root choice algorithm
    //https://en.wikipedia.org/wiki/Histogram#Number_of_bins_and_width

    private void calcNumberOfRecommendedBins(long listLength) { mNumberOfRecommendedBins = (int) Math.ceil(Math.sqrt(listLength)); }

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
                mBinInput.setText(Integer.toString(mNumberOfRecommendedBins));
            }
        });

        mCreateFrequencyTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mBinInput.getText().toString().isEmpty() || Integer.parseInt(mBinInput.getText().toString()) < 1) {
                        mErrorMessage.setText(R.string.histogram_bin_input_error_message);
                        mErrorMessage.setVisibility(View.VISIBLE);
                    } else {
                        //todo fix below but low priority, unlikely to happen with current app implementation
                        //if(getViewModel().getStaticNumberOfDataPointsInList(mListID) == 0) {
                           // mErrorMessage.setText(R.string.histogram_list_no_longer_contains_data_error_message);
                            //mErrorMessage.setVisibility(View.VISIBLE);
                        //}
                        mNumberOfBins = Integer.parseInt(mBinInput.getText().toString());
                        createFrequencyTable();
                    }
                } catch(NumberFormatException e) {
                    mErrorMessage.setText(R.string.histogram_bin_input_error_message);
                    mErrorMessage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private CreateFrequencyTableViewModel getViewModel() {
        return ViewModelProviders.of(this).get(CreateFrequencyTableViewModel.class);
    }

    private void createFrequencyTable() {
        final LiveData<List<DataPoint>> listObservable = getViewModel().getList(mListID);

        listObservable.observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {
                listObservable.removeObserver(this);

                for (DataPoint val : dataPoints) {
                    if(val.getValue().doubleValue() < mMinOfDataSet) mMinOfDataSet = val.getValue().doubleValue();
                    if(val.getValue().doubleValue() > mMaxOfDataSet) mMaxOfDataSet = val.getValue().doubleValue();
                }

                FrequencyTable frequencyTable = getFrequencyIntervals(mNumberOfBins);
                int freqIntervalIndex = 0;

                for(ExclusiveEndMixedFrequencyInterval i : frequencyTable.get()) {
                    freqIntervalIndex++;
                    for(DataPoint val : dataPoints) {

                        if (val.getValue().doubleValue() >= i.getMin() && val.getValue().doubleValue() < i.getMax()) {
                            i.addAFrequency();
                        }

                        /*
                         * The following code is important. I added extra to bin width
                         * (a value of .1 added) so it would include the last values of the data
                         * (given the mixed frequency interval is a non-inclusive end)
                         * but given that the program migrated to much larger values
                         * (and computing with doubles in general) these smaller values are truncated
                         * in the process. These frequencies should be added.
                         */

                        if(frequencyTable.numberOfFrequencyIntervals() == freqIntervalIndex) {
                            if (val.getValue().doubleValue() >= i.getMax()){
                                i.addAFrequency();
                            }

                        }
                    }
                }
                addFrequencyTable(frequencyTable);
            }
        });
    }

    private void addFrequencyTable(FrequencyTable frequencyTable) {
        String associatedListName = getViewModel().getStaticListName(mListID);
        StatisticalList newList = new StatisticalList(0, "~ Frequency Table for ID " + Integer.toString(mListID), true, associatedListName);
        int newListID = getViewModel().insertStatisticalList(newList);
        List<FrequencyInterval> newFrequencyIntervals = new ArrayList<>();

        for(ExclusiveEndMixedFrequencyInterval freqInterval : frequencyTable.get()) {
            newFrequencyIntervals.add(new FrequencyInterval(0, freqInterval.getFrequency(),
                    freqInterval.getMin(), freqInterval.getMax(), true, false, newListID));
        }
        getViewModel().insertFrequencyIntervals(newFrequencyIntervals);
        showFrequencyTable(newListID);
    }

    private void showFrequencyTable(int listID) {
        Intent intent = new Intent(getActivity(), ViewFrequencyTableActivity.class);
        intent.putExtra(EXTRA_LIST_ID, listID);
        startActivity(intent);
    }

    private FrequencyTable getFrequencyIntervals(int numberOfBins) {
        double binWidth = getBinWidth();
        double min = mMinOfDataSet;
        ExclusiveEndMixedFrequencyInterval[] frequencyIntervals = new ExclusiveEndMixedFrequencyInterval[numberOfBins];

        for(int i = 0; i < numberOfBins; i++) {
            frequencyIntervals[i] = new ExclusiveEndMixedFrequencyInterval(0, (min +(binWidth*i)),  (min +(binWidth*(i+1))));
        }

        FrequencyTable val = new FrequencyTable(Arrays.asList(frequencyIntervals));
        return val;
    }

    /*
    * given min and max of dataset, the program divides the interval
    * evenly to the number of bins. The frequencies are closed end,
    * which means we need to add a small amount to the interval.
    * todo add it instead to the max, or figure out a better solution
     */
    private double getBinWidth(){
        return ((mMaxOfDataSet - mMinOfDataSet) / mNumberOfBins)+.1;
    }
}
