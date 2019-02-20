package competition.commandgroups.drivecommandgroups;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.PoseSubsystem.Side;
import competition.subsystems.vision.commands.RotateToVisionTargetCommand;
import xbot.common.command.BaseCommandGroup;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;

public class ScoreOnMidCargoCommandGroup extends BaseCommandGroup {

    @Inject
    public ScoreOnMidCargoCommandGroup(
            RotateToVisionTargetCommand rotateToVisionTarget,
            ConfigurablePurePursuitCommand goToMidCargo,
            PoseSubsystem poseSubsystem) {

        goToMidCargo.setPointSupplier(
            () -> poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.MidCargoShip, true));
        goToMidCargo.setDotProductDrivingEnabled(true);
        this.addSequential(goToMidCargo);
        this.addSequential(rotateToVisionTarget);
    }
}