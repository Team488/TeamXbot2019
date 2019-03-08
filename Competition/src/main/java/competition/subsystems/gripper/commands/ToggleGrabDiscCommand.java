package competition.subsystems.gripper.commands;

import com.google.inject.Inject;

import competition.subsystems.gripper.GripperSubsystem;
import xbot.common.command.BaseCommand;

public class ToggleGrabDiscCommand extends BaseCommand {
    
    final GripperSubsystem gripperSubsystem;

    @Inject
    public ToggleGrabDiscCommand(GripperSubsystem gripper) {
        gripperSubsystem = gripper;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        gripperSubsystem.toggle();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}