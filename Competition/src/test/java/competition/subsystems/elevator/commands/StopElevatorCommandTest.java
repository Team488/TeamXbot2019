package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.elevator.BaseElevatorTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class StopElevatorCommandTest extends BaseElevatorTest {

    StopElevatorCommand stopElevatorCommand;

    @Override
    public void setUp() {
        super.setUp();
        stopElevatorCommand = this.injector.getInstance(StopElevatorCommand.class);
    }

    @Test
    public void testStopElevatorCommandConstructer() {
        StopElevatorCommand testStopElevatorCommand = this.injector.getInstance(StopElevatorCommand.class);
    }

    @Test
    public void testStopElevatorIntialize() {
        stopElevatorCommand.initialize();
    }

    @Test
    public void testStopElevatorExecute() {
        assertEquals(0, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
        makeElevatorRaiseHandlingRatchet();
        stopElevatorCommand.execute();
        assertEquals(0, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
    }

}