package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.properties.PropertyFactory;

public class CheesyDriveWithJoysticksCommand extends HeadingControlledDriveCommand {

    final OperatorInterface oi;

    @Inject
    public CheesyDriveWithJoysticksCommand(CommonLibFactory clf, PropertyFactory propFactory, OperatorInterface oi, DriveSubsystem driveSubsystem) {
        super(clf, driveSubsystem, propFactory);
        this.oi = oi;
    }

    @Override
    public void initialize() {
        super.initialize();
        log.info("Initializing");
    }

    @Override
    protected double getHumanTranslationInput() {
        return oi.driverGamepad.getLeftVector().y;
    }

    @Override
    protected double getHumanRotationInput() {
        return MathUtils.squareAndRetainSign(oi.driverGamepad.getRightVector().x);
    }

    @Override
    public void execute() {
        drive.cheesyDrive(getTranslation(), getRotation());
    }


}