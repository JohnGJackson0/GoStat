package app.goStat;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import app.goStat.model.DataPoint;
import app.goStat.util.StatisticsTests.ZTestUtil;

import static org.junit.Assert.assertEquals;

public class ZTestUtilGeneralCaseTest {
    List<DataPoint> generalDataSet1;
    List<DataPoint> generalDataSet2;
    double hypothesizedMeanGeneral;
    double hypothesizedMeanGeneral2;
    private static final double DELTA_OF_STANDARD_ALLOWABLE_ERROR = 1e-5;
    ZTestUtil zTest = new ZTestUtil();

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

    /* actual data from L1,L2
    xbar1= 2.442
    sX1= 1.850918975
    n1= 20
    pop stnd 1.80409189
    xbar2= 2.760695
    sX2= 1.426443
    n2= 23
    */

    @Test
    public void getZStats_ZTestUtil_negativePoint2677195579(){
        assertEquals(zTest.getZ(2.442,2.55,1.80409189,20), -.2677195579,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPLessThan_zTestUtil_negativePoint3142660731(){
        assertEquals(zTest.getPLessThan(-.4837939042),.3142660731, DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPMoreThan_zTestUtil_point3583602348(){
        assertEquals(zTest.getPMoreThan(.3628454282),.3583602348, DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPMoreThan_zTestUtil_1(){
        assertEquals(zTest.getPMoreThan(-150.9436981),1, DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPTwoTailed_zTestUtil_point383474244(){
        assertEquals(zTest.getPTwoTailed(.8708290276),.3838474244, DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getZ_zTestUtil_15point51366284(){
        assertEquals(zTest.getZ(generalDataSet1, .54656,.5464), 15.51366284,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPTwoTailed_zTestUtil_point9258432954(){
        assertEquals(zTest.getPTwoTailed(generalDataSet1,.5897,89),.9258432954,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPMoreThan_zTestUtil_point0242416514(){
        assertEquals(zTest.getPTwoTailed(generalDataSet1,.236,5),.0242416514,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }

    @Test
    public void getPLessThan_zTestUtil_l1And2416854And5464(){
        assertEquals(zTest.getPLessThan(generalDataSet1,.549,9),.8265551409,DELTA_OF_STANDARD_ALLOWABLE_ERROR);
    }
}
