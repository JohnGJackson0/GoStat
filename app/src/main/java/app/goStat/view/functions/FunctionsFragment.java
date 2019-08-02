package app.goStat.view.functions;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import app.goStat.R;
import app.goStat.view.functions.functionFragments.BinomialFragment;

public class FunctionsFragment extends Fragment {

    private FunctionsViewModel mViewModel;
    private Spinner mSpinner;
    private View mRootView;


    public static FunctionsFragment newInstance() {
        return new FunctionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.functions_fragment, container, false);
        colorSpinner();
        //todo
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.function_content_fragment,
                BinomialFragment.newInstance()).commit();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FunctionsViewModel.class);
    }

    private void colorSpinner() {
        Spinner spinner = mRootView.findViewById(R.id.functions_selection_spinner);


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_items_static_functions_selection)) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                return view;
            }
        };
        spinner.setAdapter(spinnerArrayAdapter);
    }

}
