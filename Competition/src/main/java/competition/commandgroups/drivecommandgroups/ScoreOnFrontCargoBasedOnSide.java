package competition.commandgroups.drivecommandgroups;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.SetPoseToFieldLandmarkCommand;
import competition.subsystems.vision.commands.RotateToVisionTargetCommand;
import xbot.common.command.BaseCommandGroup;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.drive.PurePursuitCommand.PointLoadingMode;
import xbot.common.subsystems.drive.RabbitPoint;

public class ScoreOnFrontCargoBasedOnSide extends BaseCommandGroup {

    @Inject
    public ScoreOnFrontCargoBasedOnSide(RotateToVisionTargetCommand rotateToVisionTarget,
            SetPoseToFieldLandmarkCommand setPose, ConfigurablePurePursuitCommand goToFrontCargo,
            ConfigurablePurePursuitCommand goForwardABit, PoseSubsystem poseSubsystem) {

        goForwardABit.addPoint(new RabbitPoint(0, 12 * 4, 90));
        goForwardABit.setMode(PointLoadingMode.Relative);

        setPose.forceHeading(true);
        setPose.setLandmarkSupplier(() -> setPose.startingSide(), () -> FieldLandmark.FrontCargoShip);
        goToFrontCargo.setPointSupplier(
            () -> poseSubsystem.getPathToNearestLandmark(FieldLandmark.FrontCargoShip));
        goToFrontCargo.setDotProductDrivingEnabled(true);
        this.addSequential(setPose);
        this.addSequential(goForwardABit);
        this.addSequential(goToFrontCargo);
        this.addSequential(rotateToVisionTarget);
        
        }
    }
