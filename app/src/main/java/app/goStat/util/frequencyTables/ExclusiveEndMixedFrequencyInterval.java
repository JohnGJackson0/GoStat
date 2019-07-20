package app.goStat.util.frequencyTables;

public final class ExclusiveEndMixedFrequencyInterval implements FrequencyInterval {
    private final double mMin;
    private final double mMax;
    private int mFrequency;

    public ExclusiveEndMixedFrequencyInterval(int initialFrequency, double min, double max){
        mMin = min;
        mMax = max;
        mFrequency = initialFrequency;
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

    @Override
    public void addOneFrequency() {
        mFrequency++;
    }

    public void setFrequency (int frequency) {
        mFrequency = frequency;
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
