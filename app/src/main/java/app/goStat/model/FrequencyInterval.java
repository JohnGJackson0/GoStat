package app.goStat.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static org.apache.commons.math3.util.Precision.round;

@Entity(tableName = "frequency_interval",
        foreignKeys = @ForeignKey(entity = StatisticalList.class,
                parentColumns = "id",
                childColumns = "list_id",
                onDelete = ForeignKey.CASCADE))
public class FrequencyInterval {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int frequency;

    @ColumnInfo(name = "min_range")
    private double minRange;

    @ColumnInfo(name = "max_range")
    private double maxRange;

    @ColumnInfo(name = "list_id")
    private int listId;

    @ColumnInfo(name = "is_min_inclusive")
    private boolean isMinInclusive;

    @ColumnInfo(name = "is_max_inclusive")
    private boolean isMaxInclusive;

    public FrequencyInterval(int id, int frequency, double minRange, double maxRange, boolean isMinInclusive, boolean isMaxInclusive, int listId) {
        this.id = id;
        this.frequency = frequency;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.isMinInclusive = isMinInclusive;
        this.isMaxInclusive = isMaxInclusive;
        this.listId = listId;
    }

    public int getId() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public double getMinRange() {
        return minRange;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public int getListId() {
        return listId;
    }

    public boolean isMinInclusive() {
        return isMinInclusive;
    }

    public boolean isMaxInclusive() {
        return isMaxInclusive;
    }

    @Override
    public String toString() {
        String minRangeChar = (isMinInclusive) ? "[" : "(";
        String maxRangeChar = (isMaxInclusive) ? "]" : ")";

        return minRangeChar + Double.toString(round(minRange, 2)) + " , "
                + Double.toString(round(maxRange, 2)) + maxRangeChar;
    }

    public double getWidth() {
        return maxRange - minRange;
    }
}
