package app.goStat.view.functions.functionFragments;

import android.support.v4.app.Fragment;
import android.widget.EditText;

public class TwoSampleZTestStatsFragment extends TestStatisticsFragment {




    public static TwoSampleZTestStatsFragment newInstance() {
        return new TwoSampleZTestStatsFragment();
    }


    @Override
    protected boolean isAnInputEmpty() {
        return false;
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
    protected EditText getAlphaEditText() {
        return null;
    }
}
