package competition.subsystems.vision.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class RotateToVisionTargetCommand extends BaseCommand {

    final OperatorInterface oi;
    final VisionSubsystem visionSubsystem;
    final PoseSubsystem pose;
    final DriveSubsystem drive;
    double goal;
    double givenGoal;
    boolean relative;
    HeadingModule hm;
    double rotation;


    @Inject
    public RotateToVisionTargetCommand(OperatorInterface oi, 
    VisionSubsystem visionSubsystem, CommonLibFactory clf,
    DriveSubsystem drive, PoseSubsystem pose) {
        this.pose = pose;
        this.oi = oi;
        this.drive = drive;
        this.visionSubsystem = visionSubsystem;
        hm = clf.createHeadingModule(drive.getRotateToHeadingPid());
        this.requires(this.visionSubsystem);
    }

    public void setHeadingGoal(double goal, boolean relative) {
        this.givenGoal = goal;
        this.relative = relative;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if (visionSubsystem.isTargetInView()) {
            double relativeAngle = visionSubsystem.getAngleToTarget();
            double goal = pose.getCurrentHeading().shiftValue(relativeAngle).getValue();
            rotation = hm.calculateHeadingPower(goal);
        }
        drive.drive(new XYPair(0, oi.operatorGamepad.getLeftVector().y), rotation);
    }
}