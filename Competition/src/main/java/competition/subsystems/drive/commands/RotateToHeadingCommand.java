package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class RotateToHeadingCommand extends BaseCommand {

    DriveSubsystem drive;
    PoseSubsystem pose;
    double goal;
    double givenGoal;
    boolean relative;
    HeadingModule hm;

    @Inject
    public RotateToHeadingCommand(DriveSubsystem drive, PoseSubsystem pose, CommonLibFactory clf) {
        this.drive = drive;
        this.pose = pose;
        hm = clf.createHeadingModule(drive.getRotateToHeadingPid());
        this.requires(drive);
    }

    public void setHeadingGoal(double goal, boolean relative) {
        this.givenGoal = goal;
        this.relative = relative;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        goal = givenGoal;
        if (relative) {
            goal = pose.getCurrentHeading().shiftValue(givenGoal).getValue();
        }
    }

    @Override
    public void execute() {
        double rotation = hm.calculateHeadingPower(goal);
        drive.drive(new XYPair(), rotation);
    }

    @Override
    public boolean isFinished() {
        return hm.isOnTarget();
    }
}