package app.goStat;

public final class ExclusiveEndMixedFrequencyInterval implements Interval {
    private final double mMin;
    private final double mMax;
    private int mFrequency;

    ExclusiveEndMixedFrequencyInterval(int frequency, double min, double max){
        mMin = min;
        mMax = max;
        mFrequency = frequency;
    }

    @Override
    public String toString() {
        return "[" + Double.toString(mMin) + "," +
                Double.toString(mMax) + ")";
    }

    protected double getMin() {
        return mMin;
    }

    protected double getMax() {
        return mMax;
    }

    protected int getFrequency() {
        return mFrequency;
    }

    protected void setFrequency (int frequency) {
        mFrequency = frequency;
    }

    protected void addAFrequency() {
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
