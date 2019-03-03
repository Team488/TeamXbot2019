package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class RaiseElevatorCommandTest extends BaseCompetitionTest {

    RaiseElevatorCommand raiseElevatorCommand;
    ElevatorSubsystem elevatorSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        raiseElevatorCommand = this.injector.getInstance(RaiseElevatorCommand.class);
        elevatorSubsystem = this.injector.getInstance(ElevatorSubsystem.class);
    }

    @Test
    public void testRaiseElevatorCommandConstructer() {
        RaiseElevatorCommand testRaiseElevatorCommand = this.injector.getInstance(RaiseElevatorCommand.class);
    }

    @Test
    public void testRaiseElevatorIntialize() {
        raiseElevatorCommand.initialize();
    }

    @Test
    public void testRaiseElevatorExecute() {
        assertEquals(0, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
        raiseElevatorCommand.execute();
        timer.advanceTimeInSecondsBy(3);
        raiseElevatorCommand.execute();
        assertEquals(1, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
    }

}