package competition.subsystems.climber.climbercommands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber.RearMotorClimberSubsystem;
import competition.subsystems.climber.commands.HoldRobotLevelCommand;
import competition.subsystems.pose.PoseSubsystem;

public class HoldRobotLevelCommandTest extends BaseCompetitionTest {

    HoldRobotLevelCommand command;
    protected PoseSubsystem pose;
    protected RearMotorClimberSubsystem rear;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(HoldRobotLevelCommand.class);
        pose = injector.getInstance(PoseSubsystem.class);
        rear = injector.getInstance(RearMotorClimberSubsystem.class);
    }

    @Test
    public void testFlat() {
        command.initialize();
        command.execute();
        assertPower(0);
    }

    // Positive pitch means front falling. Negative pitch means front rising.
    @Test
    public void testFallingForward() {
        mockRobotIO.setGyroPitch(10);
        assertEquals(10, pose.getRobotPitch(), 0.001);

        command.initialize();
        command.execute();

        assertTryingToTiltBackward();
    }

    @Test
    public void testFallingBackward() {
        mockRobotIO.setGyroPitch(-10);
        assertEquals(-10, pose.getRobotPitch(), 0.001);

        command.initialize();
        command.execute();

        assertTryingToTiltForward();
    }

    // Positive roll means right lowering. Negative roll means right rising.
    @Test
    public void testFallingRight() {
        mockRobotIO.setGyroRoll(10);
        assertEquals(10, pose.getRobotRoll(), 0.001);

        command.initialize();
        command.execute();

        assertTryingToRollLeft();
    }

    @Test
    public void testFallingLeft() {
        mockRobotIO.setGyroRoll(-10);
        assertEquals(-10, pose.getRobotRoll(), 0.001);

        command.initialize();
        command.execute();

        assertTryingToRollRight();
    }

    protected void assertTryingToTiltForward() {
        assertTrue(rear.leftMotor.getMotorOutputPercent() > 0.1);
        assertTrue(rear.rightMotor.getMotorOutputPercent() > 0.1);
    }

    protected void assertTryingToTiltBackward() {
        assertTrue(rear.leftMotor.getMotorOutputPercent() < -0.1);
        assertTrue(rear.rightMotor.getMotorOutputPercent() < -0.1);
    }

    protected void assertTryingToRollLeft() {
        assertTrue(rear.leftMotor.getMotorOutputPercent() < -0.1);
        assertTrue(rear.rightMotor.getMotorOutputPercent() > 0.1);
    }

    protected void assertTryingToRollRight() {
        assertTrue(rear.leftMotor.getMotorOutputPercent() > 0.1);
        assertTrue(rear.rightMotor.getMotorOutputPercent() < -0.1);
    }

    protected void assertPower(double power) {
        assertEquals(power, rear.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(power, rear.rightMotor.getMotorOutputPercent(), 0.001);
    }
}