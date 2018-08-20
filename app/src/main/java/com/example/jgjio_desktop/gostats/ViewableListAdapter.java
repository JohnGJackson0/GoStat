package com.example.jgjio_desktop.gostats;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ViewableListAdapter extends RecyclerView.Adapter<ViewableListAdapter.DataPointViewHolder> {

    List<DataPoint> mDataPointList;

    @Override
    public DataPointViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_viewable_data_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup,shouldAttachToParentImmediately);
        DataPointViewHolder viewHolder = new DataPointViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(DataPointViewHolder holder, int position) {
        holder.viewableDataPoint.setText(mDataPointList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return mDataPointList == null ? 0 : mDataPointList.size();
    }


    //TODO notify data RANGE instead of DATA SET
    public void updateList(List<DataPoint> updatedList) {
        if(mDataPointList == null) {
            mDataPointList = updatedList;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(mDataPointList, updatedList));
            mDataPointList = updatedList;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    class DataPointViewHolder extends RecyclerView.ViewHolder {
        TextView viewableDataPoint;

        public DataPointViewHolder(View itemView) {
            super(itemView);
            viewableDataPoint = itemView.findViewById(R.id.viewable_data_point_text_view);

        }

    }

}
