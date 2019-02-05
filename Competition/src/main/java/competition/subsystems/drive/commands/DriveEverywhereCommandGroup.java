package competition.subsystems.drive.commands;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.PoseSubsystem.Side;
import xbot.common.command.BaseCommandGroup;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;

public class DriveEverywhereCommandGroup extends BaseCommandGroup {

    @Inject
    public DriveEverywhereCommandGroup(
        PoseSubsystem pose,
        ConfigurablePurePursuitCommand goToRocket,
        ConfigurablePurePursuitCommand goToFrontCargo,
        Provider<ConfigurablePurePursuitCommand> cppFactory)
        {
        goToRocket.setPoints(pose.getPathToLandmark(Side.Left, FieldLandmark.NearRocket, false));
        goToFrontCargo.setPoints(pose.getPathToLandmark(Side.Left, FieldLandmark.FrontCargoShip, false));

        this.addSequential(goToFrontCargo);
        addReturnHome(pose, cppFactory);
        
        this.addSequential(goToRocket);
        addReturnHome(pose, cppFactory);

        addGoToCargo(pose, cppFactory);
        addReturnHome(pose, cppFactory);

        addGoToCargo(pose, cppFactory);
        addReturnHome(pose, cppFactory);

        addGoToCargo(pose, cppFactory);
        addReturnHome(pose, cppFactory);
    }

    private void addReturnHome(PoseSubsystem pose, Provider<ConfigurablePurePursuitCommand> cppFactory) {
        ConfigurablePurePursuitCommand backToLoadingStation = cppFactory.get();
        backToLoadingStation.setPoints(pose.getPathToLandmark(Side.Left, FieldLandmark.LoadingStation, false));
        this.addSequential(backToLoadingStation);
    }

    private void addGoToCargo(PoseSubsystem pose, Provider<ConfigurablePurePursuitCommand> cppFactory) {
        ConfigurablePurePursuitCommand goToCargo = cppFactory.get();
        goToCargo.setPoints(pose.getPathToLandmark(Side.Left, FieldLandmark.NearCargoShip, false));
        this.addSequential(goToCargo);
    }
}