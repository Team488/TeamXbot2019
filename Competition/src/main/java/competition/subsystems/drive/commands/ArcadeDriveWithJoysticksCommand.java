package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;

public class ArcadeDriveWithJoysticksCommand extends BaseCommand {

    final DriveSubsystem driveSubsystem;
    final OperatorInterface oi;

    @Inject
    public ArcadeDriveWithJoysticksCommand(OperatorInterface oi, DriveSubsystem driveSubsystem) {
        this.oi = oi;
        this.driveSubsystem = driveSubsystem;
        this.requires(this.driveSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double translation = oi.gamepad.getLeftVector().y;
        double rotation = MathUtils.squareAndRetainSign(oi.gamepad.getRightVector().x);

        double left = translation - rotation;
        double right = translation + rotation;

        driveSubsystem.drive(left, right);
    }

}