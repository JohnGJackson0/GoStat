package app.goStat.util;


import org.apache.commons.math3.distribution.TDistribution;

import static java.lang.StrictMath.abs;

public final class TTest {

    public static double getT(double meanOfSample, double hypothesisValue, double sampleSize, double sampleStandardDeviation) {
        return  (meanOfSample-hypothesisValue)/(sampleStandardDeviation/(Math.sqrt(sampleSize)));
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
}
