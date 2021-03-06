package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.subsystems.elevator.BaseElevatorTest;

public class LowerElevatorCommandTest extends BaseElevatorTest {

    LowerElevatorCommand lowerElevatorCommand;

    @Override
    public void setUp() {
        super.setUp();
        lowerElevatorCommand = this.injector.getInstance(LowerElevatorCommand.class);
    }

    @Test
    public void testLowerElevatorIntialize() {
        lowerElevatorCommand.initialize();
    }

    @Test
    public void testLowerElevatorExecute() {
        assertEquals(0, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
        lowerElevatorCommand.execute();
        timer.advanceTimeInSecondsBy(3);
        lowerElevatorCommand.execute();
        assertEquals(-1*elevatorSubsystem.getMaximumPower(), elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
    }

}