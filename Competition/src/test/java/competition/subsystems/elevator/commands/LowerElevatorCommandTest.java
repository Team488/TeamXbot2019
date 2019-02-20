package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class LowerElevatorCommandTest extends BaseCompetitionTest {

    LowerElevatorCommand lowerElevatorCommand;
    ElevatorSubsystem elevatorSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        lowerElevatorCommand = this.injector.getInstance(LowerElevatorCommand.class);
        elevatorSubsystem = this.injector.getInstance(ElevatorSubsystem.class);
    }

    @Test
    public void testLowerElevatorCommandConstructer() {
        LowerElevatorCommand testLowerElevatorCommand = this.injector.getInstance(LowerElevatorCommand.class);
    }

    @Test
    public void testLowerElevatorIntialize() {
        lowerElevatorCommand.initialize();
    }

    @Test
    public void testLowerElevatorExecute() {
        assertEquals(0, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
        lowerElevatorCommand.execute();
        assertEquals(-1, elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
    }

}