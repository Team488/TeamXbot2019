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
    public void testExecute() {
        vision.handlePacket("{\"hasTarget\":true, \"yaw\": 10}");
        assertTrue(vision.isTargetInView());
        assertFalse(rumbleCommand.wasTargetInView);
        vision.handlePacket("{\"hasTarget\":false, \"yaw\": 10}");
        rumbleCommand.execute();
    }

}