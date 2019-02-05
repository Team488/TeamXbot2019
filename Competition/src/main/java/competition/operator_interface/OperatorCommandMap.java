package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.CheesyDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.CheesyQuickTurnCommand;
import competition.subsystems.drive.commands.DriveEverywhereCommandGroup;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.elevator.commands.LowerElevatorCommand;
import competition.subsystems.elevator.commands.RaiseElevatorCommand;
import competition.subsystems.elevator.commands.StopElevatorCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.SetPoseToFieldLandmarkCommand;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.PoseSubsystem.Side;
import xbot.common.controls.sensors.AnalogHIDButton.AnalogHIDDescription;
import xbot.common.controls.sensors.XJoystick;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.drive.PurePursuitCommand.PointLoadingMode;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.pose.ResetHeadingAndDistanceCommandGroup;

@Singleton
public class OperatorCommandMap {
    // For mapping operator interface buttons to commands

    // Example for setting up a command to fire when a button is pressed:

    @Inject
    public void setupDriveCommands(OperatorInterface operatorInterface,
            CheesyDriveWithJoysticksCommand cheesyDrive,
            ArcadeDriveWithJoysticksCommand arcade,
            TankDriveWithJoysticksCommand tank,
            RotateToHeadingCommand rotate,
            CheesyQuickTurnCommand quickTurn,
            ConfigurablePurePursuitCommand pursuit,
            ResetHeadingAndDistanceCommandGroup resetPose,
            ConfigurablePurePursuitCommand forward,
            ConfigurablePurePursuitCommand backward,
            DriveEverywhereCommandGroup driveEverywhere,
            ConfigurablePurePursuitCommand goToRocket,
            ConfigurablePurePursuitCommand goToLoadingStation,
            ConfigurablePurePursuitCommand goToFrontCargo,
            ConfigurablePurePursuitCommand goToNearCargo,
            PoseSubsystem poseSubsystem)
    {
        operatorInterface.gamepad.getifAvailable(6).whileHeld(quickTurn);
        operatorInterface.gamepad.getPovIfAvailable(0).whenPressed(tank);
        operatorInterface.gamepad.getPovIfAvailable(90).whenPressed(arcade);
        operatorInterface.gamepad.getPovIfAvailable(180).whenPressed(cheesyDrive);

        driveEverywhere.includeOnSmartDashboard();

        goToRocket.setPoints(poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.NearRocket, false));
        goToRocket.includeOnSmartDashboard("Go To Rocket");
        goToLoadingStation.setPoints(poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.LoadingStation, false));
        goToLoadingStation.includeOnSmartDashboard("Go To Loading Station");
        goToFrontCargo.setPoints(poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.FrontCargoShip, false));
        goToFrontCargo.includeOnSmartDashboard("Go To Front Cargo");
        goToNearCargo.setPoints(poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.NearCargoShip, false));
        goToNearCargo.includeOnSmartDashboard("Go To Near Cargo");

        pursuit.setMode(PointLoadingMode.Relative);
        pursuit.addPoint(new RabbitPoint(3*12, 3*12, 0));
        operatorInterface.gamepad.getifAvailable(2).whenPressed(pursuit);        
        
        rotate.setHeadingGoal(90, true);
        operatorInterface.gamepad.getifAvailable(1).whenPressed(rotate);
        operatorInterface.gamepad.getifAvailable(3).whenPressed(resetPose);

        forward.setMode(PointLoadingMode.Relative);
        forward.addPoint(new RabbitPoint(0, 4*12, 90));
        operatorInterface.gamepad.getifAvailable(7).whenPressed(forward);

        backward.setMode(PointLoadingMode.Relative);
        backward.addPoint(new RabbitPoint(0, -4*12, 90));
        operatorInterface.gamepad.getifAvailable(8).whenPressed(backward);
    }
    
    public void setupElevatorCommands(
        OperatorInterface operatorInterface,
        RaiseElevatorCommand raiseElevator,
        LowerElevatorCommand lowerElevator,
        StopElevatorCommand stopElevator,
        XJoystick elevatorButtons   
    )

    {
        AnalogHIDDescription triggerRaise = new AnalogHIDDescription(3, .25, 1.01);
        elevatorButtons.addAnalogButton(triggerRaise);
        operatorInterface.gamepad.getAnalogIfAvailable(triggerRaise).whenPressed(raiseElevator);
        
        AnalogHIDDescription triggerLower = new AnalogHIDDescription(2, .25, 1.01);
        elevatorButtons.addAnalogButton(triggerLower);
        operatorInterface.gamepad.getAnalogIfAvailable(triggerLower).whenPressed(lowerElevator);
        
        operatorInterface.gamepad.getifAvailable(8).whenPressed(stopElevator);
    }

    @Inject
    public void setupPoseCommands(
        SetPoseToFieldLandmarkCommand setPoseToLeftHabLevelZero,
        SetPoseToFieldLandmarkCommand setPoseToLeftLoadingStation
    ) {
        // Start with a smaller set of commands, we can build up from there.
        setPoseToLeftHabLevelZero.setLandmark(Side.Left, FieldLandmark.HabLevelZero);
        setPoseToLeftLoadingStation.setLandmark(Side.Left, FieldLandmark.LoadingStation);
    }    
}

