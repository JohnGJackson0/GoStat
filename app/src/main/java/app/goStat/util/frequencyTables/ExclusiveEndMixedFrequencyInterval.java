package app.goStat.util.frequencyTables;

public final class ExclusiveEndMixedFrequencyInterval implements Interval {
    private final double mMin;
    private final double mMax;
    private int mFrequency;

    public ExclusiveEndMixedFrequencyInterval(int frequency, double min, double max){
        mMin = min;
        mMax = max;
        mFrequency = frequency;
    }

    @Override
    public String toString() {
        return "[" + Double.toString(mMin) + "," +
                Double.toString(mMax) + ")";
    }

    public double getMin() {
        return mMin;
    }

    public double getMax() {
        return mMax;
    }

    public int getFrequency() {
        return mFrequency;
    }

    public void setFrequency (int frequency) {
        mFrequency = frequency;
    }

    public void addAFrequency() {
        mFrequency++;
    }

    @Override
    public boolean isMinInclusive() {
        return true;
    }

    @Override
    public boolean isMaxInclusive() {
        return false;
    }
}
