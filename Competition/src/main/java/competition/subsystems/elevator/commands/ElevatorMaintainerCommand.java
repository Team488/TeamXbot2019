package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.PID;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

public class ElevatorMaintainerCommand extends BaseCommand{
    final OperatorInterface oi;
    final ElevatorSubsystem elevatorSubsystem;
    final PIDManager pid;
    double power; 


    @Inject
    public ElevatorMaintainerCommand(OperatorInterface oi, ElevatorSubsystem elevatorSubsystem, 
    PIDFactory pidFactory) {
        this.oi = oi;
        this.elevatorSubsystem = elevatorSubsystem;
        this.pid = pidFactory.createPIDManager(getPrefix()+"MaintainerPID", 1, 0, 0);
        this.requires(this.elevatorSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    } 

    @Override 
    public void execute() {
        power = pid.calculate(elevatorSubsystem.getTickGoal(), elevatorSubsystem.getElevatorHeightInTicks());
        elevatorSubsystem.setPower(power);
    }
}
