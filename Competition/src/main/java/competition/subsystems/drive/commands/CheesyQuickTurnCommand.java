package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;

public class CheesyQuickTurnCommand extends BaseCommand {

    DriveSubsystem drive;

    @Inject
    public CheesyQuickTurnCommand(DriveSubsystem drive) {
        this.drive = drive;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        drive.setQuickTurn(true);
    }

    @Override
    public void execute() {

    }

    @Override
    public void end() {
        super.end();
        drive.setQuickTurn(false);
    }

}