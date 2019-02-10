package competition.subsystems.gripper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripper.commands.GrabDiscCommand;

public class GrabDiscCommandTest extends BaseCompetitionTest {

    GrabDiscCommand grabDisc;
    GripperSubsystem gripperSubsystem;
    @Override
    public void setUp() {
        super.setUp();
        grabDisc = this.injector.getInstance(GrabDiscCommand.class);
        gripperSubsystem = this.injector.getInstance(GripperSubsystem.class);
    }

    @Test
    public void testGrabDisc() {
        gripperSubsystem.gripperDiscPiston.setOn(false);
        assertFalse("grabHatchet is false", gripperSubsystem.gripperDiscPiston.getAdjusted());

        grabDisc.initialize();

        assertTrue("grabHatchet is true", gripperSubsystem.gripperDiscPiston.getAdjusted());
    }



}