package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.commandgroups.drivecommandgroups.GoToLoadingStationCommandGroup;
import competition.commandgroups.drivecommandgroups.ScoreOnFrontCargoCommandGroup;
import competition.commandgroups.drivecommandgroups.ScoreOnMidCargoCommandGroup;
import competition.commandgroups.drivecommandgroups.ScoreOnNearCargoCommandGroup;
import competition.subsystems.climber.commands.DeployAllClimberLegsCommand;
import competition.subsystems.climber.commands.DeployBackCommand;
import competition.subsystems.climber.commands.DeployFrontCommand;
import competition.subsystems.climber.commands.RetractBackCommand;
import competition.subsystems.climber.commands.RetractFrontCommand;
import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.CheesyDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.CheesyQuickTurnCommand;
import competition.subsystems.drive.commands.ConfigureDriveSubsystemCommand;
import competition.subsystems.drive.commands.DriveEverywhereCommandGroup;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.elevator.commands.LowerElevatorCommand;
import competition.subsystems.elevator.commands.RaiseElevatorCommand;
import competition.subsystems.elevator.commands.SetElevatorTickGoalCommand;
import competition.subsystems.elevator.commands.StopElevatorCommand;
import competition.subsystems.gripper.commands.ExtendGripperCommand;
import competition.subsystems.gripper.commands.GrabDiscCommand;
import competition.subsystems.gripper.commands.ReleaseDiscCommand;
import competition.subsystems.gripper.commands.RetractGripperCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.PoseSubsystem.Side;
import competition.subsystems.pose.SetPoseToFieldLandmarkCommand;
import competition.subsystems.vision.VisionSubsystem;
import competition.subsystems.vision.commands.RotateToVisionTargetCommand;
import xbot.common.controls.sensors.AnalogHIDButton.AnalogHIDDescription;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.drive.PurePursuitCommand.PointLoadingMode;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.pose.ResetHeadingAndDistanceCommandGroup;

@Singleton
public class OperatorCommandMap {
    // For mapping operator interface buttons to commands

    // Example for setting up a command to fire when a button is pressed:

    @Inject
    public void setupDriveCommands(OperatorInterface operatorInterface, VisionSubsystem vision, CheesyDriveWithJoysticksCommand cheesyDrive,
            ArcadeDriveWithJoysticksCommand arcade, TankDriveWithJoysticksCommand tank, RotateToHeadingCommand rotate,
            CheesyQuickTurnCommand quickTurn, ConfigurablePurePursuitCommand pursuit,
            ResetHeadingAndDistanceCommandGroup resetPose, ConfigurablePurePursuitCommand forward,
            ConfigurablePurePursuitCommand backward, DriveEverywhereCommandGroup driveEverywhere,
            ConfigurablePurePursuitCommand goToRocket, ConfigurablePurePursuitCommand goToLoadingStation,
            ConfigurablePurePursuitCommand goToFrontCargo, ConfigurablePurePursuitCommand goToNearCargo,
            ConfigurablePurePursuitCommand goToFarLoadingStation, PoseSubsystem poseSubsystem,
            ConfigurablePurePursuitCommand goToVisionTarget) {
        operatorInterface.driverGamepad.getifAvailable(6).whileHeld(quickTurn);
        operatorInterface.driverGamepad.getPovIfAvailable(0).whenPressed(tank);
        operatorInterface.driverGamepad.getPovIfAvailable(90).whenPressed(arcade);
        operatorInterface.driverGamepad.getPovIfAvailable(180).whenPressed(cheesyDrive);

        driveEverywhere.includeOnSmartDashboard();

        goToRocket.setPointSupplier(() -> poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.NearRocket, true));
        goToRocket.setDotProductDrivingEnabled(true);
        goToRocket.includeOnSmartDashboard("Go To Rocket");

        goToLoadingStation
                .setPointSupplier(() -> poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.LoadingStation, true));
        goToLoadingStation.setDotProductDrivingEnabled(true);
        goToLoadingStation.includeOnSmartDashboard("Go To Loading Station");

        goToFarLoadingStation.setPointSupplier(
                () -> poseSubsystem.getPathToLandmark(Side.Right, FieldLandmark.LoadingStation, true));
        goToFarLoadingStation.setDotProductDrivingEnabled(true);
        goToFarLoadingStation.includeOnSmartDashboard("Go To Far Loading Station");

        goToFrontCargo
                .setPointSupplier(() -> poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.FrontCargoShip, true));
        goToFrontCargo.setDotProductDrivingEnabled(true);
        goToFrontCargo.includeOnSmartDashboard("Go To Front Cargo");

        goToNearCargo
                .setPointSupplier(() -> poseSubsystem.getPathToLandmark(Side.Left, FieldLandmark.NearCargoShip, true));
        goToNearCargo.setDotProductDrivingEnabled(true);
        goToNearCargo.includeOnSmartDashboard("Go To Near Cargo");

        pursuit.setMode(PointLoadingMode.Relative);
        pursuit.addPoint(new RabbitPoint(3 * 12, 3 * 12, 0));
        pursuit.includeOnSmartDashboard("Calibrate PP Box Turn");

        rotate.setHeadingGoal(90, true);
        rotate.includeOnSmartDashboard("Calibrate Rotation 90");
        resetPose.includeOnSmartDashboard("Reset Pose to 0 0 90");

        forward.setMode(PointLoadingMode.Relative);
        forward.addPoint(new RabbitPoint(0, 4 * 12, 90));
        forward.includeOnSmartDashboard("Move Forward");

        backward.setMode(PointLoadingMode.Relative);
        backward.addPoint(new RabbitPoint(0, -4 * 12, 90));
        backward.includeOnSmartDashboard("Move Backward");

        goToVisionTarget.setMode(PointLoadingMode.Relative);
        goToVisionTarget.setDotProductDrivingEnabled(true);
        goToVisionTarget.setPointSupplier(() -> vision.getVisionTargetRelativePosition());
        operatorInterface.driverGamepad.getifAvailable(7).whenPressed(goToVisionTarget);
    }

    @Inject
    public void setupGripperCommands(OperatorInterface operatorInterface, ReleaseDiscCommand releaseDisc,
            GrabDiscCommand grabDisc, ExtendGripperCommand extend, RetractGripperCommand retract) {
        operatorInterface.operatorGamepad.getifAvailable(1).whenPressed(grabDisc);
        operatorInterface.operatorGamepad.getifAvailable(2).whenPressed(releaseDisc);
        operatorInterface.operatorGamepad.getifAvailable(3).whenPressed(extend);
        operatorInterface.operatorGamepad.getifAvailable(4).whenPressed(retract);
    }

    @Inject
    public void setupElevatorCommands(OperatorInterface operatorInterface, RaiseElevatorCommand raiseElevator,
            LowerElevatorCommand lowerElevator, StopElevatorCommand stopElevator,
            SetElevatorTickGoalCommand setElevatorTickGoal) {
        // Right Up
        AnalogHIDDescription triggerRaise = new AnalogHIDDescription(3, .25, 1.01);
        operatorInterface.operatorGamepad.addAnalogButton(triggerRaise);
        operatorInterface.operatorGamepad.getAnalogIfAvailable(triggerRaise).whileHeld(raiseElevator);

        // Left Down
        AnalogHIDDescription triggerLower = new AnalogHIDDescription(2, .25, 1.01);
        operatorInterface.operatorGamepad.addAnalogButton(triggerLower);
        operatorInterface.operatorGamepad.getAnalogIfAvailable(triggerLower).whileHeld(lowerElevator);

        operatorInterface.operatorGamepad.getifAvailable(8).whenPressed(stopElevator);
    }

    @Inject
    public void setupPoseCommands(SetPoseToFieldLandmarkCommand setPoseToLeftHabLevelZero,
            SetPoseToFieldLandmarkCommand setPoseToLeftLoadingStation, OperatorInterface operatorInterface,
            SetPoseToFieldLandmarkCommand setPositionToLeftLoadingStation) {
        // Start with a smaller set of commands, we can build up from there.
        setPoseToLeftHabLevelZero.setLandmark(Side.Left, FieldLandmark.HabLevelZero);
        setPoseToLeftHabLevelZero.forceHeading(true);
        setPoseToLeftHabLevelZero.includeOnSmartDashboard("Set Pose to Left Level 0");
        setPoseToLeftLoadingStation.setLandmark(Side.Left, FieldLandmark.LoadingStation);
        setPoseToLeftLoadingStation.forceHeading(true);
        setPoseToLeftLoadingStation.includeOnSmartDashboard("Set pose to Left Loading Station");

        setPositionToLeftLoadingStation.setLandmark(Side.Left, FieldLandmark.LoadingStation);
        setPositionToLeftLoadingStation.forceHeading(false);
        operatorInterface.driverGamepad.getifAvailable(5).whenPressed(setPositionToLeftLoadingStation);
    }

    @Inject
    public void setupVisionCommands(OperatorInterface operatorInterface,
            RotateToVisionTargetCommand rotateToVisionTargetCommand) {
            rotateToVisionTargetCommand.setContinuousAcquisition(true);
        operatorInterface.driverGamepad.getifAvailable(8).whileHeld(rotateToVisionTargetCommand);
        rotateToVisionTargetCommand.includeOnSmartDashboard("Rotate To Vision Target");
    }

    @Inject
    public void setupDriverCommandGroups(OperatorInterface operatorInterface, ScoreOnMidCargoCommandGroup mid,
            ScoreOnFrontCargoCommandGroup front, ScoreOnNearCargoCommandGroup near,
            GoToLoadingStationCommandGroup loading,
            RotateToHeadingCommand faceForward,
            RotateToHeadingCommand faceLeft,
            RotateToHeadingCommand faceRight,
            RotateToHeadingCommand faceBack) {
        front.includeOnSmartDashboard("Score on Front Cargo");
        near.includeOnSmartDashboard("Score on Near Cargo");
        mid.includeOnSmartDashboard("Score on Mid Cargo");
        loading.includeOnSmartDashboard("Go to Loading Station");

        faceForward.setHeadingGoal(90, false);
        faceLeft.setHeadingGoal(180, false);
        faceRight.setHeadingGoal(0, false);
        faceBack.setHeadingGoal(-90, false);

        operatorInterface.driverGamepad.getifAvailable(4).whileHeld(faceForward);
        operatorInterface.driverGamepad.getifAvailable(3).whileHeld(faceLeft);
        operatorInterface.driverGamepad.getifAvailable(2).whileHeld(faceRight);
        operatorInterface.driverGamepad.getifAvailable(1).whileHeld(faceBack);
    }

    @Inject
    public void setUpClimberCommands(OperatorInterface operatorInterface, DeployBackCommand deployBack, DeployFrontCommand deployFront,
            RetractBackCommand retractBack, RetractFrontCommand retractFront) {
                //deployFront, deployBack
        operatorInterface.operatorGamepad.getifAvailable(9).whenPressed(deployBack);
        operatorInterface.operatorGamepad.getifAvailable(10).whenPressed(deployFront);
        deployFront.includeOnSmartDashboard("Climb - deployFront");
        deployBack.includeOnSmartDashboard("Climb - deployBack");

        operatorInterface.operatorGamepad.getifAvailable(5).whenPressed(retractBack);
        operatorInterface.operatorGamepad.getifAvailable(6).whenPressed(retractFront);
    }

    @Inject
    public void setupConfigureDriveSubsystemCommands(ConfigureDriveSubsystemCommand setDriveTalonParameters) {
        setDriveTalonParameters.includeOnSmartDashboard("Set Drive Talon Parameters");
    }
}
