package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.subsystems.climber.RearMotorClimberSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class HoldRobotLevelCommand extends BaseCommand {

    RearMotorClimberSubsystem rear;
    PoseSubsystem pose;
    PIDManager pitchPid;
    PIDManager rollPid;
    final DoubleProperty criticalPitchProp;

    @Inject
    public HoldRobotLevelCommand(RearMotorClimberSubsystem rear, PoseSubsystem pose, PropertyFactory propFactory, PIDFactory pf) {
        this.rear = rear;
        this.pose = pose;
        requires(rear);
        propFactory.setPrefix(getPrefix());
        pitchPid = pf.createPIDManager(getPrefix() + "PitchControl", 0.05, 0, 0, 1, -0.2);
        rollPid = pf.createPIDManager(getPrefix() + "RollControl", 0.05, 0, 0, 1, -0.2);
        criticalPitchProp = propFactory.createPersistentProperty("CriticalPitchAngle", -10);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        // Positive pitch means front rising. Negative pitch means front lowering.
        // Positive roll means right lowering. Negative roll means right rising.
        double currentPitch = pose.getRobotPitch();
        double currentRoll = pose.getRobotRoll();

        double pitchPower = pitchPid.calculate(0, currentPitch);
        double rollPower = -rollPid.calculate(0, currentRoll);
        rear.setLiftAndTilt(pitchPower, rollPower);
    }

    @Override
    public boolean isFinished() {
        // If we tilt very far forward, that (hopefully) means that we have fallen onto HAB 3.
        return pose.getRobotPitch() < criticalPitchProp.get();
    }
}