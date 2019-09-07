package app.goStat.view.functions.functionFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.goStat.R;
import app.goStat.util.StatisticsTests.TTestUtil;
import app.goStat.util.android.ClipboardUtil;
import app.goStat.util.android.TextValidator;

public class TTestStatisticsStatsFragment extends TestStatisticsFragment {

    private View mRootView;
    private EditText mHypothesisValue;
    private EditText mSampleMean;
    private EditText mSampleStandardDeviation;
    private EditText mSampleSize;
    private TextView mOutputView;
    private EditText mAlphaOptionalEditText;

    private String mAnswer;
    private String mOutput;

    private enum Variance {
        EQUALITY,
        MORETHAN,
        LESSTHAN,
        ;
    }

    public TTestStatisticsStatsFragment() {
        // Required empty public constructor
    }

    public static TTestStatisticsStatsFragment newInstance() {
        TTestStatisticsStatsFragment fragment = new TTestStatisticsStatsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflateFragment(R.layout.fragment_t_test_stats,inflater,container);

        mHypothesisValue = mRootView.findViewById(R.id.hypothesized_value_edit_text);
        mSampleMean = mRootView.findViewById(R.id.mean_of_sample_edit_text);
        mSampleStandardDeviation = mRootView.findViewById(R.id.sample_standard_deviation_edit_text);
        mSampleSize = mRootView.findViewById(R.id.sample_size_edit_text_view);
        mOutputView = mRootView.findViewById(R.id.output_text_view);
        mAlphaOptionalEditText = mRootView.findViewById(R.id.alpha_edit_text);

        validateNMoreThan1();

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

    private void validateNMoreThan1() {
        mSampleSize.addTextChangedListener(new TextValidator(mSampleSize) {
            @Override
            public void validate(TextView textView, String text) {
                if(!"".equals(mSampleSize.getText().toString())) {
                    try {
                        int val = Integer.parseInt(mSampleSize.getText().toString());
                        if (val<2) {
                            mSampleSize.setError(getString(R.string.directions_error_sample_size_less_than_2));
                        }
                    } catch (Exception e) {
                        mSampleSize.setError(getString(R.string.directions_sample_size_format_error));
                    }
                }
            }
        });
    }

    protected boolean doesEditTextContainError() {
        return (mHypothesisValue.getError() != null ||
                mSampleMean.getError() != null ||
                mSampleStandardDeviation.getError() != null) ||
                mSampleSize.getError() != null;
    }

    protected boolean isAnInputEmpty() {
        return ("".equals(mHypothesisValue.getText().toString()) ||
                "".equals(mSampleMean.getText().toString()) ||
                "".equals(mSampleStandardDeviation.getText().toString()) ||
                "".equals(mSampleSize.getText().toString()));
    }

    protected void calculateEqualityVariance() {
        //calculate equality test
        double hypothesisValue = Double.parseDouble(mHypothesisValue.getText().toString());
        double sampleMean = Double.parseDouble(mSampleMean.getText().toString());
        double sampleStandardDeviation = Double.parseDouble(mSampleStandardDeviation.getText().toString());
        double sampleSize = Double.parseDouble(mSampleSize.getText().toString());
        double answerT = new TTestUtil().getT(sampleMean,hypothesisValue,sampleSize,sampleStandardDeviation);
        double answerP = new TTestUtil().getPTwoTailed(sampleSize-1, answerT);

        String realAnswerT = Double.toString(answerT);
        String realAnswerP = Double.toString(answerP);

        mAnswer = "t value = " + realAnswerT + "\n" + "p value = " + realAnswerP;


        mOutput =  mAnswer + "\n\n"+
                "Input:" + "\n" +
                "Hypothesis Value µ0 = " + mHypothesisValue.getText().toString() + "\n" +
                "Sample Mean x̅ =" + mSampleMean.getText().toString() + "\n" +
                "Sample Standard Deviation Sx = " + mSampleStandardDeviation.getText().toString() + "\n" +
                "Sample Size n = " + mSampleSize.getText().toString() + "\n" +
                "Variance Type µ = " + "two tailed" + "\n\n" +
                alphaAndPValueAnalysis(getAlpha(), answerP);

        mOutputView.setText(mOutput);
    }
    protected void calculateMoreThanVariance() {
        double hypothesisValue = Double.parseDouble(mHypothesisValue.getText().toString());
        double sampleMean = Double.parseDouble(mSampleMean.getText().toString());
        double sampleStandardDeviation = Double.parseDouble(mSampleStandardDeviation.getText().toString());
        double sampleSize = Double.parseDouble(mSampleSize.getText().toString());

        double answerT = new TTestUtil().getT(sampleMean,hypothesisValue,sampleSize,sampleStandardDeviation);
        double answerP = new TTestUtil().getPMoreThan(sampleSize-1, answerT);

        String realAnswerT = Double.toString(answerT);
        String realAnswerP = Double.toString(answerP);

        mAnswer = "t value = " + realAnswerT + "\n" + "p value = " + realAnswerP;
        mOutput =  mAnswer + "\n\n"+
                "Input:" + "\n" +
                "Hypothesis Value µ0 = " + mHypothesisValue.getText().toString() + "\n" +
                "Sample Mean x̅ =" + mSampleMean.getText().toString() + "\n" +
                "Sample Standard Deviation Sx = " + mSampleStandardDeviation.getText().toString() + "\n" +
                "Sample Size n = " + mSampleSize.getText().toString() + "\n" +
                "Variance Type µ = " + "One tailed more than (&gt;)" + "\n\n" +
                alphaAndPValueAnalysis(getAlpha(), answerP);

        mOutputView.setText(mOutput);
    }

    protected void calculateLessThanVariance() {
        double hypothesisValue = Double.parseDouble(mHypothesisValue.getText().toString());
        double sampleMean = Double.parseDouble(mSampleMean.getText().toString());
        double sampleStandardDeviation = Double.parseDouble(mSampleStandardDeviation.getText().toString());
        double sampleSize = Double.parseDouble(mSampleSize.getText().toString());

        double answerT = new TTestUtil().getT(sampleMean,hypothesisValue,sampleSize,sampleStandardDeviation);
        double answerP = new TTestUtil().getPLessThan(sampleSize-1, answerT);

        String realAnswerT = Double.toString(answerT);
        String realAnswerP = Double.toString(answerP);

        mAnswer = "t value = " + realAnswerT + "\n" + "p value = " + realAnswerP;
        mOutput =  mAnswer + "\n\n"+
                "Input:" + "\n" +
                "Hypothesis Value µ0 = " + mHypothesisValue.getText().toString() + "\n" +
                "Sample Mean x̅ =" + mSampleMean.getText().toString() + "\n" +
                "Sample Standard Deviation Sx = " + mSampleStandardDeviation.getText().toString() + "\n" +
                "Sample Size n = " + mSampleSize.getText().toString() + "\n" +
                "variance type µ = " + "One tailed less than (&lt;)" + "\n\n" +
                alphaAndPValueAnalysis(getAlpha(), answerP);

        mOutputView.setText(mOutput);
    }

    @Override
    protected EditText getAlphaEditText() {
        return mAlphaOptionalEditText;
    }

}
