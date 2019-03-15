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
    VisionSubsystem visionSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        rotateToVisionTargetCommand = injector.getInstance(RotateToVisionTargetCommand.class);
        driveSubsystem = injector.getInstance(DriveSubsystem.class);
        visionSubsystem = injector.getInstance(VisionSubsystem.class);
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
    public void testExecute() {
        assertEquals(0.0, rotateToVisionTargetCommand.rotation, 0.001);
        assertEquals(0.0, visionSubsystem.getAngleToTarget(), 0.001);
        visionSubsystem.handlePacket("{ \"yaw\":100, \"hasTarget\":true }");
        assertTrue(visionSubsystem.isTargetInView());
        rotateToVisionTargetCommand.execute();
        assertEquals(1, rotateToVisionTargetCommand.rotation, 0.001);
        assertEquals(100.0, visionSubsystem.getAngleToTarget(), 0.001);
    }

    @Test
    public void testFreeze() {
        rotateToVisionTargetCommand.setContinuousAcquisition(true);
        rotateToVisionTargetCommand.initialize();
        assertFalse(rotateToVisionTargetCommand.goalFrozen);

        // acquire target
        visionSubsystem.handlePacket("{ \"yaw\":100, \"hasTarget\":true }");
        rotateToVisionTargetCommand.execute();
        assertTrue(rotateToVisionTargetCommand.rotation > 0);

        // vision freaks out
        visionSubsystem.handlePacket("{ \"yaw\":-100, \"hasTarget\":true }");

        // should still be using the old target so the sign hasn't flipped
        rotateToVisionTargetCommand.execute();
        assertTrue(rotateToVisionTargetCommand.goalFrozen);
        assertTrue(rotateToVisionTargetCommand.rotation > 0);

        // make sure it stays frozen to the old goal after 3rd execute
        rotateToVisionTargetCommand.execute();
        assertTrue(rotateToVisionTargetCommand.goalFrozen);
        assertTrue(rotateToVisionTargetCommand.rotation > 0);
    }

    @Test
    public void testDontFreeze() {
        rotateToVisionTargetCommand.setContinuousAcquisition(true);
        rotateToVisionTargetCommand.initialize();
        assertFalse(rotateToVisionTargetCommand.goalFrozen);
        // acquire target
        visionSubsystem.handlePacket("{ \"yaw\":2, \"hasTarget\":true }");
        rotateToVisionTargetCommand.execute();
        assertTrue(rotateToVisionTargetCommand.rotation > 0);

        visionSubsystem.handlePacket("{ \"yaw\":-2, \"hasTarget\":true }");

        rotateToVisionTargetCommand.execute();
        assertFalse(rotateToVisionTargetCommand.goalFrozen);
        assertTrue(rotateToVisionTargetCommand.rotation < 0);
    }
}