package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.commands.ConfigureDriveSubsystemCommand;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;

public class ConfigureDriveSubsystemTest extends BaseCompetitionTest {
    
    BaseDriveSubsystem baseDrive;
    ConfigureDriveSubsystemCommand command;
    XPropertyManager propManager;

    @Override
    public void setUp() {
        super.setUp();
        baseDrive = this.injector.getInstance(BaseDriveSubsystem.class);
        command = this.injector.getInstance(ConfigureDriveSubsystemCommand.class);
        propManager = this.injector.getInstance(XPropertyManager.class);
    }

    @Test
    public void testConfigureDriveSubsystem() {
        assertEquals(39, command.getMaxAmps(), 0.001);
        assertEquals(0.2, command.getSeconds(), 0.001);
        command.execute();
        propManager.randomAccessStore.setDouble(command.getPrefix() + "Max Current In Amps", 37);
        propManager.randomAccessStore.setDouble(command.getPrefix() + "Seconds From Neutral To Full", 0.1);
        assertEquals(37, command.getMaxAmps(), 0.001);
        assertEquals(0.1, command.getSeconds(), 0.001);
    }
}