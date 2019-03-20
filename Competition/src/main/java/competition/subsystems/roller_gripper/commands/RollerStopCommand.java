package competition.subsystems.roller_gripper.commands;

import com.google.inject.Inject;

import competition.subsystems.roller_gripper.RollerGripperSubsystem;
import xbot.common.command.BaseCommand;

public class RollerStopCommand extends BaseCommand {

    final RollerGripperSubsystem subsystem;

    @Inject
    public RollerStopCommand(RollerGripperSubsystem subsystem) {
        this.subsystem = subsystem;
        this.requires(this.subsystem);
    }

    @Override
    public void initialize() {
        log.info("Initialized");
    }

    @Override
    public void execute() {
        this.subsystem.stop();
    }
}