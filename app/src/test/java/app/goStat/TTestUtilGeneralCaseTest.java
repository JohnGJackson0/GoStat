package app.goStat;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import app.goStat.model.DataPoint;
import app.goStat.util.StatisticsTests.TTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

//Does one general type test for every method in the TTestUtil class

public class TTestUtilGeneralCaseTest {
    List<DataPoint> generalDataSet1;
    List<DataPoint> generalDataSet2;
    double hypothesizedMeanGeneral;
    double hypothesizedMeanGeneral2;
    private static final double DELTA_OF_STANDARD_ALLOWABLE_ERROR = 1e-5;
    TTestUtil tTest = new TTestUtil();

    @Before
    public void setup() {
        generalDataSet1 = new ArrayList<>();
        generalDataSet2 = new ArrayList<>();

        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(1.56)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(3.65)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(2.59)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(1.22)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(4.56)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(3.25)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(1.58)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(1.98)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(4.55)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(5.25)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(1.10)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(0.57)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(2.56)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(3.56)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(1.55)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(6.98)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(1.50)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(.25)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(.33)));
        generalDataSet1.add(new DataPoint(-1, true, BigDecimal.valueOf(.25)));

        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(3.99)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(4.83)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(.72)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(.697)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(1.589)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(2.56)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(2.99)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(3.52)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(.02)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(1.58)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(1.88)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(3.47)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(2.98)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(1.58)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(1.56)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(2.99)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(3.89)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(4.88)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(4.68)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(4.66)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(1.88)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(2.56)));
        generalDataSet2.add(new DataPoint(-2, true, BigDecimal.valueOf(3.99)));

        hypothesizedMeanGeneral = 2.55;
        hypothesizedMeanGeneral2= 2.99;
    }

    private double getGeneralCaseTValue() {
        return tTest.getT(generalDataSet1,hypothesizedMeanGeneral);
    }

    /* ONE SAMPLE */

    @Test
    public void getTStats_TTestUtil_negPoint260946() {
        assertEquals(tTest.getT(2.442,2.55,20,1.850918975),-.260946,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPGreaterThanDF_TTestUtil_point6015293772(){
        assertEquals(tTest.getPMoreThan(generalDataSet1.size()-1,getGeneralCaseTValue()),.601529,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPLessThanDF_TTestUtil_point3984706() {
        assertEquals(tTest.getPLessThan(generalDataSet1.size()-1,getGeneralCaseTValue()),.398470,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPTwoTailedDF_TTestUtil_point796941(){
        assertEquals(tTest.getPTwoTailed(generalDataSet1.size()-1,getGeneralCaseTValue()),.796941,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getTData_TTestUtil_negPoint2609464215(){
        assertEquals(getGeneralCaseTValue(),-.260946,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPLessThenData_TTestUtil_point3984706(){
        assertEquals(tTest.getPLessThan(generalDataSet1,2.55),.3984706228, DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPMoreThanData_TTestUtil_point6015293772(){
        assertEquals(tTest.getPMoreThan(generalDataSet1,2.55),.6015293772, DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPTwoTailedData_TTestUtil_point7969412456(){
        assertEquals(tTest.getPTwoTailed(generalDataSet1, 2.55),.7969412456, DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    /* TWO SAMPLE */

    /* actual data from L1,L2
    xbar1= 2.442
    sX1= 1.850918975
    n1= 20
    xbar2= 2.760695
    sX2= 1.426443
    n2= 23
    */

    //not pooled variations:
    @Test
    public void getTOfNotPooledTwoSampleTTestStats_TTestUtil_negPoint625298585(){
        assertEquals(tTest.getTOfNotPooledTwoSampleTTest(2.442,2.760695,1.850918975,1.426443,20,23),-.625298585,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPOfNotPooledTwoSampleTTestTwoTailedTestStats_TTestUtil_point5357729152() {
        assertEquals(tTest.getPOfNotPooledTwoSampleTTestTwoTailed(2.442,2.760695,1.850918975,1.426443,20,23),.53577929152,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPOfNotPooledTwoSampleTTestLessThanStats_TTestUtil_point2678864576(){
        assertEquals(tTest.getPOfNotPooledTwoSampleTTestLessThan(2.442,2.760695,1.850918975,1.426443,20,23),.2678864576,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPOfNotPooledTwoSampleTTestMoreThanStats_TTestUtil_point7321135424(){
        assertEquals(tTest.getPOfNotPooledTwoSampleTTestMoreThan(2.442,2.760695,1.850918975,1.426443,20,23),.7321135424,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    //pooled variations:
    @Test
    public void getTOfPooledTwoSampleTTestStats_TTestUtil_negPoint636794349(){
        assertEquals(tTest.getTOfPooledTwoSampleTTest(2.442,2.760695,1.850918975,1.426443,20,23),-.636794349,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPOfPooledTwoSampleTTestTwoTailedStats_TTestUtil_point5277984902(){
        assertEquals(tTest.getPOfPooledTwoSampleTTestTwoTailed(2.442,2.760695,1.850918975,1.426443,20,23),.5277984902,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPOfPooledTwoSampleTTestLessThanStats_TTestUtil_point2638992451() {
        assertEquals(tTest.getPOfPooledTwoSampleTTestLessThan(2.442,2.760695,1.850918975,1.426443,20,23),.2638992451,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPOfPooledTwoSampleTTestMoreThanStats_TTestUtil_point7361007549(){
        assertEquals(tTest.getPOfPooledTwoSampleTTestMoreThan(2.442,2.760695,1.850918975,1.426443,20,23),.7361007549,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }
}
