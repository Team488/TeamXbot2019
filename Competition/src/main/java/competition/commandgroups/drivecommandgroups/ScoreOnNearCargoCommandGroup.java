package competition.commandgroups.drivecommandgroups;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.PoseSubsystem.Side;
import competition.subsystems.pose.SetPoseToFieldLandmarkCommand;
import competition.subsystems.vision.commands.RotateToVisionTargetCommand;
import xbot.common.command.BaseCommandGroup;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;

public class ScoreOnNearCargoCommandGroup extends BaseCommandGroup {

    @Inject
    public ScoreOnNearCargoCommandGroup(
            RotateToVisionTargetCommand rotateToVisionTarget,
            SetPoseToFieldLandmarkCommand setPose, 
            ConfigurablePurePursuitCommand goToNearCargo,
            PoseSubsystem poseSubsystem) {

        goToNearCargo.setPointSupplier(
            () -> poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.NearCargoShip, true));
        goToNearCargo.setDotProductDrivingEnabled(true);
        this.addSequential(setPose);
        this.addSequential(goToNearCargo);
        this.addSequential(rotateToVisionTarget);
    }
}