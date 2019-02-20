package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseSetpointCommand;

public class SetElevatorTickGoalCommand extends BaseSetpointCommand {

    final OperatorInterface oi;
    final ElevatorSubsystem elevatorSubsystem;
    double tickGoal; 

    @Inject
    public SetElevatorTickGoalCommand(OperatorInterface oi, ElevatorSubsystem elevatorSubsystem) {
        super(elevatorSubsystem);
        this.oi = oi;
        this.elevatorSubsystem = elevatorSubsystem;
        this.requires(this.elevatorSubsystem);
    }

    public void setGoal(double ticks){
        tickGoal = ticks;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        elevatorSubsystem.setTickGoal(tickGoal);
    }


}