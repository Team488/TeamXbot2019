package competition.subsystems.climber.climbercommands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber.RearMotorClimberSubsystem;
import competition.subsystems.climber.commands.HoldRobotLevelCommand;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.controls.sensors.mock_adapters.MockGyro;

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
        rear.setPowerFactor(1);
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
        setPitch(10);
        assertEquals(10, pose.getRobotPitch(), 0.001);

        command.initialize();
        command.execute();

        assertTryingToTiltBackward();
    }

    @Test
    public void testFallingBackward() {
        setPitch(-10);
        assertEquals(-10, pose.getRobotPitch(), 0.001);

        command.initialize();
        command.execute();

        assertTryingToTiltForward();
    }

    // Positive roll means right lowering. Negative roll means right rising.
    @Test
    public void testFallingRight() {
        setRoll(10);
        assertEquals(10, pose.getRobotRoll(), 0.001);

        command.initialize();
        command.execute();

        assertTryingToRollLeft();
    }

    @Test
    public void testFallingLeft() {
        setRoll(-10);
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
        assertEquals(0.0, rear.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(0.0, rear.rightMotor.getMotorOutputPercent(), 0.001);

    }

    protected void assertTryingToRollLeft() {
        assertEquals(0.0, rear.leftMotor.getMotorOutputPercent(), 0.001);
        assertTrue(rear.rightMotor.getMotorOutputPercent() > 0.1);
    }

    protected void assertTryingToRollRight() {
        assertTrue(rear.leftMotor.getMotorOutputPercent() > 0.1);
        assertEquals(0.0, rear.rightMotor.getMotorOutputPercent(), 0.001);
    }

    protected void assertPower(double power) {
        assertEquals(power, rear.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(power, rear.rightMotor.getMotorOutputPercent(), 0.001);
    }

    protected void setPitch(double pitch) {
        ((MockGyro)pose.imu).setPitch(pitch);
    }

    protected void setRoll(double roll) {
        ((MockGyro)pose.imu).setRoll(roll);
    }
}
