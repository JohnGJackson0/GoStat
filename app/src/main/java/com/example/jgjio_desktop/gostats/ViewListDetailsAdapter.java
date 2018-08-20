package com.example.jgjio_desktop.gostats;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ViewListDetailsAdapter extends RecyclerView.Adapter<ViewListDetailsAdapter.ListDetailViewHolder> {
    private List<StatisticalList> mLists;


    public ViewListDetailsAdapter(Context context) { }

    public void setLists(List<StatisticalList> lists) {
        mLists = lists;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public ListDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_list_detail_content_inside_grid;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ListDetailViewHolder viewHolder = new ListDetailViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewListDetailsAdapter.ListDetailViewHolder holder, int position) {
        holder.bind(position);
    }



    class ListDetailViewHolder extends RecyclerView.ViewHolder {
        TextView listNameInsideGrid;

        public ListDetailViewHolder(View itemView) {
            super(itemView);
            listNameInsideGrid = itemView.findViewById(R.id.list_name_grid_detail);
        }

        void bind (int listIndex) {
            listNameInsideGrid.setText(mLists.get(listIndex).getName());
        }
    }

}
