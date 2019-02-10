package competition.subsystems.gripper.commands;

import com.google.inject.Inject;

import competition.subsystems.gripper.GripperSubsystem;

public class RetractGripperCommand extends ModifyGripperExtensionCommand {
    
    @Inject
    public RetractGripperCommand(GripperSubsystem gripper) {
        super(gripper);
        this.setExtend(false);
    }
}