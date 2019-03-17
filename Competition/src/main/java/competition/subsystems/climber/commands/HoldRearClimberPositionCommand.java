package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.subsystems.climber.RearMotorClimberSubsystem;
import competition.subsystems.climber.BaseMotorClimberSubsystem.EncoderAdjustment;
import xbot.common.command.BaseCommand;
import xbot.common.command.BaseSetpointCommand;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

public class HoldRearClimberPositionCommand extends BaseSetpointCommand {

    final RearMotorClimberSubsystem rear;
    final PIDManager leftPid;
    final PIDManager rightPid;

    @Inject
    public HoldRearClimberPositionCommand(RearMotorClimberSubsystem rear, PIDFactory pf) {
        super(rear);
        this.rear = rear;
        requires(rear);
        leftPid = pf.createPIDManager(getPrefix() + "Left", 0.01, 0, 0, 1, -0.2);
        rightPid = pf.createPIDManager(getPrefix() + "Right", 0.01, 0, 0, 1, -0.2);
    }

    @Override
    public void initialize() {
        log.info("Initialize");
        rear.setTickGoalsToCurrent(EncoderAdjustment.Raw);
        leftPid.reset();
        rightPid.reset();
    }

    @Override
    public void execute() {
        double leftPower = leftPid.calculate(rear.getLeftTickGoal(), rear.getLeftTicks(EncoderAdjustment.Raw));
        double rightPower = rightPid.calculate(rear.getRightTickGoal(), rear.getRightTicks(EncoderAdjustment.Raw));

        rear.setLeftPower(leftPower);
        rear.setRightPower(rightPower);
    }
}