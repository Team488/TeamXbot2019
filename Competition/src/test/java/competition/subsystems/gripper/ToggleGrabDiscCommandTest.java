package competition.subsystems.gripper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripper.GripperSubsystem.ToggleState;
import competition.subsystems.gripper.commands.ToggleGrabDiscCommand;

public class ToggleGrabDiscCommandTest extends BaseCompetitionTest {

    ToggleGrabDiscCommand toggleGrab;
    GripperSubsystem gripperSubsystem;
    @Override
    public void setUp() {
        super.setUp();
        toggleGrab = this.injector.getInstance(ToggleGrabDiscCommand.class);
        gripperSubsystem = this.injector.getInstance(GripperSubsystem.class);
    }

    @Test
    public void testGrabDisc() {
        assertEquals(ToggleState.GRAB, gripperSubsystem.getState());
        toggleGrab.initialize();
        assertEquals(ToggleState.RELEASE, gripperSubsystem.getState());
    }



}