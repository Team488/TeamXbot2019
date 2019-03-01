package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.commands.ConfigureDriveSubsystemCommand;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class ConfigureDriveSubsystemTest extends BaseCompetitionTest {
    
    DriveSubsystem drive;
    ConfigureDriveSubsystemCommand command;

    @Override
    public void setUp() {
        super.setUp();
        drive = this.injector.getInstance(DriveSubsystem.class);
        command = this.injector.getInstance(ConfigureDriveSubsystemCommand.class);
    }

    @Test
    public void testConfigureDriveSubsystem() {
        command.setMaxAmps(10);
        command.setSecondsFromNeutralToFull(5);

        command.initialize();
        command.execute();

        verifyMotorParameters(drive.leftMaster, 10, 5);
        verifyMotorParameters(drive.rightMaster, 10, 5);

        command.setMaxAmps(40);
        command.setSecondsFromNeutralToFull(0);

        command.initialize();
        command.execute();

        verifyMotorParameters(drive.leftMaster, 40, 0);
        verifyMotorParameters(drive.rightMaster, 40, 0);
    }

    private void verifyMotorParameters(XCANTalon talon, int maxCurrent, double timeToFull) {
        MockCANTalon mock = (MockCANTalon)talon;

        assertEquals(maxCurrent, mock.getContinuousCurrentLimit());
        assertEquals(timeToFull, mock.getOpenLoopRamp(), 0.001);
    }
}