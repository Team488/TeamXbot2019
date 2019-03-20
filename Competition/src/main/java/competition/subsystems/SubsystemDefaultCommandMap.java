package competition.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.RumbleManagerCommand;
import competition.subsystems.climber.FourBarSubsystem;
import competition.subsystems.climber.FrontMotorClimberSubsystem;
import competition.subsystems.climber.RearMotorClimberSubsystem;
import competition.subsystems.climber.commands.FourBarViaJoysticksCommand;
import competition.subsystems.climber.commands.FrontMotorClimberDoNothingCommand;
import competition.subsystems.climber.commands.RearMotorClimberDoNothingCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.ElevatorMaintainerCommand;
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
    public void setupMotorClimberSubsystem(FrontMotorClimberSubsystem front,
            FrontMotorClimberDoNothingCommand frontDoNothing, RearMotorClimberSubsystem rear,
            RearMotorClimberDoNothingCommand rearDoNothing) {
        front.setDefaultCommand(frontDoNothing);
        rear.setDefaultCommand(rearDoNothing);
    }
  
    
    }
