package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.MockDigitalInput;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class ElevatorMaintainerCommandTest extends BaseCompetitionTest {

    ElevatorMaintainerCommand elevatorMaintainerCommand;
    ElevatorSubsystem elevatorSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        elevatorMaintainerCommand = this.injector.getInstance(ElevatorMaintainerCommand.class);
        elevatorSubsystem = this.injector.getInstance(ElevatorSubsystem.class);
    }

    @Test
    public void testElevatorMaintainerCommandConstructor() {
        ElevatorMaintainerCommand elevatorMaintainerCommandTest = this.injector
                .getInstance(ElevatorMaintainerCommand.class);
    }

    @Test
    public void testElevatorMaintainerCommandInitialize() {
        elevatorMaintainerCommand.initialize();
    }

    @Test
    public void testElevatorMaintainerCommandExecute() {
        assertEquals(0, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
        assertEquals(0, elevatorSubsystem.getTickGoal(), 0.001);
        assertEquals(0, elevatorSubsystem.getElevatorHeightInRawTicks(), 0.001);
        assertEquals(0, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
        elevatorSubsystem.setTickGoal(100);
        ((MockCANTalon) elevatorSubsystem.master).setPosition(100);
        assertFalse(elevatorSubsystem.isCalibrationSensorPressed());
        ((MockDigitalInput) elevatorSubsystem.calibrationSensor).setValue(true);
        assertEquals(100, elevatorSubsystem.getTickGoal(), 0.001);
        assertEquals(100, elevatorSubsystem.getElevatorHeightInRawTicks(), 0.001);
        assertTrue(elevatorSubsystem.isCalibrationSensorPressed());
        elevatorMaintainerCommand.execute();
        timer.advanceTimeInSecondsBy(3);
        elevatorMaintainerCommand.execute();
        assertEquals(elevatorSubsystem.master.getMotorOutputPercent(), elevatorSubsystem.master.getMotorOutputPercent(), 0.001);

    }

}