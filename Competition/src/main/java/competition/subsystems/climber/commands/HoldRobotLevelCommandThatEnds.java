package competition.subsystems.climber.commands;

import com.google.inject.Inject;

import competition.subsystems.climber.RearMotorClimberSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.math.PIDFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class HoldRobotLevelCommandThatEnds extends HoldRobotLevelCommand {
    
    final DoubleProperty criticalPitchProp;
    
    @Inject
    public HoldRobotLevelCommandThatEnds(RearMotorClimberSubsystem rear, PoseSubsystem pose, PropertyFactory propFactory, PIDFactory pf) {
        super(rear, pose, propFactory, pf);
        criticalPitchProp = propFactory.createPersistentProperty("CriticalPitchAngle", -45);
    }

    @Override
    public boolean isFinished() {
        // If we tilt very far forward, that (hopefully) means that we have fallen onto HAB 3.
        return pose.getRobotPitch() < criticalPitchProp.get();
    }
}