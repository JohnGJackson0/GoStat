package app.goStat.view.functions.functionFragments.TestsData;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import app.goStat.R;

public class ListsLoader {
    Activity mActivity;
    Spinner mSpinner;
    ViewModel mViewModel;

    public ListsLoader(Activity activity, Spinner spinner) {
        mActivity = activity;
        mSpinner = spinner;
        mViewModel = ViewModelProviders.of((FragmentActivity) activity).get(TTestDataViewModel.class);
    }


    public void loadListsIntoSpinner(List statNames) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mActivity,android.R.layout.simple_spinner_dropdown_item, statNames) {
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
        mSpinner.setAdapter(spinnerArrayAdapter);
    }
}
