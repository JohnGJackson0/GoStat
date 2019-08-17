package app.goStat.view.functions.functionFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.goStat.R;


public class ZTestStats extends Fragment {
    private View mRootView;

    public ZTestStats() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_ztest_stats, container, false);
        
        return mRootView;
    }


    public static ZTestStats newInstance() {
        ZTestStats fragment = new ZTestStats();
        return fragment;
    }



}
