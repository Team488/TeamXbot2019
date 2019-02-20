package competition.commandgroups.drivecommandgroups;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.PoseSubsystem.Side;
import competition.subsystems.pose.SetPoseToFieldLandmarkCommand;
import competition.subsystems.vision.commands.RotateToVisionTargetCommand;
import xbot.common.command.BaseCommandGroup;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.drive.PurePursuitCommand.PointLoadingMode;

public class ScoreOnFrontCargoCommandGroup extends BaseCommandGroup {

    @Inject
    public ScoreOnFrontCargoCommandGroup(
            RotateToVisionTargetCommand rotateToVisionTarget,
            SetPoseToFieldLandmarkCommand setPose, 
            ConfigurablePurePursuitCommand goToFrontCargo,
            ConfigurablePurePursuitCommand goForwardABit,
            PoseSubsystem poseSubsystem) {

        goForwardABit.addPoint(new RabbitPoint(0, 12*4, 90));
        goForwardABit.setMode(PointLoadingMode.Relative);

        setPose.forceHeading(true);
        setPose.setLandmark(Side.Left, FieldLandmark.HabLevelTwo);
        goToFrontCargo.setPointSupplier(
            () -> poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.FrontCargoShip, true));
        goToFrontCargo.setDotProductDrivingEnabled(true);
        this.addSequential(setPose);
        this.addSequential(goForwardABit);
        this.addSequential(goToFrontCargo);
        this.addSequential(rotateToVisionTarget);
        
    }
}