package app.goStat.view.functions.functionFragments.TwoSampleTTestDistribution;

import app.goStat.util.StatisticsTests.TTestUtil;

public abstract class PooledVarianceDistrobutionTest implements TDistributionTest {
    TTestData data;

    PooledVarianceDistrobutionTest(TTestData data) {
        this.data = data;
    }

    public double getT() {
        return new TTestUtil().getTOfPooledTwoSampleTTest(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
    }

    @Override
    public String toString() {
        return "Output: \n\n" +
                "t = " + getT() + "\n" +
                "p = " + getP() + "\n\n" +
                "Input: \n" +
                data.toString() + "\n" +
                "pooled = yes";
    }

    public abstract double getP();
}
