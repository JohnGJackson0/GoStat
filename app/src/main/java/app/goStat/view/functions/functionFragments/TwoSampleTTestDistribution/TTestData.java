package app.goStat.view.functions.functionFragments.TwoSampleTTestDistribution;

public class TTestData {
    protected double n1;
    protected double xBar1;
    protected double sX1;
    protected double n2;
    protected double xBar2;
    protected double sX2;

    TTestData(double n1,double xBar1, double sX1, double n2, double xBar2,double sX2){
        this.n1 = n1;
        this.sX1 =sX1;
        this.xBar1=xBar1;
        this.n2 = n2;
        this.sX2 = sX2;
        this.xBar2 = xBar2;
    }

    @Override
    public String toString() {
        return "n1: " + n1 + "\n"
                + "sX1: " + sX1 + "\n"
                + "x̄1: " + xBar1 + "\n"
                + "n2: " + n2 + "\n"
                + "sX2: " + sX2 + "\n"
                + "x̄2: " + xBar2 + "\n";
    }
}
