package competition.commandgroups.drivecommandgroups;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.PoseSubsystem.Side;
import competition.subsystems.vision.commands.RotateToVisionTargetCommand;
import xbot.common.command.BaseCommandGroup;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;

public class GoToLoadingStationCommandGroup extends BaseCommandGroup {

    @Inject
    public GoToLoadingStationCommandGroup(
            RotateToVisionTargetCommand rotateToVisionTarget,
            ConfigurablePurePursuitCommand goToLoadingStation,
            PoseSubsystem poseSubsystem) {

        goToLoadingStation.setPointSupplier(
            () -> poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.LoadingStation, true));
        goToLoadingStation.setDotProductDrivingEnabled(true);
        this.addSequential(goToLoadingStation);
        this.addSequential(rotateToVisionTarget);
    }
}