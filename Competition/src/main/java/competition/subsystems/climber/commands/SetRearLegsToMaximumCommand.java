package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.subsystems.climber.RearMotorClimberSubsystem;
import xbot.common.command.BaseCommand;

public class SetRearLegsToMaximumCommand extends BaseCommand {

    RearMotorClimberSubsystem rear;

    @Inject
    public SetRearLegsToMaximumCommand(RearMotorClimberSubsystem rear) {
        this.rear = rear;
    }

    @Override
    public void initialize() {
        log.info("Initialized");
        rear.setTickGoalsToSafeMaximum();
    }

    @Override
    public void execute() {
        
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}