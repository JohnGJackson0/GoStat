package com.example.jgjio_desktop.gostats;

import android.support.v7.util.DiffUtil;

import java.util.List;
import java.util.Objects;

public class DiffCallback extends DiffUtil.Callback {

    List<DataPoint> mOldDataPoints;
    List<DataPoint> mNewDataPoints;

    public DiffCallback(List<DataPoint> oldDataPoints, List<DataPoint> newDataPoints) {
        mOldDataPoints =oldDataPoints;
        mNewDataPoints =newDataPoints;
    }

    @Override
    public int getOldListSize() {
        return mOldDataPoints.size();
    }

    @Override
    public int getNewListSize() {
        return mNewDataPoints.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldDataPoints.get(oldItemPosition).getID() ==
                mNewDataPoints.get(newItemPosition).getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        DataPoint newDataPoint = mNewDataPoints.get(newItemPosition);
        DataPoint oldDataPoint = mOldDataPoints.get(oldItemPosition);

        return newDataPoint.getID() == oldDataPoint.getID()
                && Objects.equals(newDataPoint.get(), oldDataPoint.get())
                && Objects.equals(newDataPoint.isEnabled(), oldDataPoint.isEnabled());
    }
}
