package com.example.jgjio_desktop.gostats;

public class DataPoint {
    private Double dataPointValue;
    private boolean isEnabled;

    DataPoint(boolean isEnabled) {
        this.isEnabled = isEnabled;
        dataPointValue = null;
    }

    DataPoint(double value, boolean isEnabled) {
        dataPointValue = value;
        this.isEnabled = isEnabled;
    }

    DataPoint(double value) {
        dataPointValue = value;
        isEnabled = true;
    }

    public double get() {
        return dataPointValue;
    }

    public void set(double value) {
        dataPointValue = value;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void enable() {
        isEnabled = true;
    }

    public void disable() {
        isEnabled = false;
    }

    public String toString() {
        return Double.toString(dataPointValue);
    }

}
