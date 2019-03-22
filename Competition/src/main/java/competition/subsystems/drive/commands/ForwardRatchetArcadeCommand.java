package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.PropertyFactory;

public class ForwardRatchetArcadeCommand extends ArcadeDriveWithJoysticksCommand {

    OperatorInterface oi;
    PoseSubsystem pose;

    double goalY;

    @Inject
    public ForwardRatchetArcadeCommand(CommonLibFactory clf, DriveSubsystem drive, PropertyFactory propFactory, OperatorInterface oi, PoseSubsystem pose) {
        super(oi, drive, propFactory, clf);
        propFactory.setPrefix(this);
        this.oi = oi;
        this.pose = pose;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        ham.reset();
        drive.getPositionalPid().reset();
        goalY = getRobotOrientedDistance();
    }

    private double getRobotOrientedDistance() {
        return (drive.getLeftTotalDistance() + drive.getRightTotalDistance()) / 2;
    }

    @Override
    public void execute() {
        double candidateTranslationPower = getTranslation();
        double currentY = getRobotOrientedDistance();

        // If we have moved forward compared to our previous goal, set that position as our new goal.
        if (currentY >= goalY) {
            goalY = currentY;
        }

        // If the human isn't commanding forward power, then listen to the PID.
        if (candidateTranslationPower < deadbandProp.get()) {
            candidateTranslationPower = drive.getPositionalPid().calculate(goalY, currentY);
        }

        // Under no circumstances power the robot backwards.
        candidateTranslationPower = MathUtils.constrainDouble(candidateTranslationPower, 0, 1);
        drive.drive(new XYPair(0, candidateTranslationPower), getRotation());
    }

}