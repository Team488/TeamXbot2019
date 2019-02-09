package competition.subsystems.gripper.commands;

import com.google.inject.Inject;

import competition.subsystems.gripper.GripperSubsystem;
import xbot.common.command.BaseCommand;

public class GrabDiscCommand extends BaseCommand{
    final GripperSubsystem gripperSubsystem;

    @Inject
    public GrabDiscCommand(GripperSubsystem gripper) {
        gripperSubsystem = gripper;
    }
    @Override
    public void initialize() {
        log.info("initializing");
        gripperSubsystem.grabHatch();
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}