package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.CheesyDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.CheesyQuickTurnCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
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
    
}
