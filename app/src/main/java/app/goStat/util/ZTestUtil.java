package app.goStat.util;

import org.apache.commons.math3.distribution.NormalDistribution;

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

}
