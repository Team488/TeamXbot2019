package competition.subsystems.gripper.commands;

import com.google.inject.Inject;

import competition.subsystems.gripper.GripperSubsystem;

public class ExtendGripperCommand extends ModifyGripperExtensionCommand {
    
    @Inject
    public ExtendGripperCommand(GripperSubsystem gripper) {
        super(gripper);
        this.setExtend(true);
    }
}