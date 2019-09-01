package app.goStat.view.functions.functionFragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.goStat.R;
import app.goStat.model.DataPoint;
import app.goStat.model.StatisticalList;
import app.goStat.util.StatisticsTests.TTestUtil;
import app.goStat.util.android.ClipboardUtil;
import app.goStat.view.functions.functionFragments.TestsData.ListsLoader;

public class TTestStatisticsData extends TestStatisticsFragment {

    private TTestDataViewModel mViewModel;
    private View mRootView;

    private EditText mHypothesisMean;
    private Spinner mSelectListSpinner;
    private List<DataPoint> mList;
    private TextView mOutputView;

    private String mAnswer;
    private String mOutput;

    private List<Integer> mCurrentStatIdInOrder;


    public static TTestStatisticsData newInstance() {
        return new TTestStatisticsData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(TTestDataViewModel.class);
        mRootView = inflateFragment(R.layout.fragment_t_test_data,inflater,container);
        mSelectListSpinner = mRootView.findViewById(R.id.select_list_spinner);
        mCurrentStatIdInOrder = new ArrayList<>();
        mHypothesisMean = mRootView.findViewById(R.id.hypothesized_value_edit_text);
        mOutputView = mRootView.findViewById(R.id.output_text_view);

        setSpinner();

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
        //observeList();
        return mRootView;
    }

    private void setSpinner() {
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
                ListsLoader listsLoader = new ListsLoader(getActivity(),mSelectListSpinner);
                listsLoader.loadListsIntoSpinner(statNames);
                observeList();
            }
        });
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



    protected boolean isAnInputEmpty() {
        return ("".equals(mHypothesisMean.getText().toString()));
    }


    private void observeList() {
        mViewModel.getDataPoints(getListID()).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {
                mList = dataPoints;
            }
        });
    }

    private int getListID() {
        if(mCurrentStatIdInOrder.size() >= mSelectListSpinner.getSelectedItemPosition()) {
            return mCurrentStatIdInOrder.get(mSelectListSpinner.getSelectedItemPosition());
        }
        return 0;
    }

    @Override
    protected boolean doesEditTextContainError() {
        return mHypothesisMean.getError() != null;
    }

    @Override
    protected void calculateEqualityVariance() {
        if (isListValid()) {
            double tValue = (double) new TTestUtil().getT(mList,Double.parseDouble(mHypothesisMean.getText().toString()));
            double pValue = (double) new TTestUtil().getPTwoTailed(mList,Double.parseDouble(mHypothesisMean.getText().toString()));
            displayAnswer(tValue,pValue,"two tailed");
        }
    }

    @Override
    protected void calculateMoreThanVariance() {
        if(isListValid()) {
            double tValue = (double) new TTestUtil().getT(mList,Double.parseDouble(mHypothesisMean.getText().toString()));
            double pValue = (double) new TTestUtil().getPMoreThan(mList,Double.parseDouble(mHypothesisMean.getText().toString()));
            displayAnswer(tValue,pValue,"more than");
        }
    }

    @Override
    protected void calculateLessThanVariance() {
        if(isListValid()) {
            double tValue = (double) new TTestUtil().getT(mList,Double.parseDouble(mHypothesisMean.getText().toString()));
            double pValue = (double) new TTestUtil().getPLessThan(mList,Double.parseDouble(mHypothesisMean.getText().toString()));
            displayAnswer(tValue,pValue,"less than");
        }

    }

    private void displayAnswer(double tValue, double pValue, String varianceType){
        Log.d("asjkdhaskjhdjksad","asdhajksd");
        mAnswer = "t = " + tValue + "\np = " + pValue;
        mOutput = mAnswer + "\n\n"+
                "Input:" + "\n" +
                "Hypothesis Value µ0 = " + mHypothesisMean.getText().toString() + "\n" +
                "Variance Type µ = " + varianceType + "\n\n" +
                alphaAndPValueAnalysis(getAlpha(), pValue);

        mOutputView.setText(mOutput);

    }

    private boolean isListValid() {
        if (mList.size() < 2) {
            super.setError(getString(R.string.directions_error_generic_more_than_one_data_entry));
            return false;
        }
        return true;
    }



    @Override
    protected EditText getAlphaEditText() {
        return mRootView.findViewById(R.id.alpha_edit_text);
    }

}
