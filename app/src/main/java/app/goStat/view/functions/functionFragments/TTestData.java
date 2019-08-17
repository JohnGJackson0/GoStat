package app.goStat.view.functions.functionFragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import app.goStat.R;
import app.goStat.model.DataPoint;
import app.goStat.model.StatisticalList;
import app.goStat.util.TTestUtil;
import app.goStat.util.android.TextValidator;

public class TTestData extends Fragment {

    private TTestDataViewModel mViewModel;
    private View mRootView;

    private EditText mHypothesisMean;
    private Spinner mSelectListSpinner;
    private LinearLayout mInputLayout;
    private LinearLayout mOutputLayout;
    private LinearLayout mErrorLayout;
    private RadioGroup mVariances;
    private Button mCalculate;
    private EditText mAlphaOptionalEditText;

    private String mAnswer;
    private String mOutput;

    private Enum mCurrentVariance;

    private List<Integer> mCurrentStatIdInOrder;

    private enum Variance {
        EQUALITY,
        MORETHAN,
        LESSTHAN,
        ;
    }

    public static TTestData newInstance() {
        return new TTestData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(TTestDataViewModel.class);
        mRootView = inflater.inflate(R.layout.ttest_data_fragment, container, false);
        mCurrentVariance = Variance.EQUALITY;
        mSelectListSpinner = mRootView.findViewById(R.id.select_list_spinner);
        mInputLayout = mRootView.findViewById(R.id.input_container_for_padding);
        mOutputLayout = mRootView.findViewById(R.id.output_container_for_padding);
        mErrorLayout = mRootView.findViewById(R.id.error_container_for_padding);
        mCurrentStatIdInOrder = new ArrayList<>();
        mHypothesisMean = mRootView.findViewById(R.id.hypothesized_value_edit_text);
        mAlphaOptionalEditText = mRootView.findViewById(R.id.alpha_edit_text);

        setSpinner();

        mVariances = mRootView.findViewById(R.id.variances_radio_group);
        mVariances.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        mCalculate = mRootView.findViewById(R.id.calculate_t_test_data_button);

        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    calculate();
                } else {
                    displayErrors();
                }
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

    private boolean isValid(){
        return !isAnInputEmpty();
    }

    private boolean isAnInputEmpty() {
        return ("".equals(mHypothesisMean.getText().toString()) ||
                mVariances.getCheckedRadioButtonId() == -1);
    }

    public void displayErrors() {
        LinearLayout errorContainer = mRootView.findViewById(R.id.error_container_for_padding);
        errorContainer.setVisibility(View.VISIBLE);

        LinearLayout output= mRootView.findViewById(R.id.output_container_for_padding);
        output.setVisibility(View.GONE);
        TextView errorTextView = mRootView.findViewById(R.id.error_text_view);
        errorTextView.setText(getString(R.string.directions_error_generic_empty));
        scrollToBottom();
    }

    private void calculate() {
        submitOutput();
    }

    private double getAlpha() {
        try {
            return Double.parseDouble(mAlphaOptionalEditText.getText().toString());
        } catch (Exception e) {
            return 0;
        }

    }

    private void submitOutput() {
        mViewModel.getDataPoints(getListID()).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {

                if(dataPoints.size() < 2) {
                    showError(getString(R.string.directions_error_generic_more_than_one_data_entry));
                } else {
                    double[] sample = new double[dataPoints.size()];

                    for(int i =0; i < dataPoints.size();i++) {
                        sample[i] = dataPoints.get(i).getValue().doubleValue();
                    }

                    if(isAnInputEmpty()){
                        showError(getString(R.string.directions_fill_out_required_inputs));
                    } else {
                        double tValue = new TTestUtil().getT(sample,Double.parseDouble(mHypothesisMean.getText().toString()));

                        double pValue = getPWithVariance(sample,Double.parseDouble(mHypothesisMean.getText().toString()));
                        mAnswer = "t = " + tValue + "\np = " + pValue;
                        mOutput = mAnswer + "\n\n"+
                                "Input:" + "\n" +
                                "Hypothesis Value µ0 = " + mHypothesisMean.getText().toString() + "\n" +
                                "Variance Type µ = " + "two tailed" + "\n\n" +
                                alphaAndPValueAnalysis(getAlpha(), pValue);

                        showOutput(mOutput);
                    }
                }
            }
        });
    }

    private String alphaAndPValueAnalysis(double alpha, double pValue) {
        if ("".equals(mAlphaOptionalEditText.getText().toString())) {
            return getString(R.string.directions_alpha_text_box_for_more_analysis);
        } else {
            if (mAlphaOptionalEditText.getError() != null){
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

    private double getPWithVariance(double[] sample, double hypothesisMean) {
        if(mCurrentVariance == Variance.EQUALITY) {
            return new TTestUtil().getPTwoTailed(sample,hypothesisMean);
        } else if (mCurrentVariance == Variance.LESSTHAN) {
            return new TTestUtil().getPLessThan(sample,hypothesisMean);
        } else {
            return new TTestUtil().getPMoreThan(sample,hypothesisMean);
        }
    }

    private int getListID() {
        if(mCurrentStatIdInOrder.size() >= mSelectListSpinner.getSelectedItemPosition()) {
            return mCurrentStatIdInOrder.get(mSelectListSpinner.getSelectedItemPosition());
        }
        return 0;
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


    private void setSpinner() {
        List<String> statNames = new ArrayList<>();

        mViewModel.getAllEditableLists().observe(this, new Observer<List<StatisticalList>>() {
            @Override
            public void onChanged(@Nullable List<StatisticalList> statisticalList) {
                if (statisticalList.size() == 0){
                    showOnlyError(getString(R.string.directions_error_create_a_list_for_calculation));
                }

                for (StatisticalList item : statisticalList) {
                    if (!item.isFrequencyTable()) {
                        mCurrentStatIdInOrder.add(item.getId());
                        statNames.add(item.getName() + " id = " + item.getId());
                    }
                }
                loadListsIntoSpinner(statNames);
            }
        });
    }

    private void showError(String error) {
        mInputLayout.setVisibility(View.VISIBLE);
        mOutputLayout.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
        scrollToBottom();

        TextView errorView = mRootView.findViewById(R.id.error_text_view);
        errorView.setText(error);
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

    private void showOnlyError(String error) {
        mInputLayout.setVisibility(View.GONE);
        mOutputLayout.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);

        TextView errorView = mRootView.findViewById(R.id.error_text_view);
        errorView.setText(error);
    }

    private void showOutput(String output) {
        mInputLayout.setVisibility(View.VISIBLE);
        mOutputLayout.setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.GONE);

        TextView outputView = mRootView.findViewById(R.id.output_text_view);
        outputView.setText(output);
        scrollToBottom();
    }

    private void loadListsIntoSpinner(List statNames) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, statNames) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                return view;
            }
        };
        mSelectListSpinner.setAdapter(spinnerArrayAdapter);
    }



}
