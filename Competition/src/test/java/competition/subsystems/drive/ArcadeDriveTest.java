package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class ArcadeDriveTest extends BaseCompetitionTest {

    ArcadeDriveWithJoysticksCommand command;
    DriveSubsystem drive;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(ArcadeDriveWithJoysticksCommand.class);
        drive = injector.getInstance(DriveSubsystem.class);
    }

    @Test
    public void testSimple() {
        ((MockFTCGamepad)oi.driverGamepad).setLeftStick(new XYPair(0, 0.5));
        command.initialize();
        command.execute();

        assertEquals(0.5, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(0.5, drive.rightMaster.getMotorOutputPercent(), 0.001);
    }

    @Test
    public void testDeadband() {
        ((MockFTCGamepad)oi.driverGamepad).setLeftStick(new XYPair(0, 0.01));
        command.initialize();
        command.execute();

        assertEquals(0, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(0, drive.rightMaster.getMotorOutputPercent(), 0.001);
    }
}