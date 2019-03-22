package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.FrontMotorClimberSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;

public class FrontMotorClimberManualControlCommand extends BaseMotorClimberManualControlCommand {

    @Inject
    public FrontMotorClimberManualControlCommand(FrontMotorClimberSubsystem front, OperatorInterface oi, ElevatorSubsystem elevator) {
        super(front, oi);
        this.requires(elevator);
    }

    @Override
    public double getHumanInput() {
        return oi.operatorGamepad.getLeftVector().y;
    }

    @Override
    public void initialize() {
        log.info("Initialize");
    }
}