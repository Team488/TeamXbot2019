package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.drive.BaseDriveSubsystem;

public class ConfigureDriveSubsystemCommand extends BaseCommand {

    private final BaseDriveSubsystem baseDrive;

    private final DoubleProperty maxAmps;
    private final DoubleProperty secondsFromNeutralToFull;

    @Inject
    public ConfigureDriveSubsystemCommand(BaseDriveSubsystem baseDrive, PropertyFactory propFactory) {
        this.baseDrive = baseDrive;
        propFactory.setPrefix(this);
        maxAmps = propFactory.createPersistentProperty("Max Current In Amps", 39);
        secondsFromNeutralToFull = propFactory.createPersistentProperty("Seconds From Neutral To Full", 0.2);
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

    public void setMaxAmps(int amps) {
        maxAmps.set(amps);
    }

    public void setSecondsFromNeutralToFull(double value) {
        secondsFromNeutralToFull.set(value);
    }
}