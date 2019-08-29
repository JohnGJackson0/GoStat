package app.goStat.util;

import android.util.Log;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

import app.goStat.model.DataPoint;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.sqrt;

public class ZTestUtil {

    public static double getZ(double sampleMean, double hypothesizedPopulationMean, double standardDeviation,double sampleSize) {
        return (sampleMean-hypothesizedPopulationMean)/(standardDeviation/sqrt(sampleSize));
    }

    public double getPLessThan(double zValue) {
        return new NormalDistribution().cumulativeProbability(zValue);
    }

    public double getPMoreThan(double zValue) {
        return 1 - new NormalDistribution().cumulativeProbability(zValue);
    }

    public double getPTwoTailed(double zValue) {
        return 2*getPMoreThan(abs(zValue));
    }

    public double getZ(List<DataPoint> sampleData, double hypothesizedMean, double standardDeviation) {
        DescriptiveStatistics ds = new DescriptiveStatistics(toDoubleArray(sampleData));
        return getZ(ds.getMean(),hypothesizedMean,standardDeviation,ds.getN());
    }


    public double getPTwoTailed(List<DataPoint> sampleData, double hypothesizedMean, double standardDeviation) {
        return getPTwoTailed(getZ(sampleData,hypothesizedMean,standardDeviation));
    }

    public double getPMoreThan(List<DataPoint> sampleData, double hypothesizedMean, double standardDeviation) {
        return getPMoreThan(getZ(sampleData,hypothesizedMean,standardDeviation));
    }

    public double getPLessThan(List<DataPoint> sampleData, double hypothesizedMean, double standardDeviation) {
        return getPLessThan(getZ(sampleData,hypothesizedMean,standardDeviation));
    }

    private double[] toDoubleArray(List<DataPoint> list) {
        double[] sample = new double[list.size()];

        for(int i =0; i < list.size();i++) {
            sample[i] = list.get(i).getValue().doubleValue();
        }
        return sample;
    }

}
