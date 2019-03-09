package competition.subsystems.elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import edu.wpi.first.wpilibj.MockDigitalInput;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class ElevatorSubsystemTest extends BaseCompetitionTest {

    ElevatorSubsystem elevatorSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        elevatorSubsystem = this.injector.getInstance(ElevatorSubsystem.class);
    }

    @Test
    public void testElevatorConstructor() {
        ElevatorSubsystem createElevatorSubsystem = this.injector.getInstance(ElevatorSubsystem.class);
    }

    @Test
    public void testStopElevator() {
        assertEquals(0, getMasterPower(), 0.001);
        elevatorSubsystem.raise();
        timer.advanceTimeInSecondsBy(3);
        elevatorSubsystem.raise();
        assertEquals(1*elevatorSubsystem.getMaximumPower(), getMasterPower(), 0.001);
        elevatorSubsystem.stop();
        assertEquals(0, getMasterPower(), 0.001);
    }

    @Test
    public void testRaiseElevator() {
        assertEquals(0, getMasterPower(), 0.001);
        elevatorSubsystem.raise();
        timer.advanceTimeInSecondsBy(3);
        elevatorSubsystem.raise();
        assertEquals(1*elevatorSubsystem.getMaximumPower(), getMasterPower(), 0.001);
    }

    @Test
    public void testLowerElevator() {
        assertEquals(0, getMasterPower(), 0.001);
        elevatorSubsystem.lower();
        timer.advanceTimeInSecondsBy(3);
        elevatorSubsystem.lower();
        assertEquals(-1*elevatorSubsystem.getMaximumPower(), getMasterPower(), 0.001);
    }

    @Test
    public void testIsCalibrationSensorPressed() {
        assertEquals(false, elevatorSubsystem.isCalibrationSensorPressed());
        ((MockDigitalInput) elevatorSubsystem.calibrationSensor).setValue(true);
        assertEquals(true, elevatorSubsystem.isCalibrationSensorPressed());
    }

    @Test
    public void testCalibrationSensorState() {
        assertEquals(false, elevatorSubsystem.getIsCalibrated());
        ((MockDigitalInput) elevatorSubsystem.calibrationSensor).setValue(true);
        elevatorSubsystem.setPower(0);
        assertEquals(true, elevatorSubsystem.getIsCalibrated());
    }

    @Test
    public void testGetElevatorHeightInTicks() {
        ((MockCANTalon) elevatorSubsystem.master).setPosition(100);
        assertEquals(100, elevatorSubsystem.getElevatorHeightInRawTicks(), 0.001);
        ((MockCANTalon) elevatorSubsystem.master).setPosition(0);
        assertEquals(0, elevatorSubsystem.getElevatorHeightInRawTicks(), 0.001);
    }

    @Test
    public void testGetCalibrationHeight() {
        // calibrationHeight is negative -1 as placeholder in ElevatorSubsystem.java
        assertEquals(0, elevatorSubsystem.getCalibrationHeight(), 0.001);
        ((MockDigitalInput) elevatorSubsystem.calibrationSensor).setValue(true);
        ((MockCANTalon)elevatorSubsystem.master).setPosition(1000);
        elevatorSubsystem.calibrate();
        assertEquals(1000, elevatorSubsystem.getCalibrationHeight(), 0.001);
    }

    @Test
    public void testSetPowerIntial() {
        assertEquals(0.0, getMasterPower(), 0.001);
        elevatorReady();
        testIsCalibrationSensorPressed();
        assertFalse(elevatorSubsystem.allowElevatorMotionSolenoid.getAdjusted());
    }

    @Test
    public void testSetPowerOne() {
        elevatorSubsystem.setPower(1);
        timer.advanceTimeInSecondsBy(3);
        elevatorSubsystem.setPower(1);
        elevatorReady();
        ((MockDigitalInput) elevatorSubsystem.calibrationSensor).setValue(false);
        testIsCalibrationSensorPressed();
        assertEquals(1*elevatorSubsystem.getMaximumPower(), getMasterPower(), 0.001);
        assertTrue(elevatorSubsystem.allowElevatorMotionSolenoid.getAdjusted());
    }

    @Test
    public void testSetPowerZero() {
        elevatorSubsystem.setPower(0);
        timer.advanceTimeInSecondsBy(3);
        elevatorSubsystem.setPower(0);
        elevatorReady();
        ((MockDigitalInput) elevatorSubsystem.calibrationSensor).setValue(false);
        testIsCalibrationSensorPressed();
        assertEquals(0, getMasterPower(), 0.001);
        assertFalse(elevatorSubsystem.allowElevatorMotionSolenoid.getAdjusted());
    }

    public void elevatorReady() {
        assertTrue(contract.isElevatorReady());
        assertTrue(contract.isElevatorLimitSwitchReady());
    }

    private double getMasterPower() {
        return elevatorSubsystem.master.getMotorOutputPercent();
    }

}