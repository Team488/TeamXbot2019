package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.subsystems.climber.MotorClimberSubsystem;
import xbot.common.command.BaseCommand;

public class MotorClimberDoNothingCommand extends BaseCommand {

    MotorClimberSubsystem climber;

    @Inject
    public MotorClimberDoNothingCommand(MotorClimberSubsystem climber) {
        this.climber = climber;
        this.requires(climber);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        climber.setFrontPower(0, 0);
        climber.setRearPower(0, 0);
    }
}