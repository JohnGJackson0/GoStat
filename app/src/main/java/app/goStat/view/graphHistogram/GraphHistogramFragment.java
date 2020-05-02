package app.goStat.view.graphHistogram;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import app.goStat.model.FrequencyInterval;
import app.goStat.R;
import app.goStat.util.debug.MyDebug;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.support.constraint.Constraints.TAG;

@RuntimePermissions
public class GraphHistogramFragment extends Fragment {
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private int mListID;
    private ProgressBar mProgressBar;
    private BarChart mBarChart;
    private Button mShare;
    private Activity mParentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // due to lifecycle getActivity() or getContext() will return null before this is called.
        View rootView = inflater.inflate(R.layout.fragment_graph_histogram, container, false);
        getActivity().setTitle(getResources().getString(R.string.fragment_label_graph_histogram));
        mParentActivity = getActivity();
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        mBarChart = (BarChart) rootView.findViewById(R.id.graph);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        graphHistogramChart(rootView);
        resizeFragment(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mShare = rootView.findViewById(R.id.share_button);
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareChart();
            }
        });

        return rootView;
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void shareChart() {
        ShareBitmapTask task = new ShareBitmapTask(mProgressBar, mBarChart, getContext());
        task.execute();
    }

    //https://www.youtube.com/watch?v=uKx0FuVriqA

    //todo need for bitmap as well
    private static class ShareBitmapTask extends AsyncTask<Void, Integer, Uri> {
        private WeakReference<ProgressBar> progressBarWeakReference;
        private WeakReference<Context> contextWeakReference;
        private WeakReference<BarChart> barChartWeakReference;

        ShareBitmapTask(ProgressBar progressBar, BarChart barChart, Context context) {
            progressBarWeakReference = new WeakReference<ProgressBar>(progressBar);
            contextWeakReference = new WeakReference<Context>(context);
            barChartWeakReference = new WeakReference<BarChart>(barChart);
        }

        //this is on UI thread
        @Override
        protected void onPreExecute() {
            //strong reference but only in scope of method = better
            //ProgressBar progressBar = weakReferenceProgressBar.get();
            ProgressBar progressBar = progressBarWeakReference.get();
            if (progressBar == null){
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Uri doInBackground(Void... nothing) {
            Bitmap currentDrawStateOfChart = barChartWeakReference.get().getChartBitmap();

            File imagesFolder = new File(contextWeakReference.get().getCacheDir(), "images");
            Uri uri = null;
            try {
                imagesFolder.mkdirs();
                File file = new File(imagesFolder, "shared_image.png");
                FileOutputStream stream = new FileOutputStream(file);
                currentDrawStateOfChart.compress(Bitmap.CompressFormat.PNG, 90, stream);
                stream.flush();
                stream.close();
                uri = FileProvider.getUriForFile(contextWeakReference.get(), "app.goStat.fileprovider", file);
            } catch (IOException e) {
                if (MyDebug.LOG)
                Log.d(TAG, "IOException while trying to write file for sharing: " + e.getMessage());
            }
            return uri;
        }

        //result return of do in background
        //this is also on UI thread
        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);

            ProgressBar progressBar = progressBarWeakReference.get();
            if (progressBar == null){
                return;
            }
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/png");
            contextWeakReference.get().startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GraphHistogramFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //method called after user has denied the permission
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationalForExternalStorageAccess(PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setTitle(getActivity().getString(R.string.title_alert_dialog_external_storage_permission_request))
                .setMessage(getActivity().getString(R.string.text_alert_dialog_external_storage_permission_request))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onExternalStorageDenied() {
        Toast toast = Toast.makeText(getActivity(),
                getResources().getString(R.string.toast_external_storage_denied),
                Toast.LENGTH_SHORT);
        View view = toast.getView();
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onExternalStorageNeverAskAgain() {
        Toast toast = Toast.makeText(getActivity(),
                getResources().getString(R.string.toast_external_storage_never_ask_again),
                Toast.LENGTH_SHORT);
        View view = toast.getView();
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    public static GraphHistogramFragment newInstance(int listId) {
        GraphHistogramFragment fragment = new GraphHistogramFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    private void graphHistogramChart(View root){
        int primary = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        int secondary = ContextCompat.getColor(getContext(), R.color.colorSecondary);

        mBarChart.getLegend().setEnabled(false);
        mBarChart.getDescription().setEnabled(false);
        getViewModel().getOnCreatedAssociatedListName(mListID).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mBarChart.getDescription().setEnabled(true);
                mBarChart.getDescription().setText(s);
                //mBarChart.getDescription().setTextSize(getResources().getDimension(R.dimen.text_graph_description));
                //mBarChart.getDescription().setTextColor(secondary);
            }
        });

        getViewModel().getFrequencyIntervalsInTable(mListID).observe(this, new Observer<List<FrequencyInterval>>() {
            @Override
            public void onChanged(@Nullable List<FrequencyInterval> frequencyIntervals) {
                List<BarEntry> barEntries = new ArrayList<>();

                float i = 1f;
                ArrayList<String> bins = new ArrayList<>();

                for (FrequencyInterval freqInterval : frequencyIntervals) {
                    int value = freqInterval.getFrequency();
                    barEntries.add(new BarEntry((float) i, value));
                    i++;
                }

                BarDataSet dataSet = new BarDataSet(barEntries, "example label");
                dataSet.setColor(primary);
                BarData barData = new BarData(dataSet);
                barData.setBarWidth(1);
                mBarChart.setData(barData);
                mBarChart.invalidate(); // refresh
            }
        });
    }

    private void resizeFragment(View rootView, int newWidth, int newHeight) {
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(newWidth, newHeight);
        rootView.setLayoutParams(p);
        rootView.requestLayout();
    }

    private GraphHistogramViewModel getViewModel() {
        return ViewModelProviders.of(this).get(GraphHistogramViewModel.class);
    }
}
