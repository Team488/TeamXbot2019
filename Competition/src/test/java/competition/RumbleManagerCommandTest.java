package competition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import competition.subsystems.vision.VisionSubsystem;

public class RumbleManagerCommandTest extends BaseCompetitionTest {

    RumbleManagerCommand rumbleCommand;
    RumbleManager rumbleManager;
    VisionSubsystem vision;

    @Override
    public void setUp() {
        super.setUp();
        this.rumbleCommand = injector.getInstance(RumbleManagerCommand.class);
        this.rumbleManager = injector.getInstance(RumbleManager.class);
        this.vision = injector.getInstance(VisionSubsystem.class);
    }

    @Test
    @Ignore
    public void test() {
        // TODO: Add abilty to test RumbleManager so we can see if the rumble command is
        // sent
        // correctly or not
        rumbleCommand.initialize();
        assertFalse(vision.isTargetInView());
        rumbleManager.updatePeriodicData();
        rumbleCommand.execute();
        assertFalse("Should not be rumbling yet", rumbleManager.getIsRumbling());

        vision.handlePacket("{\"hasTarget\":true, \"yaw\": 10}");
        rumbleManager.updatePeriodicData();
        rumbleCommand.execute();
        assertTrue("Now that target is in view, should be rumbling", rumbleManager.getIsRumbling());

        timer.advanceTimeInSecondsBy(0.3);
        vision.handlePacket("{\"hasTarget\":true, \"yaw\": 10}");
        rumbleManager.updatePeriodicData();
        rumbleCommand.execute();
        assertFalse("We should be in cooldown, no rumbling", rumbleManager.getIsRumbling());

        timer.advanceTimeInSecondsBy(0.75);
        vision.handlePacket("{\"hasTarget\":true, \"yaw\": 10}");
        rumbleManager.updatePeriodicData();
        rumbleCommand.execute();
        assertFalse("Even though cooldown over, no rumble since we have continuous acquisition",
                rumbleManager.getIsRumbling());

        vision.handlePacket("{\"hasTarget\":false, \"yaw\": 0}");
        rumbleManager.updatePeriodicData();
        rumbleCommand.execute();
        assertFalse("No vision target, no rumbling", rumbleManager.getIsRumbling());

        vision.handlePacket("{\"hasTarget\":true, \"yaw\": 22}");
        rumbleManager.updatePeriodicData();
        rumbleCommand.execute();
        assertTrue("Vision target back, rumble time!", rumbleManager.getIsRumbling());

    }

}