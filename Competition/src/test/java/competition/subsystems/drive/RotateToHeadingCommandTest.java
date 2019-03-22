package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.pose.PoseSubsystem;

public class RotateToHeadingCommandTest extends BaseCompetitionTest {

    DriveSubsystem drive;
    PoseSubsystem pose;
    RotateToHeadingCommand command;

    @Override
    public void setUp() {
        super.setUp();

        drive = injector.getInstance(DriveSubsystem.class);
        pose = injector.getInstance(PoseSubsystem.class);
        command = injector.getInstance(RotateToHeadingCommand.class);
    }

    @Test
    public void testInitialize() {
        command.initialize();
    }

    @Test
    public void testRotatesLeft() {
        assertEquals(0, drive.rightMaster.getMotorOutputPercent(), 0.001);

        command.setHeadingGoal(90, true);
        command.initialize();
        command.execute();

        assertEquals(drive.getRotateToHeadingPid().getMaxOutput(), drive.rightMaster.getMotorOutputPercent(), 0.001);
    }

    @Test
    public void testFinishes() {
        assertEquals(0, drive.rightMaster.getMotorOutputPercent(), 0.001);
        assertFalse(command.isFinished());

        command.setHeadingGoal(90, true);
        command.initialize();
        command.execute();

        assertFalse(command.isFinished());
        assertEquals(drive.getRotateToHeadingPid().getMaxOutput(), drive.rightMaster.getMotorOutputPercent(), 0.001);
        changeMockGyroHeading(90);

        // Once for error to stabilize to 0
        command.execute();
        // Again for the derivative to stabilize to 0
        command.execute();
        // Then let time pass for the time stability check
        timer.advanceTimeInSecondsBy(3);
        command.execute();

        assertEquals(0, drive.rightMaster.getMotorOutputPercent(), 0.001);
        assertTrue(command.isFinished());
    }

    protected void changeMockGyroHeading(double delta) {
        double oldHeading = mockRobotIO.getGyroHeading();
        double newHeading = oldHeading + delta;
        setMockGyroHeading(newHeading);
    }

    protected void setMockGyroHeading(double heading) {
        mockRobotIO.setGyroHeading(heading);
    }

}