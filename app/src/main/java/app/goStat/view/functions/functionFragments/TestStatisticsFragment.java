package app.goStat.view.functions.functionFragments;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import java.math.BigDecimal;
import app.goStat.R;
import app.goStat.util.android.TextValidator;

public abstract class TestStatisticsFragment extends Fragment {
    ScrollView mLayout;
    Variance mCurrentVariance;

    protected enum Variance {
        EQUALITY,
        MORETHAN,
        LESSTHAN,
        ;
    }

    protected View inflateFragment(int resId, LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(resId, container, false);
        mLayout = view.findViewById(R.id.content_scroll_view);
        mCurrentVariance = Variance.EQUALITY;
        createVariancesRadioGroupListener();
        createAlphaValidationListener();
        setHasOptionsMenu(true);
        return view;
    }

    protected void createVariancesRadioGroupListener () {
        RadioGroup variances = mLayout.findViewById(R.id.variances_radio_group);
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
    }

    protected void createAlphaValidationListener() {
        EditText alphaEditText = mLayout.findViewById(R.id.alpha_edit_text);

        alphaEditText.addTextChangedListener(new TextValidator(alphaEditText) {
            @Override
            public void validate(TextView textView, String text) {
                BigDecimal actualProbability;
                if (!".".equals(alphaEditText.getText().toString())
                        && !"".equals(alphaEditText.getText().toString())) {
                    try {
                        actualProbability = new BigDecimal(alphaEditText.getText().toString());

                        if (actualProbability.compareTo(BigDecimal.ONE) > 0) {
                            alphaEditText.setError(getString(R.string.directions_error_on_edit_text_prob_over_100_percent));
                        }
                    } catch (Exception e){
                        alphaEditText.setError(getString(R.string.directions_error_on_edit_text_decimal_format));
                    }
                }
            }
        });
    }

    protected double getAlpha() {
        try {
            return Double.parseDouble(getAlphaEditText().getText().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    protected String alphaAndPValueAnalysis(double alpha, double pValue) {
        if ("".equals(getAlphaEditText().getText().toString())) {
            return getString(R.string.directions_alpha_text_box_for_more_analysis);
        } else {
            if (getAlphaEditText().getError() != null){
                return getString(R.string.directions_generic_fix_alpha_text_box_for_more_analysis);
            }
        }
        String warning = "";
        if (alpha > .1) {
            warning = getString(R.string.directions_warning_high_alpha);
        }

        if (pValue <= alpha) {
            return getString(R.string.text_reject_null_analysis) + warning;
        } else {
            return getString(R.string.text_fail_to_reject_null_analysis) + warning;
        }
    }


    protected void onCalculatedClicked() {
        if (isValid()) {
            calculate();
        } else {
            displayError();
        }
    }

    private void scrollToBottom(){
        ScrollView scrollView = mLayout.findViewById(R.id.content_scroll_view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    protected void displayResult(){
        View errorContainer = mLayout.findViewById(R.id.generic_error_layout);
        errorContainer.setVisibility(View.GONE);

        View output= mLayout.findViewById(R.id.generic_output_layout);
        output.setVisibility(View.VISIBLE);
        scrollToBottom();
    }

    protected void displayError() {
        View errorContainer = mLayout.findViewById(R.id.generic_error_layout);
        errorContainer.setVisibility(View.VISIBLE);

        View output= mLayout.findViewById(R.id.generic_output_layout);
        output.setVisibility(View.GONE);
        TextView errorTextView = mLayout.findViewById(R.id.error_text_view);
        errorTextView.setText(getErrorMessage());
        scrollToBottom();
    }

    private void customError(String error) {
        View errorContainer = mLayout.findViewById(R.id.generic_error_layout);
        errorContainer.setVisibility(View.VISIBLE);

        View output= mLayout.findViewById(R.id.generic_output_layout);
        output.setVisibility(View.GONE);
        TextView errorTextView = mLayout.findViewById(R.id.error_text_view);
        errorTextView.setText(error);
        scrollToBottom();
    }

    private String getErrorMessage() {
        String result;

        if(isAnInputEmpty() && doesEditTextContainError()) {
            result = getString(R.string.directions_error_generic_input_empty_and_errors);
        } else {
            result = getString(R.string.directions_error_generic_input);
        }
        return result;
    }

    protected void setError(String error){
        customError(error);
    }

    protected void calculate() {
        displayResult();
        if (mCurrentVariance == mCurrentVariance.EQUALITY) {
            calculateEqualityVariance();
        } else if (mCurrentVariance == mCurrentVariance.MORETHAN) {
            calculateMoreThanVariance();
        } else if (mCurrentVariance == mCurrentVariance.LESSTHAN) {
            calculateLessThanVariance();
        }
    }

    protected boolean isValid() {
        return !isAnInputEmpty() && !doesEditTextContainError();
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

    protected abstract boolean isAnInputEmpty();
    protected abstract boolean doesEditTextContainError();
    protected abstract void calculateEqualityVariance();
    protected abstract void calculateMoreThanVariance();
    protected abstract void calculateLessThanVariance();

    //we are not getting alpha from a generic template so instead of
    //making sure the ID is always the same, the class is asking for the
    //edit text instead to provide some safety if ID changes in a layout.
    protected abstract EditText getAlphaEditText();
}
