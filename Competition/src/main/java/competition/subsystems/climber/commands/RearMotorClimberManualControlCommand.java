package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.RearMotorClimberSubsystem;

public class RearMotorClimberManualControlCommand extends BaseMotorClimberManualControlCommand {

    @Inject
    public RearMotorClimberManualControlCommand(RearMotorClimberSubsystem rear, OperatorInterface oi) {
        super(rear, oi);
    }

    @Override
    public double getHumanInput() {
        return oi.operatorGamepad.getRightVector().y;
    }

    @Override
    public void initialize() {
        log.info("Initialize");
    }
}