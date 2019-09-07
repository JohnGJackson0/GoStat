package app.goStat.view.functions.functionFragments.TwoSampleTTestDistribution;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.goStat.R;
import app.goStat.model.DataPoint;
import app.goStat.model.StatisticalList;
import app.goStat.util.android.ClipboardUtil;
import app.goStat.view.functions.functionFragments.TestStatisticsFragment;
import app.goStat.view.functions.functionFragments.TestsData.ListsLoader;

public class TwoSampleTTestDataFragment extends TestStatisticsFragment {
    private View mRootView;

    private Spinner mListOneSpinner;
    private Spinner mListTwoSpinner;
    private TwoSampleTTestDataViewModel mViewModel;
    private List<Integer> mCurrentStatIdInOrder = new ArrayList<>();
    private List<DataPoint> mListOne;
    private List<DataPoint> mListTwo;
    private boolean isPooled = false;
    private TextView mOutputView;

    private Observer mListOneObserver;
    private Observer mListTwoObserver;

    private String mAnswer = "";
    private String mOutput = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(TwoSampleTTestDataViewModel.class);
        mRootView = inflateFragment(R.layout.fragment_two_sample_t_test_data, inflater, container);
        mOutputView = mRootView.findViewById(R.id.output_text_view);
        mListOneSpinner = mRootView.findViewById(R.id.list_one_spinner);
        mListTwoSpinner = mRootView.findViewById(R.id.list_two_spinner);
        setListOneSpinnerListener();
        setListTwoSpinnerListener();
        setSpinners();
        createPooledRadioGroupListener();

        mListOneObserver = (Observer<List<DataPoint>>) dataPoints -> mListOne = dataPoints;
        mListTwoObserver = (Observer<List<DataPoint>>) dataPoints -> mListTwo = dataPoints;

        Button copyAnswer =  mRootView.findViewById(R.id.copy_answer_button);

        copyAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyAnswerToClipboard();
            }
        });

        Button copyAllText =  mRootView.findViewById(R.id.copy_all_text_button);

        copyAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyAllTextToClipboard();
            }
        });
        return mRootView;
    }


    public static TwoSampleTTestDataFragment newInstance() {
        return new TwoSampleTTestDataFragment();
    }

    @Override
    protected boolean isAnInputEmpty() {
        return false;
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

    private void copyAnswerToClipboard(){
        ClipboardUtil clip = new ClipboardUtil();
        clip.copyToClipboard(mAnswer, getActivity());
        clip.showCopyToClipboardMessage(getActivity());
    }

    private void copyAllTextToClipboard(){
        ClipboardUtil clip = new ClipboardUtil();
        clip.copyToClipboard(mOutput,getActivity());
        clip.showCopyToClipboardMessage(getActivity());
    }

    private void setListOneSpinnerListener() {

        mListOneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    //position starts at 0
            {
                setListOne(mCurrentStatIdInOrder.get(position));
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }

    private void setListTwoSpinnerListener() {
        mListTwoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            //position starts at 0
            {
                setListTwo(mCurrentStatIdInOrder.get(position));
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }

    protected void createPooledRadioGroupListener () {
        RadioGroup variances = mRootView.findViewById(R.id.pooled_radio_group);
        variances.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch(checkedID) {
                    case R.id.no_pooled_radio_button:
                        isPooled = false;
                        break;
                    case R.id.yes_pooled_radio_button:
                        isPooled = true;
                        break;
                }
            }
        });
    }
    private void setListOne(int id) {
        mViewModel.getDataPoints(id).observe(this, mListOneObserver);
    }

    private void setListTwo(int id) {
        mViewModel.getDataPoints(id).observe(this, mListTwoObserver);
    }

    @Override
    protected boolean doesEditTextContainError() {
        return false;
    }

    @Override
    protected void calculateEqualityVariance() {
        TDistributionTest distributionTest;

        if (isPooled) {
            distributionTest = new PooledTwoTailedDistributionTest(mListOne,mListTwo);
        } else{
            distributionTest = new NotPooledTwoTailedDistributionTest(mListOne,mListTwo);
        }

        mOutputView.setText(distributionTest.toString());
        mOutput = distributionTest.toString();
        mAnswer = "t = " + distributionTest.getT() + " \n"
                + "p = " + distributionTest.getP();

    }

    @Override
    protected void calculateMoreThanVariance() {
        TDistributionTest distributionTest;

        if (isPooled) {
            distributionTest = new PooledMoreThanDistributionTest(mListOne,mListTwo);
        } else{
            distributionTest = new NotPooledMoreThanDistributionTest(mListOne,mListTwo);
        }

        mOutputView.setText(distributionTest.toString());
        mOutput = distributionTest.toString();
        mAnswer = "t = " + distributionTest.getT() + " \n"
                + "p = " + distributionTest.getP();
    }

    @Override
    protected void calculateLessThanVariance() {
        TDistributionTest distributionTest;
        if (isPooled) {
            distributionTest = new PooledLessThanDistributionTest(mListOne,mListTwo);
        } else{
            distributionTest = new NotPooledLessThanDistributionTest(mListOne,mListTwo);
        }

        mOutputView.setText(distributionTest.toString());
        mOutput = distributionTest.toString();
        mAnswer = "t = " + distributionTest.getT() + " \n"
                + "p = " + distributionTest.getP();
    }

    @Override
    public void onStart() {
        super.onStart();
        mRootView.findViewById(R.id.calculate_button).findViewById(R.id.calculate_button)
                .setOnClickListener(view -> onCalculatedClicked());
    }

    @Override
    protected EditText getAlphaEditText() {
        return mRootView.findViewById(R.id.alpha_edit_text);
    }
}
