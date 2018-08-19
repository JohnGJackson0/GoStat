package com.example.jgjio_desktop.gostats;

import android.support.v7.util.DiffUtil;

import java.util.List;
import java.util.Objects;

//TODO TEST AND SEE IF THIS IS EVEN WORKING

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
        return mOldDataPoints.get(oldItemPosition).getId() ==
                mNewDataPoints.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        DataPoint newDataPoint = mNewDataPoints.get(newItemPosition);
        DataPoint oldDataPoint = mOldDataPoints.get(oldItemPosition);

        return newDataPoint.getId() == oldDataPoint.getId()
                && Objects.equals(newDataPoint.getValue(), oldDataPoint.getValue())
                && Objects.equals(newDataPoint.isEnabled(), oldDataPoint.isEnabled());
    }
}
