package com.example.jgjio_desktop.gostats;

import android.content.Context;
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

public class EditableListAdapter extends RecyclerView.Adapter<EditableListAdapter.NumberViewHolder> {
    private int mNumberOfEditableRows = 0;
    private List<DataPoint> mDataList;

    public EditableListAdapter(List<DataPoint> dataList) {
        mDataList = dataList;
        addItem();
    }

    public void addItem() {
        DataPoint mDataPoint = new DataPoint(false);
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
            mDeletionButton = itemView.findViewById(R.id.deleteDataPoint);
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
            holder.mEditableDataPoint.setText(mDataList.get(holder.getAdapterPosition()).toString());
        } else {
            holder.mEditableDataPoint.setText(null);
        }
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int templateLayoutID = R.layout.data_row;
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
                    || charSequence.toString().isEmpty())) {
                mDataList.get(position).enable();
                mDataList.get(position).set(Double.parseDouble(charSequence.toString()));
            } else {
                mDataList.get(position).disable();
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }




}
