package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.BaseMotorClimberSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseMotorClimberManualControlCommand extends BaseCommand {

    protected BaseMotorClimberSubsystem climber;
    protected OperatorInterface oi;

    @Inject
    public BaseMotorClimberManualControlCommand(BaseMotorClimberSubsystem climber, OperatorInterface oi) {
        this.climber = climber;
        this.oi = oi;

        this.requires(climber);
    }

    public abstract double getHumanInput();

    @Override
    public void execute() {

        double leftPower = getHumanInput();
        double rightPower = leftPower;

        if (climber.getLeftLimitSwitchPressed()) {
            leftPower = climber.getRetroRocketPower();
        }
        if (climber.getRightLimitSwitchPressed()) {
            rightPower = climber.getRetroRocketPower();
        }

        climber.setLeftPower(leftPower);
        climber.setRightPower(rightPower);
    }
}