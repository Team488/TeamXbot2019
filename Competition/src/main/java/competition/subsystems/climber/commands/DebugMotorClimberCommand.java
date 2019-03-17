package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber.FrontMotorClimberSubsystem;
import competition.subsystems.climber.RearMotorClimberSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;

public class DebugMotorClimberCommand extends BaseCommand {

        FrontMotorClimberSubsystem front;
        RearMotorClimberSubsystem rear;
        OperatorInterface oi;
        ElevatorSubsystem elevator;

    @Inject
    public DebugMotorClimberCommand(FrontMotorClimberSubsystem front, RearMotorClimberSubsystem rear, ElevatorSubsystem elevator, OperatorInterface oi) {
        this.front = front;
        this.rear = rear;
        this.oi = oi;
        this.elevator = elevator;
        this.requires(front);
        this.requires(rear);
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
        double leftTilt = oi.operatorGamepad.getLeftVector().x;
        double rightTilt =  oi.operatorGamepad.getRightVector().x;
        double tilt = 0;
        if (Math.abs(Math.signum(leftTilt) - Math.signum(rightTilt)) < 0.001) {
            // if the signs are the same, just take the biggest one.
            tilt = Math.max(Math.abs(leftTilt), Math.abs(rightTilt)) * Math.signum(leftTilt);
        } else {
            // if the signs are different, add them together
            tilt = leftTilt + rightTilt;
        }

        tilt = MathUtils.constrainDoubleToRobotScale(tilt);

        front.setLiftAndTilt(frontClimb, tilt);
        rear.setLiftAndTilt(rearClimb, tilt);
        elevator.setPower(0);
    }

}