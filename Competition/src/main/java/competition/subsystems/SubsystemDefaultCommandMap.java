package competition.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.RumbleManagerCommand;
import competition.subsystems.climber.FourBarSubsystem;
import competition.subsystems.climber.MotorClimberSubsystem;
import competition.subsystems.climber.commands.FourBarViaJoysticksCommand;
import competition.subsystems.climber.commands.MotorClimberDoNothingCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.ElevatorMaintainerCommand;
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
    public void setupElevatorSubsystem(ElevatorSubsystem elevatorSubsystem, ElevatorMaintainerCommand command) {
        elevatorSubsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupRumbleSubsystem(RumbleSubsystem rumbleSubsystem, RumbleManagerCommand command) {
        rumbleSubsystem.setDefaultCommand(command); 
    }

    @Inject
    public void setupFourBarSubsystem(FourBarSubsystem fourBar, FourBarViaJoysticksCommand command) {
        fourBar.setDefaultCommand(command);
    }

    @Inject
    public void setupMotorClimberSubsystem(MotorClimberSubsystem climber, MotorClimberDoNothingCommand command) {
        climber.setDefaultCommand(command);
    }
}
