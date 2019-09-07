package app.goStat.view.functions.functionFragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import app.goStat.R;
import app.goStat.model.DataPoint;
import app.goStat.model.StatisticalList;
import app.goStat.view.functions.functionFragments.TestsData.ListsLoader;

public class TwoSampleTTestDataFragment extends TestStatisticsFragment {
    private View mRootView;

    private EditText mListOnePopulationStandardDeviation;
    private EditText mListTwoPopulationStandardDeviation;
    private Spinner mListOneSpinner;
    private Spinner mListTwoSpinner;
    private TwoSampleTTestDataViewModel mViewModel;
    private List<Integer> mCurrentStatIdInOrder;
    private List<DataPoint> mListOne;
    private List<DataPoint> mListTwo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(TwoSampleTTestDataViewModel.class);
        mRootView = inflateFragment(R.layout.fragment_two_sample_t_test_data, inflater, container);

        mListOnePopulationStandardDeviation = mRootView.findViewById(R.id.list_one_population_standard_deviation_edit_text);
        mListTwoPopulationStandardDeviation = mRootView.findViewById(R.id.list_two_population_standard_deviation_edit_text);
        mListOneSpinner = mRootView.findViewById(R.id.list_one_spinner);
        mListTwoSpinner = mRootView.findViewById(R.id.list_two_spinner);
        setSpinners();
        observeLists();
        return mRootView;
    }


    public static TwoSampleTTestDataFragment newInstance() {
        return new TwoSampleTTestDataFragment();
    }

    @Override
    protected boolean isAnInputEmpty() {
        return "".equals(mListOnePopulationStandardDeviation.getText().toString()) ||
                "".equals(mListTwoPopulationStandardDeviation.getText().toString());
    }

    private void setSpinners() {
        List<String> statNames = new ArrayList<>();

        mViewModel.getAllEditableLists().observe(this, new Observer<List<StatisticalList>>() {
            @Override
            public void onChanged(@Nullable List<StatisticalList> statisticalList) {
                mCurrentStatIdInOrder = new ArrayList<>();
                for (StatisticalList item : statisticalList) {
                    if (!item.isFrequencyTable()) {
                        mCurrentStatIdInOrder.add(item.getId());
                        statNames.add(item.getName() + " id = " + item.getId());
                    }
                }
                ListsLoader listOneLoader = new ListsLoader(getActivity(),mListOneSpinner);
                ListsLoader listTwoLoader = new ListsLoader(getActivity(),mListTwoSpinner);
                listOneLoader.loadListsIntoSpinner(statNames);
                listTwoLoader.loadListsIntoSpinner(statNames);
            }
        });
    }

    private void observeLists() {
        mViewModel.getDataPoints(getListOneID()).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {
                mListOne = dataPoints;
            }
        });

        mViewModel.getDataPoints(getListTwoID()).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {
                mListTwo = dataPoints;
            }
        });

    }

    private int getListOneID() {
        if(mCurrentStatIdInOrder.size() >= mListOneSpinner.getSelectedItemPosition()) {
            return mCurrentStatIdInOrder.get(mListOneSpinner.getSelectedItemPosition());
        }
        return 0;
    }

    private int getListTwoID() {
        if(mCurrentStatIdInOrder.size() >= mListTwoSpinner.getSelectedItemPosition()) {
            return mCurrentStatIdInOrder.get(mListTwoSpinner.getSelectedItemPosition());
        }
        return 0;
    }

    @Override
    protected boolean doesEditTextContainError() {
        return false;
    }

    @Override
    protected void calculateEqualityVariance() {

    }

    @Override
    protected void calculateMoreThanVariance() {

    }

    @Override
    protected void calculateLessThanVariance() {

    }

    @Override
    public void onStart() {
        super.onStart();
        mRootView.findViewById(R.id.calculate_button).findViewById(R.id.calculate_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCalculatedClicked();
                    }
                });
    }


    @Override
    protected EditText getAlphaEditText() {
        return mRootView.findViewById(R.id.alpha_edit_text);
    }
}
