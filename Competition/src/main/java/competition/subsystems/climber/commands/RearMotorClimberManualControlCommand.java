package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.RearMotorClimberSubsystem;
import xbot.common.command.BaseCommand;

public class RearMotorClimberManualControlCommand extends BaseCommand {

    RearMotorClimberSubsystem rear;
    OperatorInterface oi;

    @Inject
    public RearMotorClimberManualControlCommand(RearMotorClimberSubsystem rear, OperatorInterface oi) {
        this.rear = rear;
        this.oi = oi;

        this.requires(rear);
    }

    @Override
    public void initialize() {
        log.info("Initialize");
    }

    @Override
    public void execute() {
        rear.setLiftAndTilt(oi.operatorGamepad.getRightVector().y, 0);
    }
}