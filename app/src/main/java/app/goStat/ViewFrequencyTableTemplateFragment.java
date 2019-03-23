package app.goStat;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewFrequencyTableTemplateFragment extends Fragment {
    private int mListID;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    View mRootView;
    Spinner mFunctionSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.view_frequency_table_template_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        setMetaDetails();
        startViewFragment();
        colorSpinner();
        Button executeFunctionButton = mRootView.findViewById(R.id.frequency_table_execute_function_button);
        mFunctionSpinner = mRootView.findViewById(R.id.frequency_table_functions_spinner);

        executeFunctionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int spinnerPosition = mFunctionSpinner.getSelectedItemPosition();
                switch (spinnerPosition) {
                    case 0:
                        startGraphFragment();
                        break;
                    case 1:
                        deleteList();
                        break;
                }
            }
        });
        return mRootView;
    }

    private void colorSpinner() {
        mFunctionSpinner = mRootView.findViewById(R.id.frequency_table_functions_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_items_frequency_table_functions)) {
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
        mFunctionSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void setMetaDetails() {
        TextView name = mRootView.findViewById(R.id.list_name_view);
        getViewModel().getListName(mListID).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) { name.setText(s); }
        });
        TextView id = mRootView.findViewById(R.id.list_id_view);
        id.setText("id " + Integer.toString(mListID));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static ViewFrequencyTableTemplateFragment newInstance(int listId) {
        ViewFrequencyTableTemplateFragment fragment = new ViewFrequencyTableTemplateFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    private ViewFrequencyTableTemplateViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewFrequencyTableTemplateViewModel.class);
    }

    void startGraphFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,  GraphHistogramFragment.newInstance(mListID), getResources().getString(R.string.meta_label_for_histogram_fragment))
                .commit();
        fragmentTransaction.addToBackStack(null);
    }

    void startViewFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.view_window,  ViewFrequencyTableFragment.newInstance(mListID), getResources().getString(R.string.meta_label_frequency_table_fragment))
                .commit();
    }

    void deleteList(){
        getViewModel().deleteList(mListID);
        getActivity().finish();
    }
}
