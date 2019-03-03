package competition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import competition.subsystems.vision.VisionSubsystem;

public class RumbleManagerCommandTest extends BaseCompetitionTest {

    RumbleManagerCommand rumbleCommand;
    VisionSubsystem vision;

    @Override
    public void setUp() {
        super.setUp();
        this.rumbleCommand = injector.getInstance(RumbleManagerCommand.class);
        this.vision = injector.getInstance(VisionSubsystem.class);
    }

    @Test
    public void test() {
        // TODO: Add abilty to test RumbleManager so we can see if the rumble command is sent
        // correctly or not
        rumbleCommand.initialize();
        assertFalse(vision.isTargetInView());
        rumbleCommand.execute();
        assertFalse(rumbleCommand.wasTargetInView);
        vision.handlePacket("{\"hasTarget\":true, \"yaw\": 10}");
        assertTrue(vision.isTargetInView());
        rumbleCommand.execute();
        assertTrue(rumbleCommand.wasTargetInView);
    }

}