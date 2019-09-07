package app.goStat.util.StatisticsTests;


import android.provider.ContactsContract;
import android.util.Log;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

import java.util.List;

import app.goStat.model.DataPoint;

import static java.lang.StrictMath.abs;

public final class TTestUtil extends TTest{

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
        return new TTest().t(hypothesisPopulationMean,toDoubleArray(sampleData));
    }

    //The one-tail P value is half the two-tail P value
    public double getPLessThan(List<DataPoint> sampleData, double hypothesisPopulationMean) {
        return getPTwoTailed(sampleData,hypothesisPopulationMean)/2;
    }

    public double getPTwoTailed(List<DataPoint> sampleData, double hypothesisPopulationMean) {
        return new TTest().tTest(hypothesisPopulationMean, toDoubleArray(sampleData));
    }

    public double getPMoreThan(List<DataPoint> sampleData, double hypothesisPopulationMean) {
        return 1-getPTwoTailed(sampleData, hypothesisPopulationMean)/2;
    }

    //TWO SAMPLE T TESTS ARE POOLED OR NOT POOLED

    //in apache for pooled test use methods starting with homoscedasticT
    //and for not pooled tests methods start with t
    public double getTOfPooledTwoSampleTTest(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        //sX1 = sqrt(variance1) therefore, variance1 = Sx1^2 ...
        return super.homoscedasticT(xBar1,xBar2,sX1*sX1, sX2*sX2,n1,n2);
    }
    public double getTOfNotPooledTwoSampleTTest(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        //sX1 = sqrt(variance1) therefore, variance1 = Sx1^2 ...
        return super.t(xBar1,xBar2,sX1*sX1, sX2*sX2,n1,n2);
    }

    public double getPOfNotPooledTwoSampleTTestTwoTailed(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        //sX1 = sqrt(variance1) therefore, variance1 = Sx1^2 ...
        return super.tTest(xBar1,xBar2,sX1*sX1, sX2*sX2,n1,n2);
    }
    public double getPOfNotPooledTwoSampleTTestLessThan(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        return 1-getPOfNotPooledTwoSampleTTestTwoTailed(xBar1, xBar2, sX1, sX2, n1, n2);
    }
    public double getPOfNotPooledTwoSampleTTestMoreThan(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        return getPOfNotPooledTwoSampleTTestTwoTailed(xBar1, xBar2, sX1, sX2, n1, n2)/2;
    }

    public double getPOfPooledTwoSampleTTestTwoTailed(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        return super.homoscedasticTTest(xBar1,xBar2,sX1*sX1, sX2*sX2,n1,n2);
    }
    public double getPOfPooledTwoSampleTTestLessThan(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        return 1-getPOfPooledTwoSampleTTestTwoTailed(xBar1, xBar2, sX1, sX2, n1, n2);
    }
    public double getPOfPooledTwoSampleTTestMoreThan(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        return getPOfPooledTwoSampleTTestTwoTailed(xBar1, xBar2, sX1, sX2, n1, n2)/2;
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
