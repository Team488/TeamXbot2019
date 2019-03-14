package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.MotorClimberSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;

public class MotorClimberCommand extends BaseCommand {

        MotorClimberSubsystem climber;
        OperatorInterface oi;
        ElevatorSubsystem elevator;

    @Inject
    public MotorClimberCommand(MotorClimberSubsystem climber, ElevatorSubsystem elevator, OperatorInterface oi) {
        this.climber = climber;
        this.oi = oi;
        this.elevator = elevator;
        this.requires(climber);
        this.requires(elevator);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double frontClimb = oi.operatorGamepad.getLeftVector().y;
        double rearClimb = oi.operatorGamepad.getRightVector().y;
        double leftTilt = MathUtils.squareAndRetainSign(oi.operatorGamepad.getLeftVector().x);
        double rightTilt =  MathUtils.squareAndRetainSign(oi.operatorGamepad.getRightVector().x);
        double tilt = 0;
        if (Math.abs(Math.signum(leftTilt) - Math.signum(rightTilt)) < 0.001) {
            // if the signs are the same, just take the biggest one.
            tilt = Math.max(Math.abs(leftTilt), Math.abs(rightTilt)) * Math.signum(leftTilt);
        } else {
            // if the signs are different, add them together
            tilt = leftTilt + rightTilt;
        }

        tilt = MathUtils.constrainDoubleToRobotScale(tilt);

        climber.setFrontPower(frontClimb, tilt);
        climber.setRearPower(rearClimb, tilt);
        elevator.setPower(0);
    }

}