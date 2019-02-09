package competition.subsystems.gripper.commands;

import com.google.inject.Inject;

import competition.subsystems.gripper.GripperSubsystem;
import xbot.common.command.BaseCommand;

public class ReleaseDiscCommand extends BaseCommand{
    final GripperSubsystem gripperSubsystem;

    @Inject
    public ReleaseDiscCommand(GripperSubsystem gripper) {
        gripperSubsystem = gripper;
    }
    @Override
    public void initialize() {
        log.info("initializing");
        gripperSubsystem.releaseHatch();
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}