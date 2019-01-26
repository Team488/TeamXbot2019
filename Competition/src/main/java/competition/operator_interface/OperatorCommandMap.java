package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.CheesyDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.CheesyQuickTurnCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.pose.PoseSubsystem;
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
            ConfigurablePurePursuitCommand goFromHabTouchingRampToCargoShipFrontRight,
            ConfigurablePurePursuitCommand goFromHabTouchingRampToCargoShipRightThree,
            ConfigurablePurePursuitCommand goFromHabTouchingRampToRocketFrontRight,
            ConfigurablePurePursuitCommand goFromHabTouchingRampToPanelLoadRight,
            ConfigurablePurePursuitCommand goFromHabTouchingRampToCargoShipFrontLeft,
            ConfigurablePurePursuitCommand goFromHabTouchingRampToCargoShipLeftThree,
            ConfigurablePurePursuitCommand goFromHabTouchingRampToRocketFrontLeft,
            ConfigurablePurePursuitCommand goFromHabTouchingRampToPanelLoadLeft,
            ResetHeadingAndDistanceCommandGroup resetPose,
            PoseSubsystem poseSubsystem)
    {
        operatorInterface.gamepad.getifAvailable(6).whileHeld(quickTurn);
        operatorInterface.gamepad.getPovIfAvailable(0).whenPressed(tank);
        operatorInterface.gamepad.getPovIfAvailable(90).whenPressed(arcade);
        operatorInterface.gamepad.getPovIfAvailable(180).whenPressed(cheesyDrive);

        pursuit.setMode(PointLoadingMode.Relative);
        pursuit.addPoint(new RabbitPoint(3*12, 3*12, 0));
        operatorInterface.gamepad.getifAvailable(2).whenPressed(pursuit);
        
        goFromHabTouchingRampToCargoShipFrontRight.setMode(PointLoadingMode.Absolute);
        goFromHabTouchingRampToCargoShipFrontRight.addPoint(new RabbitPoint(176, 208, 90));
        goFromHabTouchingRampToCargoShipFrontRight.includeOnSmartDashboard("goFromHabTouchingRampToCargoShipFrontRight");

        goFromHabTouchingRampToCargoShipRightThree.setMode(PointLoadingMode.Absolute);
        goFromHabTouchingRampToCargoShipRightThree.addPoint(new RabbitPoint(212, 262, 180));
        goFromHabTouchingRampToCargoShipRightThree.includeOnSmartDashboard("goFromHabTouchingRampToCargoShipRightThree");

        goFromHabTouchingRampToRocketFrontRight.setMode(PointLoadingMode.Absolute);
        goFromHabTouchingRampToRocketFrontRight.addPoint(new RabbitPoint(302, 197, 84));
        goFromHabTouchingRampToRocketFrontRight.includeOnSmartDashboard("goFromHabTouchingRampToRocketFrontRight");

        goFromHabTouchingRampToPanelLoadRight.setMode(PointLoadingMode.Absolute);
        goFromHabTouchingRampToPanelLoadRight.addPoint(new RabbitPoint(300, 23, 270));
        goFromHabTouchingRampToPanelLoadRight.includeOnSmartDashboard("goFromHabTouchingRampToPanelLoadRight");

        
        // left
        goFromHabTouchingRampToCargoShipFrontLeft.setMode(PointLoadingMode.Absolute);
        goFromHabTouchingRampToCargoShipFrontLeft.addPoint(new RabbitPoint(148, 208, 90));
        goFromHabTouchingRampToCargoShipFrontLeft.includeOnSmartDashboard("goFromHabTouchingRampToCargoShipFrontLeft");

        goFromHabTouchingRampToCargoShipLeftThree.setMode(PointLoadingMode.Absolute);
        goFromHabTouchingRampToCargoShipLeftThree.addPoint(new RabbitPoint(112, 262, 180));
        goFromHabTouchingRampToCargoShipLeftThree.includeOnSmartDashboard("goFromHabTouchingRampToCargoShipLeftThree");

        goFromHabTouchingRampToRocketFrontLeft.setMode(PointLoadingMode.Absolute);
        goFromHabTouchingRampToRocketFrontLeft.addPoint(new RabbitPoint(22, 197, 84));
        goFromHabTouchingRampToRocketFrontLeft.includeOnSmartDashboard("goFromHabTouchingRampToRocketFrontLeft");

        goFromHabTouchingRampToPanelLoadLeft.setMode(PointLoadingMode.Absolute);
        goFromHabTouchingRampToPanelLoadLeft.addPoint(new RabbitPoint(24, 23, 270));
        goFromHabTouchingRampToPanelLoadLeft.includeOnSmartDashboard("goFromHabTouchingRampToPanelLoadLeft");


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
        SetPoseToFieldLandmarkCommand setPoseToCargoShipRightThree,
        SetPoseToFieldLandmarkCommand setPoseToCargoShipFrontRight,
        SetPoseToFieldLandmarkCommand setPoseToHabRightTwo,
        SetPoseToFieldLandmarkCommand setPoseToHabRightOne,
        SetPoseToFieldLandmarkCommand setPoseToHabTouchingRampRight,
        SetPoseToFieldLandmarkCommand setPoseToPanelLoadRight,
        SetPoseToFieldLandmarkCommand setPoseToRocketFrontRight,

        SetPoseToFieldLandmarkCommand setPoseToCargoShipLeftThree,
        SetPoseToFieldLandmarkCommand setPoseToCargoShipFrontLeft,
        SetPoseToFieldLandmarkCommand setPoseToHabLeftTwo,
        SetPoseToFieldLandmarkCommand setPoseToHabLeftOne,
        SetPoseToFieldLandmarkCommand setPoseToHabTouchingRampLeft,
        SetPoseToFieldLandmarkCommand setPoseToPanelLoadLeft,
        SetPoseToFieldLandmarkCommand setPoseToRocketFrontLeft

    ) {
        //FieldPose landmarkLocation = pose.getFieldPoseForLandmark(chosenLandmark);

        setPoseToCargoShipRightThree.setLandmark(FieldLandmark.CargoShipRightThree);
        setPoseToCargoShipRightThree.forceHeading(true);
        setPoseToCargoShipRightThree.includeOnSmartDashboard("setPoseToCargoShipRightThree");
        setPoseToCargoShipFrontRight.setLandmark(FieldLandmark.CargoShipFrontRight);
        setPoseToCargoShipFrontRight.forceHeading(true);
        setPoseToCargoShipFrontRight.includeOnSmartDashboard("setPoseToCargoShipFrontRight");
        setPoseToHabRightTwo.setLandmark(FieldLandmark.HabRightTwo);
        setPoseToHabRightTwo.forceHeading(true);
        setPoseToHabRightTwo.includeOnSmartDashboard("setPoseToHabRightTwo");
        setPoseToHabRightOne.setLandmark(FieldLandmark.HabRightOne);
        setPoseToHabRightOne.forceHeading(true);
        setPoseToHabRightOne.includeOnSmartDashboard("setPoseToHabRightOne");
        setPoseToHabTouchingRampRight.setLandmark(FieldLandmark.HabTouchingRampRight);
        setPoseToHabTouchingRampRight.forceHeading(true);
        setPoseToHabTouchingRampRight.includeOnSmartDashboard("setPoseToHabTouchingRampRight");
        setPoseToPanelLoadRight.setLandmark(FieldLandmark.PanelLoadRight);
        setPoseToPanelLoadRight.forceHeading(true);
        setPoseToPanelLoadRight.includeOnSmartDashboard("setPoseToPanelLoadRight");
        setPoseToRocketFrontRight.setLandmark(FieldLandmark.RocketFrontRight);
        setPoseToRocketFrontRight.forceHeading(true);
        setPoseToRocketFrontRight.includeOnSmartDashboard("setPoseToRocketFrontRight");

        //left
        setPoseToCargoShipLeftThree.setLandmark(FieldLandmark.CargoShipLeftThree);
        setPoseToCargoShipLeftThree.forceHeading(true);
        setPoseToCargoShipLeftThree.includeOnSmartDashboard("setPoseToCargoShipLeftThree");
        setPoseToCargoShipFrontLeft.setLandmark(FieldLandmark.CargoShipFrontLeft);
        setPoseToCargoShipFrontLeft.forceHeading(true);
        setPoseToCargoShipFrontLeft.includeOnSmartDashboard("setPoseToCargoShipFrontLeft");
        setPoseToHabLeftTwo.setLandmark(FieldLandmark.HabLeftTwo);
        setPoseToHabLeftTwo.forceHeading(true);
        setPoseToHabLeftTwo.includeOnSmartDashboard("setPoseToHabLeftTwo");
        setPoseToHabLeftOne.setLandmark(FieldLandmark.HabLeftOne);
        setPoseToHabLeftOne.forceHeading(true);
        setPoseToHabLeftOne.includeOnSmartDashboard("setPoseToHabLeftOne");
        setPoseToHabTouchingRampLeft.setLandmark(FieldLandmark.HabTouchingRampLeft);
        setPoseToHabTouchingRampLeft.forceHeading(true);
        setPoseToHabTouchingRampLeft.includeOnSmartDashboard("setPoseToHabTouchingRampLeft");
        setPoseToPanelLoadLeft.setLandmark(FieldLandmark.PanelLoadLeft);
        setPoseToPanelLoadLeft.forceHeading(true);
        setPoseToPanelLoadLeft.includeOnSmartDashboard("setPoseToPanelLoadLeft");
        setPoseToRocketFrontLeft.setLandmark(FieldLandmark.RocketFrontLeft);
        setPoseToRocketFrontLeft.forceHeading(true);
        setPoseToRocketFrontLeft.includeOnSmartDashboard("setPoseToRocketFrontLeft");

    }
    
}
