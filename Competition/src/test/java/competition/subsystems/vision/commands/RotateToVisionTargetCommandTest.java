package competition.subsystems.vision.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.vision.VisionSubsystem;

public class RotateToVisionTargetCommandTest extends BaseCompetitionTest {
    RotateToVisionTargetCommand rotateToVisionTargetCommand;
    DriveSubsystem driveSubsystem;
    VisionSubsystem visionSubsytem;

    @Override
    public void setUp() {
        super.setUp();
        rotateToVisionTargetCommand = injector.getInstance(RotateToVisionTargetCommand.class);
        driveSubsystem = injector.getInstance(DriveSubsystem.class);
        visionSubsytem = injector.getInstance(VisionSubsystem.class);
    }
    @Test
    public void testSetHeadingGoal() {
        rotateToVisionTargetCommand.givenGoal = 0.0;
        rotateToVisionTargetCommand.relative = true;
        assertEquals(0.0, rotateToVisionTargetCommand.givenGoal, 0.001);
        assertTrue(rotateToVisionTargetCommand.relative);
        rotateToVisionTargetCommand.setHeadingGoal(1.0, false);
        assertEquals(1.0, rotateToVisionTargetCommand.givenGoal, 0.001);
        assertFalse(rotateToVisionTargetCommand.relative);
    }

    @Test
    public void testConstructor() {
        RotateToVisionTargetCommand commandTest = injector.getInstance(RotateToVisionTargetCommand.class);
    }

    @Test
    public void testInitialize() {
        rotateToVisionTargetCommand.initialize();
    }

    @Test
    @Ignore
    public void testExecute() {
        assertEquals(0.0, rotateToVisionTargetCommand.rotation, 0.001);
        assertEquals(0.0, visionSubsytem.getAngleToTarget(), 0.001);
        visionSubsytem.handlePacket("{ \"targetYaw\":100 }");
        visionSubsytem.isTargetInView();
        rotateToVisionTargetCommand.execute();
        assertEquals(1, rotateToVisionTargetCommand.rotation, 0.001);
        assertEquals(100.0, visionSubsytem.getAngleToTarget(), 0.001);
    }

}