package com.example.jgjio_desktop.gostats.Repository;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jgjio_desktop.gostats.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO disallow invalid and nothing inputs before adding lines
//currently, the data is added as disabled and 0

//TODO fix layout so that it doesn't studder

//TODO allow user to append data, possibly in another adapter


//Todo need to build this list to support changes to the Room DataPoints
//     by pending the data, and waiting to update, if the list changes
//     on the pending point, it will display update and not the pending point
//     so user will see the wrong point, even though we have it saved in a
//     pending list

//Todo allow the user to expand the DataPoints in the list
//todo implement updatable and provide

public class EditableListAdapter extends PagedListAdapter<DataPoint, EditableListAdapter.NumberViewHolder>  {
    private Set<DataPoint> mUpdatedNonAppendingDataPoints = new HashSet<>();
    private OnLastEditTextOnEnterCallBack mOnLastEnterCallBack;
    private Context mContext;
    private Boolean requestFocusOnLastEditTextInOnBindViewHolder = false;

    public void setLastEditTextToRequestFocus() {
        requestFocusOnLastEditTextInOnBindViewHolder = true;
    }

    public interface OnLastEditTextOnEnterCallBack {
        void createDataElement();
    }

    public List<DataPoint> getPendingUpdates() {
        List<DataPoint> mUpdate = new ArrayList<>(mUpdatedNonAppendingDataPoints);
        mUpdatedNonAppendingDataPoints.clear();
        return mUpdate;
    }

    protected EditableListAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
        mOnLastEnterCallBack = (OnLastEditTextOnEnterCallBack) mContext;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {
        TextView mIndexOfEditableRow;
        EditText mEditableDataPoint;

        protected EditTextListener editTextListener;

        public NumberViewHolder(View itemView, EditTextListener editTextListener) {
            super(itemView);

            mIndexOfEditableRow =  itemView.findViewById(R.id.dataPointIndexTextView);
            mEditableDataPoint =  itemView.findViewById(R.id.dataPoint);
            this.editTextListener = editTextListener;
            mEditableDataPoint.addTextChangedListener(this.editTextListener);
        }

        void bindTo (DataPoint dataPoint, int listIndex)
        {
            mIndexOfEditableRow.setText(Integer.toString(listIndex + 1));//statistical lists start at 1

            if (dataPoint.isEnabled()) {
                mEditableDataPoint.setText(dataPoint.getValue().toString());
            } else {
                mEditableDataPoint.setText(null);
            }

            if (requestFocusOnLastEditTextInOnBindViewHolder && (listIndex == getItemCount() -1)) {
                mEditableDataPoint.requestFocus();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                requestFocusOnLastEditTextInOnBindViewHolder = false;
            }
        }

        void clear() {
            //TODO IMPLEMENT
        }

        private void listenForNextEnter() {
            mEditableDataPoint.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        //add callback to update room
                        mOnLastEnterCallBack.createDataElement();
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

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int templateLayoutID = R.layout.item_editable_data_row;
        Context context = viewGroup.getContext();
        boolean shouldAttachToParentImmediately = false;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(templateLayoutID, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view, new EditTextListener());
        return viewHolder;
    }


    private class EditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if ((charSequence.toString().startsWith(".") && charSequence.length() == 1) || charSequence.length() == 0) {
                getItem(position).setEnabled(false);
            } else {
                getItem(position).setEnabled(true);
                getItem(position).setValue(new BigDecimal(charSequence.toString()));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mUpdatedNonAppendingDataPoints.add(getItem(position));
        }
    }

    @Override
    public void onBindViewHolder(EditableListAdapter.NumberViewHolder holder, final int position) {
        DataPoint dataPoint = getItem(position);

        holder.editTextListener.updatePosition(holder.getAdapterPosition());

        if (dataPoint != null) {
            holder.bindTo(dataPoint, position);
        } else {
            holder.clear();
        }

        if (position == getItemCount()-1) {
            holder.listenForNextEnter();
        }
    }

    // a DataPoint may have changed if reloaded from the database,
    // but ID is fixed.
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

}
