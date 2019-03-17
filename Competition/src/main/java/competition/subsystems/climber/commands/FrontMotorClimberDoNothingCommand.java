package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.subsystems.climber.FrontMotorClimberSubsystem;
import xbot.common.command.BaseCommand;

public class FrontMotorClimberDoNothingCommand extends BaseCommand {

    FrontMotorClimberSubsystem climber;

    @Inject
    public FrontMotorClimberDoNothingCommand(FrontMotorClimberSubsystem climber) {
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