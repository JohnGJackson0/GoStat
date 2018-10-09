package com.example.jgjio_desktop.gostats;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//todo add final for list id elsewhere like with mActionId here
public class ShowSummaryStatisticsListSelectionAdapter extends RecyclerView.Adapter<ShowSummaryStatisticsListSelectionAdapter.ListDetailsViewHolder>{
    private List<StatisticalList> mLists;
    private Context mContext;
    private final int mActionId;

    public ShowSummaryStatisticsListSelectionAdapter(Context context, int actionId) {
        mContext = context;
        mActionId = actionId;
    }


    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    public int getItemCount() {
        if (mLists == null) {
            return 0;
        } else
            return mLists.size();
    }

    @Override
    public void onBindViewHolder(ShowSummaryStatisticsListSelectionAdapter.ListDetailsViewHolder holder, int position) {
        holder.listName.setText(mLists.get(position).getName());
        holder.listId.setText(Integer.toString(mLists.get(position).getId()));
    }

    public void update(List<StatisticalList> updatedList) {
        mLists = updatedList;
        notifyDataSetChanged();
    }

    class ListDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        TextView listId;

        public ListDetailsViewHolder(View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name_selector_text);
            listId = itemView.findViewById(R.id.list_id_selector_text);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int listIndex = mLists.get(getAdapterPosition()).getId();
                    startSummaryIntent(listIndex);
                }
            });
        }
    }

    @Override
    public ListDetailsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_list_selector;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ListDetailsViewHolder viewHolder = new ListDetailsViewHolder(view);
        return viewHolder;
    }


    private void startSummaryIntent(double listIndex) {
        if (mActionId == 1) {
            Intent intent = new Intent(mContext, ShowSummaryStatisticsActivity.class);
            intent.putExtra(EXTRA_LIST_ID, listIndex);
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, GraphActivity.class);
            intent.putExtra(EXTRA_LIST_ID, listIndex);
            mContext.startActivity(intent);
        }
    }





}
