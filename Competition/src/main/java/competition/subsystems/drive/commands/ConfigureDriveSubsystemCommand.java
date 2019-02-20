package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;

public class ConfigureDriveSubsystemCommand extends BaseCommand {

    private final BaseDriveSubsystem baseDrive;

    private final DoubleProperty maxAmps;
    private final DoubleProperty secondsFromNeutralToFull;

    @Inject
    public ConfigureDriveSubsystemCommand(BaseDriveSubsystem baseDrive, XPropertyManager propManager) {
        this.baseDrive = baseDrive;
        maxAmps = propManager.createPersistentProperty(getPrefix() +"Max Current In Amps", 39);
        secondsFromNeutralToFull = propManager.createPersistentProperty(getPrefix() +"Seconds From Neutral To Full",
                0.2);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        int maxAmpsInt = (int) maxAmps.get();
        baseDrive.setCurrentLimits(maxAmpsInt, true);
        baseDrive.setVoltageRamp(secondsFromNeutralToFull.get());
    }

    public double getMaxAmps() {
        return maxAmps.get();
    }

    public double getSeconds() {
        return secondsFromNeutralToFull.get();
    }

}