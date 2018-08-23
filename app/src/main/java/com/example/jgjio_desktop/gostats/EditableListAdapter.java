package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//TODO disallow invalid and nothing inputs before adding lines
//currently, the data is added as disabled and 0

//TODO fix layout so that it doesn't studder

public class EditableListAdapter extends RecyclerView.Adapter<EditableListAdapter.NumberViewHolder> {
    private int mNumberOfEditableRows = 0;
    private List<DataPoint> mDataList;
    private int mListId;
    private AppDatabase mDb;
    private Context mContext;
    AppRepository mAppRepo;
    EditableListViewModel model;

    public EditableListAdapter(List<DataPoint> dataList, int listId, Context context) {
        mDataList = dataList;
        this.mListId = listId;
        mContext = context;
        addItem();
        mDb = AppDatabase.getAppDatabase(context);

    }

    public void updateDatabase() {
        model = ViewModelProviders.of((FragmentActivity) mContext).get(EditableListViewModel.class);
        model.insertDataPoints(mDataList);
    }

    public void addItem() {
        DataPoint mDataPoint = new DataPoint(mListId,false, 0.);
        mNumberOfEditableRows++;
        mDataList.add(mDataPoint);
        this.notifyItemInserted(mNumberOfEditableRows);
    }

    @Override
    public int getItemCount() {
        return mNumberOfEditableRows;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {
        TextView mIndexOfEditableRow;
        EditText mEditableDataPoint;
        ImageView mDeletionButton;

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

            mEditableDataPoint.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        createDataRow();
                        removeOnKeyListener(mEditableDataPoint);
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
        holder.nextEnterEditTextListener();
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());

        if (mDataList.get(holder.getAdapterPosition()).isEnabled()) {
            holder.mEditableDataPoint.setText(Double.toString(mDataList.get(holder.getAdapterPosition()).getValue()));
        } else {
            holder.mEditableDataPoint.setText(null);
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

            if (!(charSequence.toString() == "" || charSequence.toString() == null
                    || charSequence.toString().isEmpty() || charSequence.toString().startsWith("."))) {
                mDataList.get(position).setEnabled(true);
                mDataList.get(position).setValue(Double.parseDouble(charSequence.toString()));
            } else {
                mDataList.get(position).setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {


        }
    }




}
