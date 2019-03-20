package competition.commandgroups.drivecommandgroups;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.vision.commands.RotateToVisionTargetCommand;
import xbot.common.command.BaseCommandGroup;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;

public class GoToNearestLoadingStationCommandGroup extends BaseCommandGroup {

    @Inject
    public GoToNearestLoadingStationCommandGroup(RotateToVisionTargetCommand rotate, PoseSubsystem pose,
            ConfigurablePurePursuitCommand goToLoadingStation) {
                
            goToLoadingStation.setPointSupplier(
                () -> pose.getPathToNearestLandmark(FieldLandmark.LoadingStation));
            goToLoadingStation.setDotProductDrivingEnabled(true);
            this.addSequential(goToLoadingStation);
            this.addSequential(rotate);
        }
    }

