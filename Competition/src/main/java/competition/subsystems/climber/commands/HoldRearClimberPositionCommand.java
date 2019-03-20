package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.subsystems.climber.BaseMotorClimberSubsystem.EncoderAdjustment;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.RearMotorClimberSubsystem;
import xbot.common.command.BaseSetpointCommand;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class HoldRearClimberPositionCommand extends BaseSetpointCommand {

    final RearMotorClimberSubsystem rear;
    final PIDManager leftPid;
    final PIDManager rightPid;
    final OperatorInterface oi;
    final DoubleProperty joystickGoalScalingProp;
    final DoubleProperty joystickDeadbandProp;

    @Inject
    public HoldRearClimberPositionCommand(RearMotorClimberSubsystem rear, PIDFactory pf, OperatorInterface oi, PropertyFactory propFactory) {
        super(rear);
        propFactory.setPrefix(getPrefix());
        this.rear = rear;
        this.oi = oi;
        requires(rear);
        leftPid = pf.createPIDManager(getPrefix() + "Left", 0.01, 0, 0, 1, -0.2);
        rightPid = pf.createPIDManager(getPrefix() + "Right", 0.01, 0, 0, 1, -0.2);

        joystickGoalScalingProp = propFactory.createPersistentProperty("JoystickGoalScaling", 100);
        joystickDeadbandProp = propFactory.createPersistentProperty("JoystickDeadband", 0.07);
    }

    @Override
    public void initialize() {
        log.info("Initialize");
        leftPid.reset();
        rightPid.reset();
    }

    @Override
    public void execute() {
        double leftPower = leftPid.calculate(rear.getLeftTickGoal(), rear.getLeftTicks(EncoderAdjustment.Hard));
        double rightPower = rightPid.calculate(rear.getRightTickGoal(), rear.getRightTicks(EncoderAdjustment.Hard));

        rear.setLeftPower(leftPower);
        rear.setRightPower(rightPower);

        double humanInput = oi.operatorGamepad.getRightVector().y;
        if (Math.abs(humanInput) <= joystickDeadbandProp.get()) {
            humanInput = 0;
        }

        double goalAdjustment = humanInput * joystickGoalScalingProp.get();

        double udpatedGoal = rear.getLeftTickGoal() + goalAdjustment;
        rear.setBothTickGoals(udpatedGoal);
    }
}