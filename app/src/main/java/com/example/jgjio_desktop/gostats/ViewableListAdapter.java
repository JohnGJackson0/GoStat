package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ViewableListAdapter extends PagedListAdapter<DataPoint, ViewableListAdapter.DataPointViewHolder> {

    protected ViewableListAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public void onBindViewHolder(@NonNull DataPointViewHolder holder, int position) {

        DataPoint dataPoint = getItem(position);

        if (dataPoint != null) {
            holder.bindTo(position, dataPoint);
        } else {
            holder.clear();
        }
    }

    private static DiffUtil.ItemCallback<DataPoint> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<DataPoint>() {
                @Override
                public boolean areItemsTheSame(DataPoint oldDataPoint, DataPoint newDataPoint) {
                    return oldDataPoint.getId() == newDataPoint.getId();
                }

                @Override
                public boolean areContentsTheSame(DataPoint oldDataPoint,
                                                  DataPoint newDataPoint) {
                    return oldDataPoint.equals(newDataPoint);
                }
            };


    class DataPointViewHolder extends RecyclerView.ViewHolder {
        TextView viewableDataPoint;
        TextView positionDataPoint;

        public DataPointViewHolder(View itemView) {
            super(itemView);
            viewableDataPoint = itemView.findViewById(R.id.viewable_data_point_text_view);
            positionDataPoint = itemView.findViewById(R.id.item_position);

        }

        void bindTo(int listIndex, DataPoint dataPoint) {

            viewableDataPoint.setText(Double.toString(dataPoint.getValue()));
            positionDataPoint.setText(Integer.toString(listIndex + 1)); //statistical lists start at 1 instead of 0

            if (!dataPoint.isEnabled()) {
                itemView.setBackgroundColor(itemView.getContext().getColor(R.color.highlight));
                viewableDataPoint.setText(R.string.disabled_data_point_text);
            } else {
                itemView.setBackgroundColor(itemView.getContext().getColor(R.color.colorWhite));
            }
        }

        void clear() {
            //todo what to do here?
        }

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

}
