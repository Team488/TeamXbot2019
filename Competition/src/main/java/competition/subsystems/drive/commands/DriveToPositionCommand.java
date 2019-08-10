package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.FieldPose;
import xbot.common.math.MathUtils;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.pose.PoseSubsystemTest;

public class DriveToPositionCommand extends BaseCommand {
    private PoseSubsystem poseSubsystem;
    private DriveSubsystem driveSubsystem;
    private double goal;
    private double distanceToDrive;

    @Inject
    public DriveToPositionCommand(CommonLibFactory clf, PropertyFactory propFactory, PoseSubsystem pose, DriveSubsystem drive) {
       this.poseSubsystem = pose;
       this.driveSubsystem = drive;
    }

    public void initialize() {
        goal = poseSubsystem.getCurrentFieldPose().getPoint().y + distanceToDrive;
    }

    public void setDistanceToDrive(double distanceToDrive)
    {
        this.distanceToDrive = distanceToDrive;
    }

    @Override
    public void execute() {
        double power = driveSubsystem.getPositionalPid().calculate(goal, poseSubsystem.getCurrentFieldPose().getPoint().y);
        driveSubsystem.drive(power, power);
    }

    


}