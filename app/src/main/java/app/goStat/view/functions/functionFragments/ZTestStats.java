package app.goStat.view.functions.functionFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import app.goStat.R;
import app.goStat.util.TTestUtil;
import app.goStat.util.ZTestUtil;
import app.goStat.util.android.ClipboardUtil;


public class ZTestStats extends StatisticsTestFragments {

    private View mRootView;
    private EditText mHypothesisValue;
    private EditText mSampleMean;
    private EditText mStandardDeviation;
    private EditText mSampleSize;
    private RadioGroup mVariances;
    private TextView mOutputView;
    private String mAnswer;
    private String mOutput;

    public ZTestStats() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflateFragment(R.layout.fragment_z_test_stats,inflater,container);
        mHypothesisValue = mRootView.findViewById(R.id.hypothesized_value_edit_text);
        mSampleMean = mRootView.findViewById(R.id.mean_of_sample_edit_text);
        mStandardDeviation = mRootView.findViewById(R.id.standard_deviation_edit_text);
        mSampleSize = mRootView.findViewById(R.id.sample_size_edit_text_view);
        mOutputView = mRootView.findViewById(R.id.output_text_view);

        mVariances = mRootView.findViewById(R.id.variances_radio_group);

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

    protected void calculateEqualityVariance() {
        double hypothesisValue = Double.parseDouble(mHypothesisValue.getText().toString());
        double sampleMean = Double.parseDouble(mSampleMean.getText().toString());
        double standardDeviation = Double.parseDouble(mStandardDeviation.getText().toString());
        double sampleSize = Double.parseDouble(mSampleSize.getText().toString());
        double answerZ = new ZTestUtil().getZ(sampleMean,hypothesisValue,standardDeviation,sampleSize);
        double answerP = new ZTestUtil().getPTwoTailed(answerZ);

        String realAnswerZ = Double.toString(answerZ);
        String realAnswerP = Double.toString(answerP);

        mAnswer = "z value = " + realAnswerZ + "\n" + "p value = " + realAnswerP;


        mOutput =  mAnswer + "\n\n"+
                "Input:" + "\n" +
                "Hypothesis Value µ0 = " + mHypothesisValue.getText().toString() + "\n" +
                "Sample Mean x̅ =" + mSampleMean.getText().toString() + "\n" +
                "Sample Standard Deviation Sx = " + mStandardDeviation.getText().toString() + "\n" +
                "Sample Size n = " + mSampleSize.getText().toString() + "\n" +
                "Variance Type µ = " + "two tailed" + "\n\n" +
                alphaAndPValueAnalysis(getAlpha(), answerP);

        mOutputView.setText(mOutput);
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

    protected void calculateMoreThanVariance() {
        double hypothesisValue = Double.parseDouble(mHypothesisValue.getText().toString());
        double sampleMean = Double.parseDouble(mSampleMean.getText().toString());
        double standardDeviation = Double.parseDouble(mStandardDeviation.getText().toString());
        double sampleSize = Double.parseDouble(mSampleSize.getText().toString());
        double answerZ = new ZTestUtil().getZ(sampleMean,hypothesisValue,standardDeviation,sampleSize);
        double answerP = new ZTestUtil().getPMoreThan(answerZ);

        String realAnswerZ = Double.toString(answerZ);
        String realAnswerP = Double.toString(answerP);

        mAnswer = "z value = " + realAnswerZ + "\n" + "p value = " + realAnswerP;
        mOutput =  mAnswer + "\n\n"+
                "Input:" + "\n" +
                "Hypothesis Value µ0 = " + mHypothesisValue.getText().toString() + "\n" +
                "Sample Mean x̅ =" + mSampleMean.getText().toString() + "\n" +
                "Standard Deviation σ = " + mStandardDeviation.getText().toString() + "\n" +
                "Sample Size n = " + mSampleSize.getText().toString() + "\n" +
                "Variance Type µ = more than" + "\n\n" +
                alphaAndPValueAnalysis(getAlpha(), answerP);

        mOutputView.setText(mOutput);
    }

    protected void calculateLessThanVariance() {

        double hypothesisValue = Double.parseDouble(mHypothesisValue.getText().toString());
        double sampleMean = Double.parseDouble(mSampleMean.getText().toString());
        double standardDeviation = Double.parseDouble(mStandardDeviation.getText().toString());
        double sampleSize = Double.parseDouble(mSampleSize.getText().toString());
        double answerZ = new ZTestUtil().getZ(sampleMean,hypothesisValue,standardDeviation,sampleSize);
        double answerP = new ZTestUtil().getPLessThan(answerZ);

        String realAnswerZ = Double.toString(answerZ);
        String realAnswerP = Double.toString(answerP);

        mAnswer = "z value = " + realAnswerZ + "\n" + "p value = " + realAnswerP;


        mOutput =  mAnswer + "\n\n"+
                "Input:" + "\n" +
                "Hypothesis Value µ0 = " + mHypothesisValue.getText().toString() + "\n" +
                "Sample Mean x̅ =" + mSampleMean.getText().toString() + "\n" +
                "Standard Deviation σ = " + mStandardDeviation.getText().toString() + "\n" +
                "Sample Size n = " + mSampleSize.getText().toString() + "\n" +
                "Variance Type µ = " + "less than" + "\n\n" +
                alphaAndPValueAnalysis(getAlpha(), answerP);

        mOutputView.setText(mOutput);
    }

    @Override
    protected EditText getAlphaEditText() {
        return mRootView.findViewById(R.id.alpha_edit_text);
    }


    protected boolean isAnInputEmpty() {
        return "".equals(mHypothesisValue.getText().toString()) ||
                "".equals(mSampleMean.getText().toString()) ||
                "".equals(mStandardDeviation.getText().toString()) ||
                "".equals(mSampleSize.getText().toString()) ||
                mVariances.getCheckedRadioButtonId() == -1;
    }

    protected boolean doesEditTextContainError() {
        return mHypothesisValue.getError() != null ||
                mSampleMean.getError() != null ||
                mStandardDeviation.getError() != null ||
                mSampleSize.getError() != null;
    }

    public static ZTestStats newInstance() {
        ZTestStats fragment = new ZTestStats();
        return  fragment;
    }

}
