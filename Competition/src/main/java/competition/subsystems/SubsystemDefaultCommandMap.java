package competition.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.RumbleManagerCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.StopElevatorCommand;
import competition.subsystems.rumble.RumbleSubsystem;

@Singleton
public class SubsystemDefaultCommandMap {
    // For setting the default commands on subsystems

    @Inject
    public void setupDriveSubsystem(DriveSubsystem driveSubsystem, ArcadeDriveWithJoysticksCommand command) {
        driveSubsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupElevatorSubsystem(ElevatorSubsystem elevatorSubsystem, StopElevatorCommand command) {
        elevatorSubsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupRumbleSubsystem(RumbleSubsystem rumbleSubsystem, RumbleManagerCommand command) {
        rumbleSubsystem.setDefaultCommand(command); 
    }
}
