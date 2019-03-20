package competition.commandgroups.operatorcommandgroups;

import com.google.inject.Inject;

import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem.HatchLevel;
import competition.subsystems.elevator.commands.SetElevatorTickGoalCommand;
import competition.subsystems.gripper.commands.GrabDiscCommand;
import competition.subsystems.gripper.commands.RetractGripperCommand;
import xbot.common.command.BaseCommandGroup;

public class SafeMode extends BaseCommandGroup {

    @Inject
    public SafeMode(RetractGripperCommand retract, 
    GrabDiscCommand grab, SetElevatorTickGoalCommand setElevator, ElevatorSubsystem elevator) {

        this.addParallel(grab);
        this.addParallel(retract);
        setElevator.setGoal(elevator.getTickHeightForLevel(HatchLevel.Low));
        this.addParallel(setElevator);
    }
}