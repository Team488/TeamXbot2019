package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.CheesyDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.CheesyQuickTurnCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.elevator.commands.LowerElevatorCommand;
import competition.subsystems.elevator.commands.RaiseElevatorCommand;
import competition.subsystems.elevator.commands.StopElevatorCommand;
import xbot.common.controls.sensors.XJoystick;
import xbot.common.controls.sensors.AnalogHIDButton.AnalogHIDDescription;
import competition.subsystems.pose.SetPoseToFieldLandmarkCommand;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.drive.PurePursuitCommand.PointLoadingMode;
import xbot.common.subsystems.pose.ResetHeadingAndDistanceCommandGroup;
import xbot.common.subsystems.pose.commands.ResetDistanceCommand;
import xbot.common.subsystems.drive.RabbitPoint;

@Singleton
public class OperatorCommandMap {
    // For mapping operator interface buttons to commands

    // Example for setting up a command to fire when a button is pressed:
    
    @Inject
    public void setupDriveCommands(
            OperatorInterface operatorInterface,
            CheesyDriveWithJoysticksCommand cheesyDrive,
            ArcadeDriveWithJoysticksCommand arcade,
            TankDriveWithJoysticksCommand tank,
            RotateToHeadingCommand rotate,
            CheesyQuickTurnCommand quickTurn,
            ConfigurablePurePursuitCommand pursuit,
            ResetHeadingAndDistanceCommandGroup resetPose)
    {
        operatorInterface.gamepad.getifAvailable(6).whileHeld(quickTurn);
        operatorInterface.gamepad.getPovIfAvailable(0).whenPressed(tank);
        operatorInterface.gamepad.getPovIfAvailable(90).whenPressed(arcade);
        operatorInterface.gamepad.getPovIfAvailable(180).whenPressed(cheesyDrive);

        pursuit.setMode(PointLoadingMode.Relative);
        pursuit.addPoint(new RabbitPoint(3*12, 3*12, 0));
        operatorInterface.gamepad.getifAvailable(2).whenPressed(pursuit);
        
        rotate.setHeadingGoal(90, true);
        operatorInterface.gamepad.getifAvailable(1).whenPressed(rotate);
        operatorInterface.gamepad.getifAvailable(3).whenPressed(resetPose);

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
        SetPoseToFieldLandmarkCommand setPoseToHabLevelOne,
        SetPoseToFieldLandmarkCommand setPoseToRightLoadingStation,
        SetPoseToFieldLandmarkCommand setPoseToCargoShipSix
    ) {
        setPoseToHabLevelOne.setLandmark(FieldLandmark.HabLevelOne);
        setPoseToHabLevelOne.forceHeading(true);
        setPoseToHabLevelOne.includeOnSmartDashboard();
        setPoseToRightLoadingStation.setLandmark(FieldLandmark.RightLoadingStation);
        setPoseToRightLoadingStation.forceHeading(true);
        setPoseToRightLoadingStation.includeOnSmartDashboard();
        setPoseToCargoShipSix.setLandmark(FieldLandmark.CargoShipSix);
        setPoseToCargoShipSix.forceHeading(true);
        setPoseToCargoShipSix.includeOnSmartDashboard();

    }
    
}
