package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.subsystems.climber.FrontMotorClimberSubsystem;
import competition.subsystems.climber.BaseMotorClimberSubsystem.EncoderAdjustment;
import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

public class FrontClimberPositionMaintainerCommand extends BaseCommand {

    FrontMotorClimberSubsystem front;
    PIDManager leftPid;
    PIDManager rightPid;

    @Inject
    public FrontClimberPositionMaintainerCommand(FrontMotorClimberSubsystem front, PIDFactory pf) {
        this.front = front;
        this.requires(front);

        leftPid = pf.createPIDManager(getPrefix() + "Left", 0.01, 0, 0, 1, -0.2);
        rightPid = pf.createPIDManager(getPrefix() + "Right", 0.01, 0, 0, 1, -0.2);
    }

    @Override
    public void initialize() {
        log.info("Initialize");
        front.setTickGoalsToCurrent(EncoderAdjustment.Floor);
        leftPid.reset();
        rightPid.reset();
    }

    @Override
    public void execute() {
        double leftPower = leftPid.calculate(front.getLeftTickGoal(), front.getLeftTicks(EncoderAdjustment.Floor));
        double rightPower = rightPid.calculate(front.getRightTickGoal(), front.getRightTicks(EncoderAdjustment.Floor));

        front.setLeftPower(leftPower);
        front.setRightPower(rightPower);
    }
}