package competition.subsystems.gripper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripper.commands.ReleaseDiscCommand;

public class ReleaseDiscCommandTest extends BaseCompetitionTest {

    ReleaseDiscCommand releaseDisc;
    GripperSubsystem gripperSubsystem;
    @Override
    public void setUp() {
        super.setUp();
        releaseDisc = this.injector.getInstance(ReleaseDiscCommand.class);
        gripperSubsystem = this.injector.getInstance(GripperSubsystem.class);
    }

    @Test
    public void testGrabDisc() {
        gripperSubsystem.gripperPiston.setOn(true);
        assertTrue("releaseHatchet is true", gripperSubsystem.gripperPiston.getAdjusted());

        releaseDisc.initialize();

        assertFalse("releaseHatchet is false", gripperSubsystem.gripperPiston.getAdjusted());
    }



}