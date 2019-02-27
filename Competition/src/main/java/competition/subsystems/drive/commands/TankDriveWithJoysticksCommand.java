package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.XPropertyManager;

public class TankDriveWithJoysticksCommand extends HeadingControlledDriveCommand {

    final OperatorInterface oi;

    @Inject
    public TankDriveWithJoysticksCommand(CommonLibFactory clf, XPropertyManager propMan, OperatorInterface oi, DriveSubsystem driveSubsystem) {
        super(clf, driveSubsystem, propMan);
        this.oi = oi;
    }

    @Override
    public void initialize() {
        super.initialize();
        log.info("Initializing");
    }

    @Override
    protected double getHumanTranslationInput() {
        return (oi.driverGamepad.getLeftVector().y + oi.driverGamepad.getRightVector().y) / 2;
    }

    @Override
    protected double getHumanRotationInput() {
        return (-oi.driverGamepad.getLeftVector().y + oi.driverGamepad.getRightVector().y) / 2;
    }
}
