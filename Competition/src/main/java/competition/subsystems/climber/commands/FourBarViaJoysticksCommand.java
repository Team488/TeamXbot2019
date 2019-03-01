package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.FourBarSubsystem;
import xbot.common.command.BaseCommand;

public class FourBarViaJoysticksCommand extends BaseCommand {

    OperatorInterface oi;
    FourBarSubsystem fourBar;

    @Inject
    public FourBarViaJoysticksCommand(OperatorInterface oi, FourBarSubsystem fourBar) {
        this.oi = oi;
        this.fourBar = fourBar;
        this.requires(fourBar);
    }

    @Override
    public void initialize() {
        log.info("Initialized");
    }

    @Override
    public void execute() {
        fourBar.setPower(oi.operatorGamepad.getLeftVector().y);
    }
}