package app.goStat.util.StatisticsTests;


import android.provider.ContactsContract;
import android.util.Log;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

import java.util.List;

import app.goStat.model.DataPoint;

import static java.lang.StrictMath.abs;

public final class TTestUtil extends TTest{
    /*
    * IMPLEMENTED method variations:
    *
    * one sample:
    * t w/ stats
    * t w/ data
    * less than p w/ stats
    * more than p w/ stats
    * two tailed p w/ stats
    * less than p w/ data
    * more than p w/ data
    * two tailed p w/ data
    *
    * two sample:
    * t w/ pooled stats
    * t w/ not pooled stats
    * p w/ less than pooled stats
    * p w/ more than pooled stats
    * p w/ two tailed pooled stats
    * p w/ less than not-pooled stats
    * p w/ more than not-pooled stats
    * p w/ two tailed not-pooled stats
    *
    * NOT IMPLEMENTED METHOD VARIATIONS:
    *
    * The code gets to these answers through the use of
    * converting the data to stats and calling the stat methods
    *
    * p w/ less than pooled data
    * p w/ more than pooled data
    * p w/ two tailed pooled data
    * p w/ less than not-pooled data
    * p w/ more than not-pooled data
    * p w/ two tailed not-pooled data
     */

    //t value one sample

    public static double getT(double meanOfSample, double hypothesisPopulationMean, double sampleSize, double sampleStandardDeviation) {
        return  (meanOfSample-hypothesisPopulationMean)/(sampleStandardDeviation/(Math.sqrt(sampleSize)));
    }

    public double getT(List<DataPoint> sampleData, double hypothesisPopulationMean) {
        return new TTest().t(hypothesisPopulationMean,toDoubleArray(sampleData));
    }

    //p value one sample

    public double getPLessThan(double DF, double tValue) {
        return new TDistribution(DF).cumulativeProbability(tValue);
    }

    public double getPMoreThan(double DF, double tValue) {
        return 1 - new TDistribution(DF).cumulativeProbability(tValue);
    }

    public double getPTwoTailed(double DF, double tValue) {
        return 2*getPMoreThan(DF,abs(tValue));
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

    //t-value two sample

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

    //p-value two sample

    public double getPOfNotPooledTwoSampleTTestTwoTailed(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        //sX1 = sqrt(variance1) therefore, variance1 = Sx1^2 ...
        return super.tTest(xBar1,xBar2,sX1*sX1, sX2*sX2,n1,n2);
    }
    public double getPOfNotPooledTwoSampleTTestLessThan(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        return getPOfNotPooledTwoSampleTTestTwoTailed(xBar1, xBar2, sX1, sX2, n1, n2)/2;
    }
    public double getPOfNotPooledTwoSampleTTestMoreThan(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        return 1-(getPOfNotPooledTwoSampleTTestTwoTailed(xBar1, xBar2, sX1, sX2, n1, n2)/2);
    }
    public double getPOfPooledTwoSampleTTestTwoTailed(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        return super.homoscedasticTTest(xBar1,xBar2,sX1*sX1, sX2*sX2,n1,n2);
    }
    public double getPOfPooledTwoSampleTTestLessThan(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        return getPOfPooledTwoSampleTTestTwoTailed(xBar1, xBar2, sX1, sX2, n1, n2)/2;
    }
    public double getPOfPooledTwoSampleTTestMoreThan(double xBar1, double xBar2, double sX1, double sX2, double n1, double n2) {
        return 1-getPOfPooledTwoSampleTTestTwoTailed(xBar1, xBar2, sX1, sX2, n1, n2)/2;
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
