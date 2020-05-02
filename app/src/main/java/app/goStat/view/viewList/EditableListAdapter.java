package app.goStat.view.viewList;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.v7.util.DiffUtil;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.goStat.model.DataPoint;
import app.goStat.R;
import app.goStat.util.debug.MyDebug;

public class EditableListAdapter extends PagedListAdapter<DataPoint, EditableListAdapter.NumberViewHolder>   {
    private Set<DataPoint> mUpdatedNonAppendingDataPoints = new HashSet<>();
    private OnLastEditTextOnEnterCallBack mOnLastEnterCallBack;
    private NewViewHolderReceiverCallBack mNewViewHolderReceiverCallBack;
    private Context mContext;

    public interface OnLastEditTextOnEnterCallBack {
        void createDataElement();
    }

    public interface NewViewHolderReceiverCallBack {
        void receiveNewestViewHolder(EditableListAdapter.NumberViewHolder vh);
    }

    public List<DataPoint> getPendingUpdates() {
        List<DataPoint> updateList = new ArrayList<>(mUpdatedNonAppendingDataPoints);
        mUpdatedNonAppendingDataPoints.clear();
        return updateList;
    }

    protected EditableListAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
        mOnLastEnterCallBack = (OnLastEditTextOnEnterCallBack) mContext;
        mNewViewHolderReceiverCallBack = (NewViewHolderReceiverCallBack) mContext;
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

            //before doing this should we check to see if focus is on
            mEditableDataPoint.addTextChangedListener(this.editTextListener);
        }

        void bindTo (DataPoint dataPoint, int position) {
            mIndexOfEditableRow.setText(Integer.toString(position + 1));//statistical ListsLoader start at 1
            if (MyDebug.LOG)
            Log.v("NumberViewHolder", "bindTo: binding at position: " + Integer.toString(position));

            if (dataPoint.isEnabled()) {
                mEditableDataPoint.getText().clear();
                mEditableDataPoint.append(dataPoint.getValue().toString());
            } else if (mEditableDataPoint.length() > 0) {
                mEditableDataPoint.getText().clear();
            }
        }

        void clear() {
            //TODO IMPLEMENT
        }

        private void createNewDataPointOnNextEnter() {
            mEditableDataPoint.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        //add callback to update room
                        mOnLastEnterCallBack.createDataElement();
                        if (MyDebug.LOG)
                        Log.d("NumberViewHolder", "onKey: Key Event of enter on last EditText, creating data point");
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

        //todo make more efficient
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
            holder.createNewDataPointOnNextEnter();
            mNewViewHolderReceiverCallBack.receiveNewestViewHolder(holder);
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
