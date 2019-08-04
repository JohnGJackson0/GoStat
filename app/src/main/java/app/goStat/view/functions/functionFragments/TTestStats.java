package app.goStat.view.functions.functionFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.math.BigDecimal;

import app.goStat.R;
import app.goStat.util.TTest;
import app.goStat.util.android.ClipboardUtil;
import app.goStat.util.android.TextValidator;

public class TTestStats extends Fragment {

    private View mRootView;
    private EditText mHypothesisValue;
    private EditText mSampleMean;
    private EditText mSampleStandardDeviation;
    private EditText mSampleSize;
    private Button mCalculate;
    private TextView mOutputView;
    private Variance mCurrentVariance;
    private EditText mAlphaOptionalEditText;

    private String mAnswer;
    private String mOutput;

    private RadioGroup variances;

    private enum Variance {
        EQUALITY,
        MORETHAN,
        LESSTHAN,
        ;
    }

    public TTestStats() {
        // Required empty public constructor
    }

    public static TTestStats newInstance() {
        TTestStats fragment = new TTestStats();
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
        mRootView = inflater.inflate(R.layout.fragment_t_test_stats, container, false);

        mHypothesisValue = mRootView.findViewById(R.id.hypothesized_value_edit_text);
        mSampleMean = mRootView.findViewById(R.id.mean_of_sample_edit_text);
        mSampleStandardDeviation = mRootView.findViewById(R.id.sample_standard_deviation_edit_text);
        mSampleSize = mRootView.findViewById(R.id.sample_size_edit_text_view);
        mCalculate = mRootView.findViewById(R.id.calculate_t_test_stats_button);
        mOutputView = mRootView.findViewById(R.id.output_text_view);
        mCurrentVariance = Variance.EQUALITY;
        mAlphaOptionalEditText = mRootView.findViewById(R.id.alpha_edit_text);

        validateNMoreThan1();

        variances = mRootView.findViewById(R.id.variances_radio_group);
        variances.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch(checkedID) {
                    case R.id.variances_equality_radio_button:
                        onEqualityTestClicked();
                        break;
                    case R.id.variances_less_than_radio_button:
                        onLessThanTestClicked();
                        break;
                    case R.id.variances_more_than_radio_button:
                        onMoreThanTestClicked();
                        break;
                }
            }
        });

        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    calculate();
                } else {
                    displayError();
                }
            }
        });


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

        mAlphaOptionalEditText.addTextChangedListener(new TextValidator(mAlphaOptionalEditText) {
            @Override
            public void validate(TextView textView, String text) {
                BigDecimal actualProbability;
                if (!".".equals(mAlphaOptionalEditText.getText().toString())
                        && !"".equals(mAlphaOptionalEditText.getText().toString())) {
                    try {
                        actualProbability = new BigDecimal(mAlphaOptionalEditText.getText().toString());

                        if (actualProbability.compareTo(BigDecimal.ONE) > 0) {
                            mAlphaOptionalEditText.setError(getString(R.string.directions_error_on_edit_text_prob_over_100_percent));
                        }
                    } catch (Exception e){
                        mAlphaOptionalEditText.setError(getString(R.string.directions_error_on_edit_text_decimal_format));
                    }
                }
            }
        });





        return mRootView;
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
                            mSampleSize.setError("The number must be above 1");
                        }
                    } catch (Exception e) {
                        mSampleSize.setError("Must be a whole number above 1");
                    }
                }
            }
        });
    }

    private boolean isValid() {
        return !isAnInputEmpty() && !doesEditTextContainError();
    }

    private void displayResult(){
        LinearLayout errorContainer = mRootView.findViewById(R.id.error_container_for_padding);
        errorContainer.setVisibility(View.GONE);

        LinearLayout output= mRootView.findViewById(R.id.output_container_for_padding);
        output.setVisibility(View.VISIBLE);
        scrollToBottom();
    }

    private void displayError() {
        LinearLayout errorContainer = mRootView.findViewById(R.id.error_container_for_padding);
        errorContainer.setVisibility(View.VISIBLE);

        LinearLayout output= mRootView.findViewById(R.id.output_container_for_padding);
        output.setVisibility(View.GONE);
        TextView errorTextView = mRootView.findViewById(R.id.error_text_view);
        errorTextView.setText(getErrorMessage());
        scrollToBottom();
    }

    private String getErrorMessage() {
        String result;

        if(isAnInputEmpty() && doesEditTextContainError()) {
            result = getString(R.string.directions_error_generic_input_empty_and_errors);
        } else if (isAnInputEmpty()) {
            result = getString(R.string.directions_error_generic_empty);
        } else {
            result = (String) getString(R.string.directions_error_generic_input);
        }
        return result;

    }

    private boolean doesEditTextContainError() {
        return (mHypothesisValue.getError() != null ||
                mSampleMean.getError() != null ||
                mSampleStandardDeviation.getError() != null) ||
                mSampleSize.getError() != null;
    }

    private void scrollToBottom(){
        ScrollView scrollView = mRootView.findViewById(R.id.content_scroll_view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private boolean isAnInputEmpty() {
        return ("".equals(mHypothesisValue.getText().toString()) ||
                "".equals(mSampleMean.getText().toString()) ||
                "".equals(mSampleStandardDeviation.getText().toString()) ||
                "".equals(mSampleSize.getText().toString()) ||
                variances.getCheckedRadioButtonId() == -1);
    }

    private void calculate() {
        displayResult();
        if (mCurrentVariance == mCurrentVariance.EQUALITY) {
            calculateEqualityVariance();
        } else if (mCurrentVariance == mCurrentVariance.MORETHAN) {
            calculateMoreThanVariance();
        } else if (mCurrentVariance == mCurrentVariance.LESSTHAN) {
            calculateLessThanVariance();
        }
    }


    private double getAlpha() {
        try {
            return Double.parseDouble(mAlphaOptionalEditText.getText().toString());
        } catch (Exception e) {
            return 0;
        }

    }

    private void calculateEqualityVariance() {
        //calculate equality test
        double hypothesisValue = Double.parseDouble(mHypothesisValue.getText().toString());
        double sampleMean = Double.parseDouble(mSampleMean.getText().toString());
        double sampleStandardDeviation = Double.parseDouble(mSampleStandardDeviation.getText().toString());
        double sampleSize = Double.parseDouble(mSampleSize.getText().toString());
        double answerT = new TTest().getT(sampleMean,hypothesisValue,sampleSize,sampleStandardDeviation);
        double answerP = new TTest().getPTwoTailed(sampleSize-1, answerT);

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
    private void calculateMoreThanVariance() {
        double hypothesisValue = Double.parseDouble(mHypothesisValue.getText().toString());
        double sampleMean = Double.parseDouble(mSampleMean.getText().toString());
        double sampleStandardDeviation = Double.parseDouble(mSampleStandardDeviation.getText().toString());
        double sampleSize = Double.parseDouble(mSampleSize.getText().toString());

        double answerT = new TTest().getT(sampleMean,hypothesisValue,sampleSize,sampleStandardDeviation);
        double answerP = new TTest().getPMoreThan(sampleSize-1, answerT);

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

    private void calculateLessThanVariance() {
        double hypothesisValue = Double.parseDouble(mHypothesisValue.getText().toString());
        double sampleMean = Double.parseDouble(mSampleMean.getText().toString());
        double sampleStandardDeviation = Double.parseDouble(mSampleStandardDeviation.getText().toString());
        double sampleSize = Double.parseDouble(mSampleSize.getText().toString());

        double answerT = new TTest().getT(sampleMean,hypothesisValue,sampleSize,sampleStandardDeviation);
        double answerP = new TTest().getPLessThan(sampleSize-1, answerT);

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

    private String alphaAndPValueAnalysis(double alpha, double pValue) {
        if ("".equals(mAlphaOptionalEditText.getText().toString())) {
            return "You can fill out alpha text box for more analysis.";
        } else {
            if (mAlphaOptionalEditText.getError() != null){
                return "Fix alpha text box for more analysis.";
            }
        }
        String warning = "";
        if (alpha > .1) {
            warning = "WARNING: An alpha higher then 10 is not very common. Please make sure alpha is correct.";
        }

        if (pValue <= alpha) {
            return "Reject null hypothesis and result is statistically significant. " + warning;
        } else {
            return "We Fail to reject the null hypothesis. " + warning;
        }
    }

    private void onEqualityTestClicked() {
        mCurrentVariance = Variance.EQUALITY;
    }

    private void onLessThanTestClicked() {
        mCurrentVariance = Variance.LESSTHAN;
    }

    private void onMoreThanTestClicked() {
        mCurrentVariance = Variance.MORETHAN;
    }




}
