package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.commands.ForwardRatchetArcadeCommand;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class ForwardRatchetArcadeCommandTest extends BaseCompetitionTest {

    ForwardRatchetArcadeCommand command;
    DriveSubsystem drive;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(ForwardRatchetArcadeCommand.class);
        drive = injector.getInstance(DriveSubsystem.class);
    }

    @Test
    public void dontCrash() {
        command.initialize();
        command.execute();
    }

    @Test
    public void ratchet() {
        command.initialize();
        command.execute();
        verifyDrivePower(0, 0);

        changeDrivePosition(10000);
        command.execute();
        verifyDrivePower(0, 0);

        changeDrivePosition(-10000);
        //execute twice to clear D
        command.execute();
        command.execute();
        verifyDrivePower(drive.getPositionalPid().getMaxOutput(), drive.getPositionalPid().getMaxOutput());

        changeDrivePosition(10000);
        //execute twice to clear D
        command.execute();
        command.execute();
        verifyDrivePower(0, 0);
    }

    private void verifyDrivePower(double left, double right) {
        assertEquals(left, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(right, drive.rightMaster.getMotorOutputPercent(), 0.001);
    }

    private void changeDrivePosition(double delta) {
        ((MockCANTalon)drive.leftMaster).setPosition((int)(drive.leftMaster.getSelectedSensorPosition(0) + delta));
        ((MockCANTalon)drive.rightMaster).setPosition((int)(drive.rightMaster.getSelectedSensorPosition(0) + delta));
    }
}