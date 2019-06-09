package app.goStat.view.viewFrequencyTable;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.goStat.model.FrequencyInterval;
import app.goStat.R;

public class FrequencyIntervalAdapter extends PagedListAdapter<FrequencyInterval, FrequencyIntervalAdapter.FrequencyIntervalViewHolder> {

    @Override
    public void onBindViewHolder(FrequencyIntervalAdapter.FrequencyIntervalViewHolder holder, final int position) {
        FrequencyInterval frequencyInterval =  getItem(position);

        if (frequencyInterval != null) holder.bindTo(frequencyInterval, position);
    }

    protected FrequencyIntervalAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public FrequencyIntervalAdapter.FrequencyIntervalViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int templateLayoutID = R.layout.item_interval_details;
        Context context = viewGroup.getContext();
        boolean shouldAttachToParentImmediately = false;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(templateLayoutID, viewGroup, shouldAttachToParentImmediately);
        FrequencyIntervalAdapter.FrequencyIntervalViewHolder viewHolder = new FrequencyIntervalAdapter.FrequencyIntervalViewHolder(view);
        return viewHolder;
    }

    class FrequencyIntervalViewHolder extends RecyclerView.ViewHolder {
        TextView interval;
        TextView frequency;

        public FrequencyIntervalViewHolder(View itemView) {
            super(itemView);
            interval = itemView.findViewById(R.id.interval_detail);
            frequency = itemView.findViewById(R.id.frequency_detail);
        }

        void bindTo (FrequencyInterval frequencyInterval, int position) {
            frequency.setText(Integer.toString(frequencyInterval.getFrequency()));
            interval.setText(frequencyInterval.toString());
        }
    }

    private static DiffUtil.ItemCallback<FrequencyInterval> DIFF_CALLBACK = new DiffUtil.ItemCallback<FrequencyInterval>() {
        @Override
        public boolean areItemsTheSame(FrequencyInterval oldFrequencyInterval, FrequencyInterval newFrequencyInterval) {
            return oldFrequencyInterval.getId() == newFrequencyInterval.getId();
        }
        @Override
        public boolean areContentsTheSame(FrequencyInterval oldFrequencyInterval, FrequencyInterval newFrequencyInterval) {
            return oldFrequencyInterval.equals(newFrequencyInterval);
        }
    };
}
