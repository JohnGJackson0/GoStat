package app.goStat;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.goStat.R;


import java.util.List;

public class ViewListDetailsAdapter extends PagedListAdapter<StatisticalList, ViewListDetailsAdapter.ListDetailViewHolder> {
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private Context mContext;
    private int mPositionLongHoldClick;
    private ActionMode mActionMode;
    private ActiveListSelectionFragment mActiveListSelectionFragment;

    protected ViewListDetailsAdapter(Context context, ActiveListSelectionFragment fragmentActivity) {
        super(DIFF_CALLBACK);
        mContext = context;
        mActiveListSelectionFragment = fragmentActivity;
    }

    @Override
    public ViewListDetailsAdapter.ListDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int templateLayoutID = R.layout.item_list_details;
        Context context = viewGroup.getContext();
        boolean shouldAttachToParentImmediately = false;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(templateLayoutID, viewGroup, shouldAttachToParentImmediately);
        ViewListDetailsAdapter.ListDetailViewHolder viewHolder = new ViewListDetailsAdapter.ListDetailViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewListDetailsAdapter.ListDetailViewHolder holder, final int position) {
        StatisticalList statisticalList =  getItem(position);

        if (statisticalList != null) {
            holder.bindTo(statisticalList, position);
        } else {
            holder.clear();
        }
    }

    class ListDetailViewHolder extends RecyclerView.ViewHolder {

        TextView listName;
        TextView frequencyTableMessage;
        TextView listIDMessage;

        CardView createdByUserMessage;
        CardView createdBySystemMessage;
        CardView editableListMessage;
        CardView lockedMessage;
        CardView frequencyTableInfoMessage;
        CardView staticMessage;

        public ListDetailViewHolder(View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.list_name);
            frequencyTableMessage = itemView.findViewById(R.id.frequency_table_meta_message);
            createdByUserMessage = itemView.findViewById(R.id.created_by_user_message_container);
            createdBySystemMessage = itemView.findViewById(R.id.created_by_system_message_container);
            editableListMessage = itemView.findViewById(R.id.editable_message_container);
            lockedMessage = itemView.findViewById(R.id.locked_message_container);
            frequencyTableInfoMessage = itemView.findViewById(R.id.frequency_table_meta_message_container);
            staticMessage = itemView.findViewById(R.id.static_message_container);
            listIDMessage = itemView.findViewById(R.id.list_id_message);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startViewIntent(getItem(getAdapterPosition()).getId(), getItem(getAdapterPosition()).isFrequencyTable());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    mPositionLongHoldClick = getAdapterPosition();

                    if (mActionMode != null) {
                        return false;
                    }

                    mActionMode = view.startActionMode(mActionModeCallBack);
                    view.setSelected(true);
                    return true;
                }
            });


        }

        void bindTo(StatisticalList statisticalList, int position) {
            listName.setText(getItem(position).getName());

            if (statisticalList.isFrequencyTable()) {
                createdByUserMessage.setVisibility(View.GONE);
                createdBySystemMessage.setVisibility(View.VISIBLE);
                editableListMessage.setVisibility(View.GONE);
                lockedMessage.setVisibility(View.VISIBLE);
                frequencyTableInfoMessage.setVisibility(View.VISIBLE);
                staticMessage.setVisibility(View.VISIBLE);
                listName.setTextColor(ContextCompat.getColor(mContext, R.color.colorSecondary));
                frequencyTableMessage.setText("Associated List ID " + Integer.toString(statisticalList.getAssociatedList()));
                frequencyPreview(position, itemView);
            } else {
                createdByUserMessage.setVisibility(View.VISIBLE);
                createdBySystemMessage.setVisibility(View.GONE);
                editableListMessage.setVisibility(View.VISIBLE);
                lockedMessage.setVisibility(View.GONE);
                frequencyTableInfoMessage.setVisibility(View.GONE);
                staticMessage.setVisibility(View.GONE);
                listName.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                listPreview(position, itemView);
            }

            listIDMessage.setText("List ID " + Integer.toString(statisticalList.getId()));
        }

        void clear() {
            //TODO IMPLEMENT
        }

    }


    private void listPreview(int position, View itemView) {

        TextView previewText = itemView.findViewById(R.id.preview);

        int listID = getItem(position).getId();

        getViewModel().getEditableListPreview(listID).observe(mActiveListSelectionFragment, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(@Nullable List<DataPoint> dataPoints) {
                String s = "";
                int index=0;


                //todo add ellipses
                for(DataPoint val: dataPoints) {
                    if (index != 0)
                        s=s+", ";

                    s = s + val.getValue().toString();

                    if (index == 25) {
                        s = s + " ... ";
                    }

                    index++;
                }

                Log.d("kjgdflk", s);
                previewText.setText(s);
            }

        });
    }

    //[2.0, 5.6) -> 2 , [5.6, 9.2) -> 2 ...
    // 11 freq intervals

    private void frequencyPreview(int position, View itemView){
        TextView previewText = itemView.findViewById(R.id.preview);

        int listID = getItem(position).getId();

        getViewModel().getFrequencyTablePreview(listID).observe(mActiveListSelectionFragment, new Observer<List<FrequencyInterval>>() {
            @Override
            public void onChanged(@Nullable List<FrequencyInterval> frequencyIntervals) {
                String s = "";
                int index = 0;

                for (FrequencyInterval val: frequencyIntervals) {
                    if (index != 0)
                        s = s + "\n";

                    s = s + val.toString() + " -> " + val.getFrequency();

                    if (index == 10) {
                        s = s + " ... ";
                    }

                    index++;
                }
                previewText.setText(s);
            }
        });

    }

    // a Statistical List may have changed if reloaded from the database,
    // but ID is fixed.
    private static DiffUtil.ItemCallback<StatisticalList> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<StatisticalList>() {

                @Override
                public boolean areItemsTheSame(StatisticalList oldStatisticalList, StatisticalList newStatisticalList) {
                    return oldStatisticalList.getId() == newStatisticalList.getId();
                }
                @Override
                public boolean areContentsTheSame(StatisticalList oldStatisticalList,
                                                  StatisticalList newStatisticalList) {
                    return oldStatisticalList.equals(newStatisticalList);
                }
            };

    /*
     * Preview
     *
     *
     *
     *
     *
     */


    private ActiveListSelectionViewModel getViewModel() {
        return ViewModelProviders.of(mActiveListSelectionFragment).get(ActiveListSelectionViewModel.class);
    }



    /*
    * Context menu
    *
    *
    *
    *
    *
     */

    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_menu_list_selection, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_view_list:
                    startViewIntent(getItem(mPositionLongHoldClick).getId(), getItem(mPositionLongHoldClick).isFrequencyTable());
                    mode.finish();
                    return true;
                case R.id.action_delete_list:
                    if (mContext instanceof ViewableListsActivity) {
                        ((ViewableListsActivity)mContext).removeList(getItem(mPositionLongHoldClick));
                    }
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };


    private void startViewIntent(int listID, boolean isFrequencyTable) {
        if(!isFrequencyTable) {
            Intent intent = new Intent(mContext, ViewSingleEditableListActivity.class);
            intent.putExtra(EXTRA_LIST_ID, listID);
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, ViewFrequencyTableActivity.class);
            intent.putExtra(EXTRA_LIST_ID, listID);
            mContext.startActivity(intent);
        }
    }
}
