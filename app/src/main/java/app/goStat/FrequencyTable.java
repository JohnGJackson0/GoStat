package app.goStat;

import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FrequencyTable implements IntervalSeries{
    List<ExclusiveEndMixedFrequencyInterval> mFreqIntervalsForTable;

    FrequencyTable(List<ExclusiveEndMixedFrequencyInterval> freqIntervalsForTable) {
        mFreqIntervalsForTable = freqIntervalsForTable;
        sort();
    }

    public int numberOfFrequencyIntervals() {
        return mFreqIntervalsForTable.size();
    }

    public ExclusiveEndMixedFrequencyInterval get(int position) {
        return mFreqIntervalsForTable.get(position);
    }

    public List<ExclusiveEndMixedFrequencyInterval> get() {
        return mFreqIntervalsForTable;
    }

    public void addAFrequencyToIntervalAt (int position) {
        mFreqIntervalsForTable.get(position).addAFrequency();
        Log.d("dfsdf",Integer.toString(mFreqIntervalsForTable.get(position).getFrequency()));
        Log.d("dfsdf",Integer.toString(this.get(position).getFrequency()));
    }

    //todo test these methods

    @Override
    public boolean isContinuous() {
        sort();
        double minFreq = mFreqIntervalsForTable.get(0).getMin();
        int i = 0;

        double lastEndingFreq = mFreqIntervalsForTable.get(0).getMax();

        for (ExclusiveEndMixedFrequencyInterval freq: mFreqIntervalsForTable){
            if (lastEndingFreq != freq.getMin()) {
                return false;
            }

            lastEndingFreq = mFreqIntervalsForTable.get(i).getMax();
            i++;
        }
        return true;
    }

    @Override
    public boolean isOverlapping() {
        final boolean isMinInclusive = mFreqIntervalsForTable.get(0).isMinInclusive();
        final boolean isMaxInclusive = mFreqIntervalsForTable.get(0).isMaxInclusive();

        if (isContinuous()) {
            for (ExclusiveEndMixedFrequencyInterval freq: mFreqIntervalsForTable) {
                if (freq.isMinInclusive() != isMinInclusive || freq.isMaxInclusive() != isMaxInclusive) {
                    return true;
                }
            }
        } else if (isMinInclusive == isMaxInclusive) {
            return true;
        }

        return false;
    }

    private void sort() {
        Collections.sort(mFreqIntervalsForTable, new sortFrequencyIntervalsByMinInRange());
    }

    private class sortFrequencyIntervalsByMinInRange implements Comparator<ExclusiveEndMixedFrequencyInterval> {
        @Override
        public int compare(ExclusiveEndMixedFrequencyInterval a, ExclusiveEndMixedFrequencyInterval b) {
            return a.getMin() < b.getMin() ? - 1 : a.getMin() == b.getMin() ? 0 : 1;
        }
    }
}
