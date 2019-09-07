package app.goStat.view.functions.functionFragments.TwoSampleTTestDistribution;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;

import java.util.List;

import app.goStat.model.DataPoint;
import app.goStat.util.Converters.StatisticalListConverter;
import app.goStat.util.StatisticsTests.TTestUtil;

abstract class NotPooledVarianceDistributionTest implements TDistributionTest{
    TTestData data;

    NotPooledVarianceDistributionTest(TTestData data) {
        this.data = data;
    }

    NotPooledVarianceDistributionTest(List<DataPoint> list1, List<DataPoint> list2) {
        StatisticalListConverter listConverter = new StatisticalListConverter();

        double[] listOne = listConverter.toDoubleArray(list1);
        double[] listTwo = listConverter.toDoubleArray(list2);

        SummaryStatistics summaryListOne = new SummaryStatistics();
        SummaryStatistics summaryListTwo = new SummaryStatistics();

        for( int i = 0; i < listOne.length; i++) {
            summaryListOne.addValue(listOne[i]);
        }

        for( int i = 0; i < listTwo.length; i++) {
            summaryListTwo.addValue(listTwo[i]);
        }

        data = new TTestData(summaryListOne.getN(), summaryListOne.getMean(), summaryListOne.getStandardDeviation(),
                summaryListTwo.getN(), summaryListTwo.getMean(), summaryListTwo.getStandardDeviation());
    }

    public double getT() {
        return new TTestUtil().getTOfNotPooledTwoSampleTTest(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
    }
    public abstract double getP();
    @Override
    public String toString() {
        return "Output: \n\n" +
                "t = " + getT() + "\n" +
                "p = " + getP() + "\n\n" +
                "Input: \n\n" +
                data.toString() + "\n" +
                "pooled = no";
    }
}
