package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

public class ElevatorManualOverrideCommand extends BaseCommand {

    ElevatorSubsystem elevator;
    OperatorInterface oi;

    @Inject
    public ElevatorManualOverrideCommand(ElevatorSubsystem elevator, OperatorInterface oi) {
        this.elevator = elevator;
        this.oi = oi;
        requires(elevator);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        elevator.setSafeties(false);
    }

    @Override
    public void execute() {
        elevator.insanelyDangerousSetPower(oi.operatorGamepad.getLeftVector().y);
    }
}