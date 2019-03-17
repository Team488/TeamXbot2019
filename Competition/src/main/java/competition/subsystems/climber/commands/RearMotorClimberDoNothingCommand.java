package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.subsystems.climber.RearMotorClimberSubsystem;
import xbot.common.command.BaseCommand;

public class RearMotorClimberDoNothingCommand extends BaseCommand {

    RearMotorClimberSubsystem climber;

    @Inject
    public RearMotorClimberDoNothingCommand(RearMotorClimberSubsystem climber) {
        this.climber = climber;
        this.requires(climber);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        climber.setLiftAndTilt(0, 0);
    }
}