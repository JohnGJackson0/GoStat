package app.goStat.util;


import android.provider.ContactsContract;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

import java.util.List;

import app.goStat.model.DataPoint;

import static java.lang.StrictMath.abs;

public final class TTestUtil {

    public static double getT(double meanOfSample, double hypothesisPopulationMean, double sampleSize, double sampleStandardDeviation) {
        return  (meanOfSample-hypothesisPopulationMean)/(sampleStandardDeviation/(Math.sqrt(sampleSize)));
    }

    public double getPLessThan(double DF, double tValue) {
        return new TDistribution(DF).cumulativeProbability(tValue);
    }

    public double getPMoreThan(double DF, double tValue) {
        return 1 - new TDistribution(DF).cumulativeProbability(tValue);
    }

    public double getPTwoTailed(double DF, double tValue) {
        return 2*getPMoreThan(DF,abs(tValue));
    }

    public double getT(List<DataPoint> sampleData, double hypothesisPopulationMean) {
        return new org.apache.commons.math3.stat.inference.TTest().t(hypothesisPopulationMean,toDoubleArray(sampleData));
    }

    public double getPTwoTailed(List<DataPoint> sampleData, double hypothesisPopulationMean) {
        return new TTest().tTest(hypothesisPopulationMean, toDoubleArray(sampleData));
    }

    //The one-tail P value is half the two-tail P value
    public double getPLessThan(List<DataPoint> sampleData, double hypothesisPopulationMean) {
        return getPTwoTailed(sampleData,hypothesisPopulationMean)/2;
    }

    public double getPMoreThan(List<DataPoint> sampleData, double hypothesisPopulationMean) {
        return 1-getPLessThan(sampleData, hypothesisPopulationMean);
    }


    //todo maybe just fetch as a new query and list id?
    private double[] toDoubleArray(List<DataPoint> list) {
        double[] sample = new double[list.size()];

        for(int i =0; i < list.size();i++) {
            sample[i] = list.get(i).getValue().doubleValue();
        }
        return sample;
    }



}
