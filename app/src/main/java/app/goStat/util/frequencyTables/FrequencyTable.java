package app.goStat.util.frequencyTables;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FrequencyTable {
    List<ExclusiveEndMixedFrequencyInterval> mFreqIntervalsForTable;

    public FrequencyTable(List<ExclusiveEndMixedFrequencyInterval> freqIntervalsForTable) {
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
