package app.goStat.util.Converters;

import java.util.List;

import app.goStat.model.DataPoint;

public class StatsticalListConverter {

    public double[] toDoubleArray(List<DataPoint> list) {
        double[] sample = new double[list.size()];

        for(int i =0; i < list.size();i++) {
            sample[i] = list.get(i).getValue().doubleValue();
        }
        return sample;
    }

}
