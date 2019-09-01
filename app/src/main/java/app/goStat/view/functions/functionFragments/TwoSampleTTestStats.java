package app.goStat.view.functions.functionFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import app.goStat.R;
import app.goStat.util.StatisticsTests.TTestUtil;
import app.goStat.util.android.ClipboardUtil;

public class TwoSampleTTestStats extends TestStatisticsFragment {
    View mRootView;

    EditText mAlphaOptionalEditText;
    EditText mListOneSampleMean;
    EditText mListOneStandardDeviation;
    EditText mListOneSampleSize;
    EditText mListTwoSampleMean;
    EditText mListTwoStandardDeviation;
    EditText mListTwoSampleSize;
    RadioGroup mPooledRadioGroup;
    TextView mOutputView;
    Button mCalculate;
    private boolean isPooled;

    private String mAnswer = "";
    private String mOutput = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflateFragment(R.layout.fragment_two_sample_t_test_stats,inflater,container);
        mAlphaOptionalEditText = mRootView.findViewById(R.id.alpha_edit_text);

        mListOneSampleMean = mRootView.findViewById(R.id.list_one_sample_mean_edit_text);
        mListOneStandardDeviation = mRootView.findViewById(R.id.list_one_standard_deviation_edit_text);
        mListOneSampleSize = mRootView.findViewById(R.id.list_one_sample_size_edit_text);
        mListTwoSampleMean = mRootView.findViewById(R.id.list_two_sample_mean_edit_text);
        mListTwoStandardDeviation = mRootView.findViewById(R.id.list_two_standard_deviation_edit_text);
        mListTwoSampleSize = mRootView.findViewById(R.id.list_two_sample_size_edit_text);
        mPooledRadioGroup = mRootView.findViewById(R.id.pooled_radio_group);
        mCalculate = mRootView.findViewById(R.id.calculate_button);
        mOutputView = mRootView.findViewById(R.id.output_text_view);

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

        isPooled = false; //default
        createPooledRadioGroupListener();


        return mRootView;
    }

    protected void createPooledRadioGroupListener () {
        RadioGroup variances = mLayout.findViewById(R.id.pooled_radio_group);
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

    public static TwoSampleTTestStats newInstance() {
        return new TwoSampleTTestStats();
    }

    @Override
    protected boolean isAnInputEmpty() {
        return "".equals(mListOneSampleMean.getText().toString()) ||
                "".equals(mListOneStandardDeviation.getText().toString()) ||
                "".equals(mListOneSampleSize.getText().toString()) ||
                "".equals(mListTwoSampleMean.getText().toString()) ||
                "".equals(mListTwoStandardDeviation.getText().toString()) ||
                "".equals(mListTwoSampleSize.getText().toString());
    }

    @Override
    protected boolean doesEditTextContainError() {
        return mListOneSampleMean.getError() != null ||
                mListOneStandardDeviation.getError() != null ||
                mListOneSampleSize.getError() != null ||
                mListTwoSampleMean.getError() != null ||
                mListTwoStandardDeviation.getError() != null ||
                mListTwoSampleSize.getError() != null;
    }

    @Override
    protected void calculateEqualityVariance() {
        TDistributionTest distributionTest;

        if (isPooled) {
            distributionTest = new PooledTwoTailedDistributionTest(getData());
        } else{
            distributionTest = new NotPooledTwoTailedDistributionTest(getData());
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
            distributionTest = new PooledMoreThanDistributionTest(getData());
        } else{
            distributionTest = new NotPooledMoreThanDistributionTest(getData());
        }
        mOutputView.setText(distributionTest.toString());
        mAnswer = "t = " + distributionTest.getT() + " \n"
                + "p = " + distributionTest.getP();
    }

    @Override
    protected void calculateLessThanVariance() {
        TDistributionTest distributionTest;

        if (isPooled) {
            distributionTest = new PooledLessThanDistributionTest(getData());
        } else{
            distributionTest = new NotPooledLessThanDistributionTest(getData());
        }
        mOutputView.setText(distributionTest.toString());
        mAnswer = "t = " + distributionTest.getT() + " \n"
                + "p = " + distributionTest.getP();
    }

    @Override
    protected EditText getAlphaEditText() {
        return mAlphaOptionalEditText;
    }

    private TTestData getData() {
        return new TTestData(Double.parseDouble(mListOneSampleSize.getText().toString()),
                Double.parseDouble(mListOneSampleMean.getText().toString()),
                Double.parseDouble(mListOneStandardDeviation.getText().toString()),
                Double.parseDouble(mListTwoSampleSize.getText().toString()),
                Double.parseDouble(mListTwoSampleMean.getText().toString()),
                Double.parseDouble(mListTwoStandardDeviation.getText().toString()));
    }

    private class TTestData {
        protected double n1;
        protected double xBar1;
        protected double sX1;
        protected double n2;
        protected double xBar2;
        protected double sX2;

        TTestData(double n1,double xBar1, double sX1, double n2, double xBar2,double sX2){
            this.n1 = n1;
            this.sX1 =sX1;
            this.xBar1=xBar1;
            this.n2 = n2;
            this.sX2 = sX2;
            this.xBar2 = xBar2;
        }

        @Override
        public String toString() {
            return "n1: " + n1 + "\n"
                    + "sX1: " + sX1 + "\n"
                    + "x̄1: " + xBar1 + "\n"
                    + "n2: " + n2 + "\n"
                    + "sX2: " + sX2 + "\n"
                    + "x̄2: " + xBar2 + "\n";
        }
    }

    private interface TDistributionTest{
        double getT();
        double getP();
    }

    private abstract class PooledVarianceDistributionTest implements TDistributionTest{
        TTestData data;

        PooledVarianceDistributionTest(TTestData data) {
            this.data = data;
        }

        public double getT() {
            return new TTestUtil().getTOfPooledTwoSampleTTest(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
        }

        @Override
        public String toString() {
            return "Output: \n\n" +
                    "t = " + getT() + "\n" +
                    "p = " + getP() + "\n\n" +
                    "Input: \n" +
                    data.toString() + "\n" +
                    "pooled = yes";
        }

        public abstract double getP();
    }

    private class PooledMoreThanDistributionTest extends PooledVarianceDistributionTest {
        PooledMoreThanDistributionTest(TTestData data){
            super(data);
        }

        @Override
        public double getP() {
            return new TTestUtil().getPOfPooledTwoSampleTTestMoreThan(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
        }
        @Override
        public String toString() {
            return super.toString() +
                    "variance type: more than";
        }
    }

    private class PooledLessThanDistributionTest extends PooledVarianceDistributionTest {
        PooledLessThanDistributionTest(TTestData data){
            super(data);
        }

        @Override
        public double getP() {
            return new TTestUtil().getPOfPooledTwoSampleTTestLessThan(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
        }
        @Override
        public String toString() {
            return super.toString() +
                    "variance type: less than";
        }
    }

    private class PooledTwoTailedDistributionTest extends PooledVarianceDistributionTest {
        PooledTwoTailedDistributionTest(TTestData data){
            super(data);
        }

        @Override
        public double getP() {
            return new TTestUtil().getPOfPooledTwoSampleTTestTwoTailed(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
        }

        @Override
        public String toString() {
            return super.toString() +
                    "variance type: two tailed";
        }
    }

    private abstract class NotPooledVarianceDistributionTest implements TDistributionTest{
        TTestData data;

        NotPooledVarianceDistributionTest(TTestData data) {
            this.data = data;
        }
        public double getT() {
            return new TTestUtil().getTOfNotPooledTwoSampleTTest(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
        }
        public abstract double getP();
        @Override
        public String toString() {
            return "Output: \n\n" +
                    "t = " + getT() + "\n" +
                    "p = " + getP() + "\n\n" +
                    "Input: \n\n" +
                    data.toString() + "\n" +
                    "pooled = no";
        }
    }


    private class NotPooledMoreThanDistributionTest extends NotPooledVarianceDistributionTest {
        NotPooledMoreThanDistributionTest(TTestData data){
            super(data);
        }

        @Override
        public double getP() {
            return new TTestUtil().getPOfNotPooledTwoSampleTTestMoreThan(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
        }
        @Override
        public String toString() {
            return super.toString() +
                    "variance type: more than";
        }
    }

    private class NotPooledLessThanDistributionTest extends NotPooledVarianceDistributionTest {
        NotPooledLessThanDistributionTest(TTestData data){
            super(data);
        }

        @Override
        public double getP() {
            return new TTestUtil().getPOfNotPooledTwoSampleTTestLessThan(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
        }
        @Override
        public String toString() {
            return super.toString() +
                    "variance type: less than";
        }
    }

    private class NotPooledTwoTailedDistributionTest extends NotPooledVarianceDistributionTest {
        NotPooledTwoTailedDistributionTest(TTestData data){
            super(data);
        }

        @Override
        public double getP() {
            return new TTestUtil().getPOfNotPooledTwoSampleTTestTwoTailed(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
        }
        @Override
        public String toString() {
            return super.toString() +
                    "variance type: two tailed";
        }
    }
}
