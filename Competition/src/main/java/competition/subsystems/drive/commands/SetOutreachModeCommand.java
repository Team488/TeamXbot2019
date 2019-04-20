package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

public class SetOutreachModeCommand extends BaseCommand {

    DriveSubsystem drive;
    ElevatorSubsystem elevator;
    boolean enableOutreachMode;

    @Inject
    public SetOutreachModeCommand(DriveSubsystem drive, ElevatorSubsystem elevator) {
        this.drive = drive;
        this.elevator = elevator;
    }

    public void setOutreachModeEnabled(boolean value) {
        enableOutreachMode = value;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        drive.setOutreachModeEnabled(enableOutreachMode);
        elevator.setOutreachModeEnabled(enableOutreachMode);
    }

    @Override
    public void execute() {
        
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}