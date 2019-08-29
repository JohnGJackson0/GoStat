package app.goStat.view.functions;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import app.goStat.R;
import app.goStat.view.functions.functionFragments.BinomialFragment;
import app.goStat.view.functions.functionFragments.InstructionsFragment;
import app.goStat.view.functions.functionFragments.TTestStatisticsData;
import app.goStat.view.functions.functionFragments.TTestStatisticsStats;
import app.goStat.view.functions.functionFragments.TestsData.ZTestStatisticsData;
import app.goStat.view.functions.functionFragments.ZTestStatisticsStats;

public class FunctionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.functions_activity);
        Spinner spinner = findViewById(R.id.functions_selection_spinner);
        colorSpinner(spinner);
        spinner.setOnItemSelectedListener(this);

    }

    private void colorSpinner(Spinner spinner) {

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_items_static_functions_selection)) {
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
        spinner.setAdapter(spinnerArrayAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch(position) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.function_content_fragment, InstructionsFragment.newInstance())
                        .commitNow();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.function_content_fragment, BinomialFragment.newInstance())
                        .commitNow();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.function_content_fragment, TTestStatisticsStats.newInstance())
                        .commitNow();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.function_content_fragment, TTestStatisticsData.newInstance())
                        .commitNow();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.function_content_fragment,  ZTestStatisticsStats.newInstance())
                        .commitNow();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.function_content_fragment, ZTestStatisticsData.newInstance())
                        .commitNow();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
