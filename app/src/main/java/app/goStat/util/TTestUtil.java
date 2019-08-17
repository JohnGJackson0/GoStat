package app.goStat.util;


import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

import java.util.List;

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

    public double getT(double[] sample, double hypothesisPopulationMean) {
        return new org.apache.commons.math3.stat.inference.TTest().t(hypothesisPopulationMean,sample);
    }

    public double getPTwoTailed(double[] sample, double hypothesisPopulationMean) {
        return new TTest().tTest(hypothesisPopulationMean, sample);
    }

    //The one-tail P value is half the two-tail P value
    public double getPLessThan(double[] sample, double hypothesisPopulationMean) {
        return getPTwoTailed(sample,hypothesisPopulationMean)/2;
    }

    public double getPMoreThan(double[] sample, double hypothesisPopulationMean) {
        return 1-getPLessThan(sample, hypothesisPopulationMean);
    }



}
