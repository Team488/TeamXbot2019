package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.FrontMotorClimberSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

public class FrontMotorClimberManualControlCommand extends BaseCommand {

    FrontMotorClimberSubsystem front;
    OperatorInterface oi;

    @Inject
    public FrontMotorClimberManualControlCommand(FrontMotorClimberSubsystem front, OperatorInterface oi, ElevatorSubsystem elevator) {
        this.front = front;
        this.oi = oi;

        this.requires(front);
        this.requires(elevator);
    }

    @Override
    public void initialize() {
        log.info("Initialize");
    }

    @Override
    public void execute() {
        front.setLiftAndTilt(oi.operatorGamepad.getLeftVector().y, 0);
    }
}