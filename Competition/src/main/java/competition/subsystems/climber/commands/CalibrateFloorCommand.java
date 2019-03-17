package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.subsystems.climber.FrontMotorClimberSubsystem;
import competition.subsystems.climber.RearMotorClimberSubsystem;
import xbot.common.command.BaseCommand;

public class CalibrateFloorCommand extends BaseCommand {

    FrontMotorClimberSubsystem front;
    RearMotorClimberSubsystem rear;

    @Inject
    public CalibrateFloorCommand(FrontMotorClimberSubsystem front, RearMotorClimberSubsystem rear) {
        this.front = front;
        this.rear = rear;
        requires(front);
        requires(rear);
    }

    @Override
    public void initialize() {
        log.info("Initialized");
        front.calibrateFloor();
        rear.calibrateFloor();
    }

    @Override
    public void execute() {
        
    }
}