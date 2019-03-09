package app.goStat;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.math.BigDecimal;

@Entity(tableName = "data_point",
        foreignKeys = @ForeignKey(entity = StatisticalList.class,
                parentColumns = "id",
                childColumns = "list_id",
                onDelete = ForeignKey.CASCADE))

@TypeConverters({BigDecimalConverter.class})
public class DataPoint {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "list_id")
    private int listId;

    private boolean isEnabled;

    private BigDecimal value;

    public DataPoint(int listId, boolean isEnabled, BigDecimal value) {
        this.listId = listId;
        this.isEnabled = isEnabled;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getListId() {
        return listId;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
