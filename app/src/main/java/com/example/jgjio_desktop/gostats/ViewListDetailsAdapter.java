package com.example.jgjio_desktop.gostats;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStores;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.google.gson.reflect.TypeToken.get;


public class ViewListDetailsAdapter extends RecyclerView.Adapter<ViewListDetailsAdapter.ListDetailViewHolder> {
    private List<StatisticalList> mLists;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private Activity mContext;

    private ActionMode mActionMode;
    private int positionLongHoldClick;
    ActiveListViewModel mActiveListViewModel;



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
            mActionMode =null;

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int listIndex = mLists.get(getAdapterPosition()).getId();
                    startViewIntent(listIndex);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
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

        void bind (int listIndex) {
            listNameInsideGrid.setText(mLists.get(listIndex).getName());
        }

    }

    private void startViewIntent(int listIndex) {
        Intent intent = new Intent(mContext, ViewSingleListActivity.class);
        intent.putExtra(EXTRA_LIST_ID, listIndex);
        mContext.startActivity(intent);
    }

    private void startEditIntent(int listIndex) {
        Intent intent = new Intent(mContext, EditableListActivity.class);
        intent.putExtra(EXTRA_LIST_ID, listIndex);
        mContext.startActivity(intent);
    }


}
