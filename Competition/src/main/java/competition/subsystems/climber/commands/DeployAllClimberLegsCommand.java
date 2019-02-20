package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.ClimberSubsystem;
import xbot.common.command.BaseCommand;

public class DeployAllClimberLegsCommand extends BaseCommand {
    final ClimberSubsystem climber;
    final OperatorInterface oi;

    @Inject
    public DeployAllClimberLegsCommand(OperatorInterface oi, ClimberSubsystem climber) {
        this.oi = oi;
        this.climber = climber;
        this.requires(this.climber);
    }

    @Override
    public void initialize() {
        log.info("Intializing");
    }

    @Override
    public void execute() {
        climber.deployBack();
        climber.deployFront();
    }
    
}