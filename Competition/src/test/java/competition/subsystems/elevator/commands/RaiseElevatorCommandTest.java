package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.BaseElevatorTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class RaiseElevatorCommandTest extends BaseElevatorTest {

    RaiseElevatorCommand raiseElevatorCommand;

    @Override
    public void setUp() {
        super.setUp();
        raiseElevatorCommand = this.injector.getInstance(RaiseElevatorCommand.class);
    }

    @Test
    public void testRaiseElevatorIntialize() {
        raiseElevatorCommand.initialize();
    }

    @Test
    public void testRaiseElevatorExecute() {
        assertEquals(0, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
        raiseElevatorCommand.execute();
        setElevatorPositionForMovingUpwards();
        raiseElevatorCommand.execute();
        assertEquals(1*elevatorSubsystem.getMaximumPower(), elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
    }

}