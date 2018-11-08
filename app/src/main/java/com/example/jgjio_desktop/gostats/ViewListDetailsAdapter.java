package com.example.jgjio_desktop.gostats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ViewListDetailsAdapter extends RecyclerView.Adapter<ViewListDetailsAdapter.ListDetailViewHolder> {
    private List<StatisticalList> mLists;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private Activity mContext;
    private ActionMode mActionMode;
    private int positionLongHoldClick;

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
                    startViewIntent(mLists.get(positionLongHoldClick).getId());
                    mode.finish();
                    return true;
                case R.id.action_edit_list:
                    startEditIntent(mLists.get(positionLongHoldClick).getId());
                    mode.finish();
                    return true;
                case R.id.action_delete_list:
                    if (mContext instanceof ViewableListsActivity) {
                        ((ViewableListsActivity)mContext).removeList(mLists.get(positionLongHoldClick));
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

    ViewListDetailsAdapter(Activity context) {
        mContext = context;
    }

    public void setLists(List<StatisticalList> lists) {
        mLists = lists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mLists == null) {
            return 0;
        } else return mLists.size();
    }

    @Override
    public ListDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_list_details;
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
        TextView listName;
        TextView id;

        TextView userCreatedList;
        TextView frequencyTableList;

        TextView createdByUserMessage;
        TextView createdBySystemMessage;
        TextView editableListMessage;
        TextView lockedMessage;
        TextView frequencyTableInfoMessage;
        TextView staticMessage;


        public ListDetailViewHolder(View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name);
            id = itemView.findViewById(R.id.list_id);
            userCreatedList = itemView.findViewById(R.id.user_created);
            frequencyTableList = itemView.findViewById(R.id.frequency_table);

            createdByUserMessage = itemView.findViewById(R.id.created_by_user_message);
            createdBySystemMessage = itemView.findViewById(R.id.created_by_system_message);
            editableListMessage = itemView.findViewById(R.id.editable_message);
            lockedMessage = itemView.findViewById(R.id.locked_message);
            frequencyTableInfoMessage = itemView.findViewById(R.id.frequency_table_meta_message);
            staticMessage = itemView.findViewById(R.id.static_message);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int idIndex = mLists.get(getAdapterPosition()).getId();
                    startViewIntent(idIndex);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    positionLongHoldClick = getAdapterPosition();

                    if (mActionMode != null) {
                        return false;
                    }

                    mActionMode = mContext.startActionMode(mActionModeCallBack);
                    view.setSelected(true);
                    return true;
                }
            });

        }

        void bind(int listIndex) {
            listName.setText(mLists.get(listIndex).getName());
            id.setText(Integer.toString(mLists.get(listIndex).getId()));

            if (mLists.get(listIndex).isFrequencyTable()) {
                userCreatedList.setVisibility(View.GONE);
                frequencyTableList.setVisibility(View.VISIBLE);
                createdByUserMessage.setVisibility(View.GONE);
                createdBySystemMessage.setVisibility(View.VISIBLE);
                editableListMessage.setVisibility(View.GONE);
                lockedMessage.setVisibility(View.VISIBLE);
                //todo not yet implemented
                frequencyTableInfoMessage.setVisibility(View.GONE);
                staticMessage.setVisibility(View.VISIBLE);

            } else {
                userCreatedList.setVisibility(View.VISIBLE);
                frequencyTableList.setVisibility(View.GONE);

                createdByUserMessage.setVisibility(View.VISIBLE);
                createdBySystemMessage.setVisibility(View.GONE);
                editableListMessage.setVisibility(View.VISIBLE);
                lockedMessage.setVisibility(View.GONE);
                frequencyTableInfoMessage.setVisibility(View.GONE);
                staticMessage.setVisibility(View.GONE);
            }
        }
    }

    private void startViewIntent(int listIndex) {
        Intent intent = new Intent(mContext, ViewSingleListActivity.class);
        intent.putExtra(EXTRA_LIST_ID, listIndex);
        mContext.startActivity(intent);
    }

    private void startEditIntent(double listIndex) {
        Intent intent = new Intent(mContext, EditableListActivity.class);
        intent.putExtra(EXTRA_LIST_ID, listIndex);
        mContext.startActivity(intent);
    }
}
