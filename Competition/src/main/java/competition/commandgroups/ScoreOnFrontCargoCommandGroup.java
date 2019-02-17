package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.PoseSubsystem.Side;
import competition.subsystems.pose.SetPoseToFieldLandmarkCommand;
import competition.subsystems.vision.commands.RotateToVisionTargetCommand;
import xbot.common.command.BaseCommandGroup;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;

public class ScoreOnFrontCargoCommandGroup extends BaseCommandGroup {

    @Inject
    public ScoreOnFrontCargoCommandGroup(
            RotateToVisionTargetCommand rotateToVisionTarget,
            SetPoseToFieldLandmarkCommand setPose, 
            ConfigurablePurePursuitCommand goToFrontCargo,
            PoseSubsystem poseSubsystem) {

        setPose.forceHeading(true);
        setPose.setLandmark(Side.Left, FieldLandmark.HabLevelTwo);
        goToFrontCargo.setPointSupplier(
            () -> poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.FrontCargoShip, true));
        goToFrontCargo.setDotProductDrivingEnabled(true);
        this.addSequential(setPose);
        this.addSequential(goToFrontCargo);
        this.addSequential(rotateToVisionTarget);
        
    }
}