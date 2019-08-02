package app.goStat.view.functions.functionFragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.math3.distribution.BinomialDistribution;
import java.math.BigDecimal;
import java.math.RoundingMode;
import app.goStat.R;
import app.goStat.util.android.TextValidator;


/*

x: The number of successes that result from the binomial experiment.
n: The number of trials in the binomial experiment.
P: The probability of success on an individual trial.
Q: The probability of failure on an individual trial. (This is equal to 1 - P.)
n!: The factorial of n (also known as n factorial).
b(x; n, P): Binomial probability - the probability that an n-trial binomial experiment results in exactly x successes, when the probability of success on an individual trial is P.
nCr: The number of combinations of n things, taken r at a time.
 */


public class BinomialFragment extends Fragment{
    private View mRootView;
    private Range mCurrentRange;

    private EditText mTrialsEditText;
    private EditText mProbabilityOfSuccessEditText;
    private EditText mNumberOfSuccessesFromTrialsEditText;

    private RadioGroup mRangeRadioGroup;

    private String mOutput;
    private String mAnswer;

    private enum Range {
        MAX,
        EXACT,
        ;
    }

    public BinomialFragment() {
        // Required empty public constructor
    }

    public static BinomialFragment newInstance() {
        BinomialFragment fragment = new BinomialFragment();
        Bundle args = new Bundle();
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
        mRootView =  inflater.inflate(R.layout.binomial_fragment, container, false);

        mTrialsEditText = mRootView.findViewById(R.id.number_of_trials_edit_text);
        mProbabilityOfSuccessEditText = mRootView.findViewById(R.id.probability_of_success_edit_text);
        mNumberOfSuccessesFromTrialsEditText = mRootView.findViewById(R.id.number_of_successes_edit_text);

        onExactClicked();

        Button calc =  mRootView.findViewById(R.id.calculate_binomial_button);

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
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

        //validation for percentage from user

        mProbabilityOfSuccessEditText.addTextChangedListener(new TextValidator(mProbabilityOfSuccessEditText) {
            @Override
            public void validate(TextView textView, String text) {
                BigDecimal actualProbability;
                if (!".".equals(mProbabilityOfSuccessEditText.getText().toString())
                        && !"".equals(mProbabilityOfSuccessEditText.getText().toString())) {
                    try {
                        actualProbability = new BigDecimal(mProbabilityOfSuccessEditText.getText().toString());

                        if (actualProbability.compareTo(BigDecimal.ONE) > 0) {
                            mProbabilityOfSuccessEditText.setError(getString(R.string.directions_error_on_edit_text_binomial_prob_success_over_100_percent));
                        }
                    } catch (Exception e){
                        mProbabilityOfSuccessEditText.setError(getString(R.string.directions_error_on_edit_text_binomial_decimal_format));
                    }
                }
            }
        });

        //the number of successes must be less than or equal to the number of trials

        mNumberOfSuccessesFromTrialsEditText.addTextChangedListener(new TextValidator(mNumberOfSuccessesFromTrialsEditText) {
            @Override
            public void validate(TextView textView, String text) {
                validateNumberOfSuccessesLessThanNumberOfTrials();
            }
        });
        mTrialsEditText.addTextChangedListener(new TextValidator(mTrialsEditText) {
            @Override
            public void validate(TextView textView, String text) {
                validateNumberOfSuccessesLessThanNumberOfTrials();
            }
        });

        mRangeRadioGroup = mRootView.findViewById(R.id.probability_range_max_exact_ratio_group);

        mRangeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch(checkedID) {
                    case R.id.probability_max_radio_button:
                        onMaxClicked();
                        break;
                    case R.id.probability_exact_radio_button:
                        onExactClicked();
                        break;
                }
            }
        });

        return mRootView;
    }

    private void validateNumberOfSuccessesLessThanNumberOfTrials() {
        if (isNumberOfSuccessesLessThanNumberOfTrials()) {
            mNumberOfSuccessesFromTrialsEditText.setError(getString(R.string.directions_error_on_edit_text_binomial_successes_less_than_trials));
            mTrialsEditText.setError(getString(R.string.directions_error_on_edit_text_binomial_successes_less_than_trials));
        } else {
            mNumberOfSuccessesFromTrialsEditText.setError(null);
            mTrialsEditText.setError(null);
        }
    }

    private boolean isNumberOfSuccessesLessThanNumberOfTrials() {
        if ("".equals(mNumberOfSuccessesFromTrialsEditText.getText().toString())
                || "".equals(mTrialsEditText.getText().toString()) ) {
            return false;
        }
        return Integer.parseInt(mNumberOfSuccessesFromTrialsEditText.getText().toString())
                > Integer.parseInt(mTrialsEditText.getText().toString());
    }

    private void copyAnswerToClipboard(){
        copyToClipboard(mAnswer);
        showCopyToClipboardMessage();
    }

    private void copyAllTextToClipboard(){
        copyToClipboard(mOutput);
        showCopyToClipboardMessage();
    }

    public void onMaxClicked(){
        mCurrentRange = Range.MAX;
    }

    public void onExactClicked(){
        mCurrentRange = Range.EXACT;
    }

    private boolean isAnInputEmpty() {
        return ("".equals(mNumberOfSuccessesFromTrialsEditText.getText().toString()) ||
                "".equals(mTrialsEditText.getText().toString()) ||
                "".equals(mProbabilityOfSuccessEditText.getText().toString()) ||
                mRangeRadioGroup.getCheckedRadioButtonId() == -1);
    }

    private boolean doesEditTextContainError() {
        return (mNumberOfSuccessesFromTrialsEditText.getError() != null ||
                mTrialsEditText.getError() != null ||
                mProbabilityOfSuccessEditText.getError() != null);
    }

    private boolean validate(){
        return !isAnInputEmpty() && !doesEditTextContainError();
    }

    private void displayError() {
        LinearLayout outputContainer = mRootView.findViewById(R.id.output_container_for_padding);
        outputContainer.setVisibility(View.GONE);
        LinearLayout errorContainer = mRootView.findViewById(R.id.error_container_for_padding);
        errorContainer.setVisibility(View.VISIBLE);
        TextView errorTextView = mRootView.findViewById(R.id.error_text_view);
        errorTextView.setText(getErrorMessage());
        scrollToBottom();
    }

    private String getErrorMessage() {
        String errorMessage;

        if(isAnInputEmpty() && doesEditTextContainError()) {
            errorMessage = getString(R.string.directions_error_binomial_input_empty_and_errors);
        } else if (isAnInputEmpty()) {
            errorMessage = getString(R.string.directions_error_binomial_empty);
        } else {
            errorMessage = getString(R.string.directions_error_binomial_errors);
        }

        return errorMessage;
    }

    private void displayResult() {
        LinearLayout outputContainer = mRootView.findViewById(R.id.output_container_for_padding);
        outputContainer.setVisibility(View.VISIBLE);

        LinearLayout errorContainer = mRootView.findViewById(R.id.error_container_for_padding);
        errorContainer.setVisibility(View.GONE);
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

    //method called after already validated
    private void calculate(){
        displayResult();

        TextView output = mRootView.findViewById(R.id.output_text_view);

        int trials = Integer.parseInt(mTrialsEditText.getText().toString());
        BigDecimal probabilityOfSuccess = new BigDecimal(mProbabilityOfSuccessEditText.getText().toString());
        int numberOfSuccesses = Integer.parseInt(mNumberOfSuccessesFromTrialsEditText.getText().toString());

        BinomialDistribution binomial;
        binomial = new BinomialDistribution(trials,probabilityOfSuccess.doubleValue());

        if (mCurrentRange == Range.EXACT) {
            closeKeyboard();
            double realAnswer =binomial.cumulativeProbability(numberOfSuccesses-1, numberOfSuccesses);
            double percentageAnswer = round(realAnswer*100,2);
            String probabilityOfSuccessRoundedPercent = probabilityOfSuccess.movePointRight(2).toString();
            mAnswer = Double.toString(realAnswer);

            mOutput =  mAnswer + "\n\n"
                    + "With a success rate of " + probabilityOfSuccessRoundedPercent
                    + " percent and " +  Integer.toString(trials) + " chances, there is a probability of "
                    + Double.toString(percentageAnswer) + " percent that success will happen exactly "
                    + Integer.toString(numberOfSuccesses) + " times.";
                    output.setText(mOutput);
            scrollToBottom();
        } else if (mCurrentRange == Range.MAX){
            closeKeyboard();
            double realAnswer = binomial.cumulativeProbability(numberOfSuccesses);
            double percentageAnswer = round(realAnswer*100,2);
            String probabilityOfSuccessRoundedPercent = probabilityOfSuccess.movePointRight(2).toString();
            mAnswer = Double.toString(realAnswer);

            mOutput = "Result: " +  mAnswer + "\n\n"
                    + "With a success rate of " + probabilityOfSuccessRoundedPercent
                    + " percent and " +  Integer.toString(trials) + " chances, there is a probability of "
                    + Double.toString(percentageAnswer) + " percent that success will happen less than or equal to "
                    + Integer.toString(numberOfSuccesses) + " times.";
            output.setText(mOutput);
            scrollToBottom();
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    private void copyToClipboard(String copyText) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.meta_label_copy_to_clipboard_result), copyText);
        clipboard.setPrimaryClip(clip);
    }

    private void showCopyToClipboardMessage() {
        Toast toast = Toast.makeText(getActivity(),
                getResources().getString(R.string.toast_copy_to_clipboard_generic),
                Toast.LENGTH_SHORT);
        View view = toast.getView();
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //access to getActivity()

    }
}
