package app.goStat;

import android.arch.lifecycle.Observer;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class ViewFrequencyTableFragment extends Fragment implements View.OnClickListener {
    private int mListID;
    private String mCopyToClipboardText;
    private View mRootView;
    private Button mCopyToClipboard;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.view_frequency_table_fragment, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        setCopyTo();
        mCopyToClipboard = mRootView.findViewById(R.id.copy_to_clipboard_button);
        mCopyToClipboard.setOnClickListener(this);
        configureRecyclerView();
        return mRootView;
    }

    public static ViewFrequencyTableFragment newInstance(int listId) {
        ViewFrequencyTableFragment fragment = new ViewFrequencyTableFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    private void configureRecyclerView() {
        RecyclerView recyclerView = mRootView.findViewById(R.id.rv_interval_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        FrequencyIntervalAdapter adapter = new FrequencyIntervalAdapter();
        recyclerView.setAdapter(adapter);
        getViewModel().getListById(mListID).observe(this, adapter::submitList);
    }

    private ViewFrequencyTableFragmentViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewFrequencyTableFragmentViewModel.class);
    }

    private void showCopyToClipboardMessage() {
        Toast toast = Toast.makeText(getActivity(),
                "Copied To Clipboard",
                Toast.LENGTH_SHORT);
        View view = toast.getView();
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    @Override
    public void onClick(View view) {
        copyToClipboard();
    }

    private void setCopyTo() {
        getViewModel().getTable(mListID).observe(this, new Observer<List<FrequencyInterval>>() {
            @Override
            public void onChanged(@Nullable List<FrequencyInterval> frequencyIntervals) {
                mCopyToClipboardText = "";
                mCopyToClipboardText = "Frequency Table: Go! Statistics \n";
                for (FrequencyInterval val : frequencyIntervals) {
                    mCopyToClipboardText = mCopyToClipboardText  + "Interval: " + val.toString()  +  " Frequency: " + val.getFrequency() + " \n";
                }
            }
        });
    }

    private void copyToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Frequency Table", mCopyToClipboardText);
        clipboard.setPrimaryClip(clip);
        showCopyToClipboardMessage();
    }
}
