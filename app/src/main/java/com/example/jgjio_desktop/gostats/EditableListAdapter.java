package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO disallow invalid and nothing inputs before adding lines
//currently, the data is added as disabled and 0

//TODO fix layout so that it doesn't studder

public class EditableListAdapter extends RecyclerView.Adapter<EditableListAdapter.NumberViewHolder> {
    private int mNumberOfEditableRows = 0;
    private List<DataPoint> mDataList = new ArrayList<>();
    private int mListId;
    private AppDatabase mDb;
    private Context mContext;
    EditableListViewModel mViewModel;
    Set<Integer> mInputsToBeUpdated = new HashSet();


    public EditableListAdapter(int listId, Context context) {
        this.mListId = listId;
        mContext = context;
        addItem();
        mDb = AppDatabase.getAppDatabase(context);

        Log.d("NumItems", Integer.toString(getItemCount()));
        mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(EditableListViewModel.class);

    }

    public void updateDatabase() {
        mViewModel.insertDataPoints(mDataList);
    }

    public void addItem() {
        DataPoint mDataPoint = new DataPoint(mListId,false, 0.);
        mNumberOfEditableRows++;
        mDataList.add(mDataPoint);
        this.notifyItemInserted(mNumberOfEditableRows);
    }

    public void updateList(List<DataPoint> myList) {
        if (myList.size() != 0) {
            mDataList = myList;
            notifyDataSetChanged();
            mNumberOfEditableRows = mDataList.size();
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 1 : mDataList.size();
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {
        TextView mIndexOfEditableRow;
        EditText mEditableDataPoint;

        public MyCustomEditTextListener myCustomEditTextListener;

        public NumberViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);

            mIndexOfEditableRow =  itemView.findViewById(R.id.dataPointIndexTextView);
            mEditableDataPoint =  itemView.findViewById(R.id.dataPoint);
            this.myCustomEditTextListener = myCustomEditTextListener;
            mEditableDataPoint.addTextChangedListener(myCustomEditTextListener);

        }

        void bindData (int listIndex) {
            mIndexOfEditableRow.setText(String.valueOf(listIndex + 1)); //start index at 1 instead of 0

        }


        private void nextEnterEditTextListener() {
            //we only want one on key listener, two creates multiple rows
            //when using notify item changed, this will happen without the
            //following line
            removeOnKeyListener(mEditableDataPoint);

            Log.d("sd", "sd");

            mEditableDataPoint.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (mDataList.get(mDataList.size()-1).isEnabled() == false) {
                            ((EditableListActivity)mContext).displayInputInvalidDialog(mDataList.size()-1);
                        } else {
                            createDataRow();
                            removeOnKeyListener(mEditableDataPoint);
                        }
                    }
                    return false;
                }
            });

        }

    }

    private void removeOnKeyListener(EditText editText) {
        editText.setOnKeyListener(null);
    }

    public int createDataRow() {
        this.addItem();
        return this.getItemCount();
    }

    //DO NOT ADD TOO MUCH IN THIS METHOD.
    @Override
    public void onBindViewHolder(EditableListAdapter.NumberViewHolder holder, final int position) {
        holder.bindData(position);
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());

        if (mDataList.get(position).isEnabled()) {
            holder.mEditableDataPoint.setText(Double.toString(mDataList.get(holder.getAdapterPosition()).getValue()));
            holder.itemView.setBackgroundColor(mContext.getColor(R.color.cardview_light_background));
        } else {
            Log.d("s", "isdisabled at " + Integer.toString(position));
            holder.itemView.setBackgroundColor(mContext.getColor(R.color.highlight));
            holder.mEditableDataPoint.setText(null);
        }

        Log.d("pos: ", Integer.toString(position));
        Log.d("last MdataList", Integer.toString(mDataList.size()));

        //test for if last entry
        //what if last entry already has one?

        if(position == mDataList.size() -1) {
            holder.nextEnterEditTextListener();
            holder.mEditableDataPoint.requestFocus();
        }
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int templateLayoutID = R.layout.item_editable_data_row;
        Context context = viewGroup.getContext();
        boolean shouldAttachToParentImmediately = false;


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(templateLayoutID, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view, new MyCustomEditTextListener());

        return viewHolder;
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //TODO charSequence.toString().startsWith(".") creates a bug of setting editText to disabled if 0.
            if (!(charSequence.toString() == "" || charSequence.toString() == null
                    || charSequence.toString().isEmpty() || charSequence.toString().startsWith("."))) {
                mDataList.get(position).setEnabled(true);
                mDataList.get(position).setValue(Double.parseDouble(charSequence.toString()));
            } else {
                mDataList.get(position).setEnabled(false);
            }
            mInputsToBeUpdated.add(position);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                ((EditableListActivity)mContext).displayInputInvalidDialog(position);
            }
        }
    }



}
