package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

public class StopElevatorCommand extends BaseCommand {

    final OperatorInterface oi;
    final ElevatorSubsystem elevatorSubsystem;

    @Inject
    public StopElevatorCommand(OperatorInterface oi, ElevatorSubsystem elevatorSubsystem) {
        this.oi = oi;
        this.elevatorSubsystem = elevatorSubsystem;
        this.requires(this.elevatorSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        elevatorSubsystem.stop();
    }
}