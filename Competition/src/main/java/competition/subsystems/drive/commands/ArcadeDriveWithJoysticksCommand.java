package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.control_logic.HeadingAssistModule;

public class ArcadeDriveWithJoysticksCommand extends BaseCommand {

    final DriveSubsystem driveSubsystem;
    final OperatorInterface oi;
    final DoubleProperty deadbandProp;
    final HeadingAssistModule ham;

    @Inject
    public ArcadeDriveWithJoysticksCommand(OperatorInterface oi, DriveSubsystem driveSubsystem, 
            XPropertyManager propMan, CommonLibFactory clf) {
        this.oi = oi;
        this.driveSubsystem = driveSubsystem;
        this.requires(this.driveSubsystem);
        deadbandProp = propMan.createPersistentProperty(getPrefix() + "Deadband", 0.05);
        ham = clf.createHeadingAssistModule(
            clf.createHeadingModule(driveSubsystem.getRotateToHeadingPid()),
            clf.createHeadingModule(driveSubsystem.getRotateDecayPid()));
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        ham.reset();
    }

    @Override
    public void execute() {
        double translation = oi.driverGamepad.getLeftVector().y;
        double rotation = MathUtils.squareAndRetainSign(oi.driverGamepad.getRightVector().x);

        rotation = ham.calculateHeadingPower(rotation);

        double left = translation - rotation;
        double right = translation + rotation;

        driveSubsystem.drive(left, right);
    }

}