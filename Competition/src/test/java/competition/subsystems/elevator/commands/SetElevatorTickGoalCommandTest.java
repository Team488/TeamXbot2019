package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class SetElevatorTickGoalCommandTest extends BaseCompetitionTest {

    SetElevatorTickGoalCommand setElevatorTickGoalCommand;
    ElevatorSubsystem elevatorSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        setElevatorTickGoalCommand = this.injector.getInstance(SetElevatorTickGoalCommand.class);
        elevatorSubsystem = this.injector.getInstance(ElevatorSubsystem.class);
    }

    @Test

    public void testSetElevatorTickGoalCommandConstructor() {
        SetElevatorTickGoalCommand testElevatorTickGoalCommand = this.injector
                .getInstance(SetElevatorTickGoalCommand.class);
    }

    @Test
    public void testSetTickGoal() {
        assertEquals(0, setElevatorTickGoalCommand.tickGoal, 0.001);
        setElevatorTickGoalCommand.setGoal(100);
        assertEquals(100, setElevatorTickGoalCommand.tickGoal, 0.001);
    }

    @Test
    public void testSetElevatorTickGoalCommandInitialize() {
        setElevatorTickGoalCommand.initialize();
    }
    @Test
    public void testSetElevatorTickGoalCommandExecute() {
        assertEquals(0, elevatorSubsystem.getTickGoal(), 0.001);
        setElevatorTickGoalCommand.setGoal(100);
        setElevatorTickGoalCommand.execute();
        assertEquals(100, elevatorSubsystem.getTickGoal(), 0.001);

    }

}