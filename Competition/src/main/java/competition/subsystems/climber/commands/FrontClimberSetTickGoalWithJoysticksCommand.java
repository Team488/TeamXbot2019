package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.FrontMotorClimberSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseSetpointCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class FrontClimberSetTickGoalWithJoysticksCommand extends BaseSetpointCommand {

    final FrontMotorClimberSubsystem front;
    final OperatorInterface oi;
    final DoubleProperty tickScaleProperty;

    @Inject
    public FrontClimberSetTickGoalWithJoysticksCommand(
        FrontMotorClimberSubsystem front, 
        OperatorInterface oi, 
        PropertyFactory propFactory,
        ElevatorSubsystem elevator) {
        super(front);
        this.front = front;
        this.oi = oi;
        propFactory.setPrefix(this);
        this.tickScaleProperty = propFactory.createPersistentProperty("Joystick Tick Goal scale", 100.0);
        // to take ownership of the left joystick
        this.requires(elevator);
    }

    @Override
    public void initialize() {
        log.info("Initialize");
    }

    @Override
    public void execute() {
        double up = oi.operatorGamepad.getLeftVector().y * tickScaleProperty.get();
        double tilt = oi.operatorGamepad.getLeftVector().x * tickScaleProperty.get();

        double leftGoal = front.getLeftTickGoal() + up - tilt;
        double rightGoal = front.getRightTickGoal() + up + tilt;
        front.setLeftTickGoal(leftGoal);
        front.setRightTickGoal(rightGoal);
    }
}