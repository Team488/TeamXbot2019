package competition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertTrue(vision.isTargetInView());
        assertFalse(rumbleCommand.wasTargetInView);
        vision.handlePacket("190");
        rumbleCommand.execute();
    }

}