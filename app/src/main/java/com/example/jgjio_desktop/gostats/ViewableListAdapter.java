package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
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

    public ViewableListAdapter (Context context) {

    }

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

    public void updateList(List<DataPoint> dataPointsList) {
        mDataPointList = dataPointsList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(DataPointViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mDataPointList == null ? 0 : mDataPointList.size();
    }


    class DataPointViewHolder extends RecyclerView.ViewHolder {
        TextView viewableDataPoint;
        TextView positionDataPoint;

        public DataPointViewHolder(View itemView) {
            super(itemView);
            viewableDataPoint = itemView.findViewById(R.id.viewable_data_point_text_view);
            positionDataPoint = itemView.findViewById(R.id.item_position);

        }

        void bind (int listIndex) {
            viewableDataPoint.setText(Double.toString(mDataPointList.get(listIndex).getValue()));
            positionDataPoint.setText(Integer.toString(listIndex + 1));
        }

    }

}
