package com.example.jgjio_desktop.gostats;

import java.util.UUID;


public class DataPoint {
    private Double dataPointValue;
    private boolean isEnabled;
    private final UUID ID= UUID.randomUUID();

    DataPoint(boolean isEnabled) {
        this.isEnabled = isEnabled;
        dataPointValue = null;
    }

    DataPoint(Double value) {
        this.isEnabled = isEnabled;
        dataPointValue = value;
    }

    DataPoint(double value, boolean isEnabled) {
        dataPointValue = value;
        this.isEnabled = isEnabled;
    }

    DataPoint(double value) {
        dataPointValue = value;
        isEnabled = true;
    }

    public UUID getID() {
        return this.ID;
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
