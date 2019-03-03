package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class StopElevatorCommandTest extends BaseCompetitionTest {

    StopElevatorCommand stopElevatorCommand;
    ElevatorSubsystem elevatorSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        stopElevatorCommand = this.injector.getInstance(StopElevatorCommand.class);
        elevatorSubsystem = this.injector.getInstance(ElevatorSubsystem.class);
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
        elevatorSubsystem.raise();
        timer.advanceTimeInSecondsBy(3);
        elevatorSubsystem.raise();
        assertEquals(1, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
        stopElevatorCommand.execute();
        assertEquals(0, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
    }

}