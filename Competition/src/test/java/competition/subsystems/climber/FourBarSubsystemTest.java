package competition.subsystems.climber;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import competition.BaseCompetitionTest;

public class FourBarSubsystemTest extends BaseCompetitionTest {

    FourBarSubsystem fourBar;

    public void setUp(){
        super.setUp();
        fourBar = this.injector.getInstance(FourBarSubsystem.class);
    }

    public double getMasterPower() {
        return fourBar.master.getMotorOutputPercent();
    }

    @Test
    public void testConstructor() {
        FourBarSubsystem fourBarTest = this.injector.getInstance(FourBarSubsystem.class);
    }

    @Test
    public void testRaise() {
        assertEquals(0, getMasterPower(), 0.001);
        fourBar.deploy();
        assertEquals(1, getMasterPower(), 0.001);
    }

    @Test
    public void testLower() {
        assertEquals(0, getMasterPower(), 0.001);
        fourBar.retract();
        assertEquals(-1, getMasterPower(), 0.001);
    }

    @Test
    public void testStop(){
        testRaise();
        fourBar.stop();
        assertEquals(0, getMasterPower(), 0.001);
        testLower();
        fourBar.stop();
        assertEquals(0, getMasterPower(), 0.001);
    }

    @Test
    public void testSetPower() {
        assertEquals(0, getMasterPower(), 0.001);
        fourBar.setPower(1);
        assertEquals(1, getMasterPower(), 0.001);
        fourBar.setPower(-1);
        assertEquals(-1, getMasterPower(), 0.001);
    }

    @Test
    @Ignore
    public void testConfigSoftLimit() {
        FourBarSubsystem fourBarLimitTest = this.injector.getInstance(FourBarSubsystem.class);
        assertEquals(0, fourBarLimitTest.lowerLimit, 0.001);
        assertEquals(1000, fourBarLimitTest.upperLimit, 0.001);
    }

}