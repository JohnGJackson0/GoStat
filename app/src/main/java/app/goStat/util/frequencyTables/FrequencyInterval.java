package app.goStat.util.frequencyTables;

public interface FrequencyInterval {
    double getMin();
    double getMax();

    void setFrequency(int setFrequency);
    int getFrequency();
    void addOneFrequency();

    boolean isMinInclusive();
    boolean isMaxInclusive();
}
