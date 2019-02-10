package competition.subsystems.gripper.commands;

import com.google.inject.Inject;

import competition.subsystems.gripper.GripperSubsystem;
import xbot.common.command.BaseCommand;

public class ModifyGripperExtensionCommand extends BaseCommand {

    protected GripperSubsystem gripper;
    private boolean extend = true;

    @Inject
    public ModifyGripperExtensionCommand(GripperSubsystem gripper) {
        this.gripper = gripper;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    protected void setExtend(boolean extend) {
        this.extend = extend;
    }

    @Override
    public void execute() {
        gripper.setExtension(extend);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}