package competition.subsystems.vision.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class RotateToVisionTargetCommand extends BaseCommand {

    final OperatorInterface oi;
    final VisionSubsystem visionSubsystem;
    final PoseSubsystem pose;
    final DriveSubsystem drive;
    final DoubleProperty visionDeltaLimitProp;
    Double goal = null;
    boolean goalFrozen = false;
    HeadingModule hm;
    double rotation;
    private boolean continuousAcquisition = false;
    final DoubleProperty constantOffsetProp;
    final DoubleProperty freezeOffsetProp;
    final BooleanProperty frozenProp;

    @Inject
    public RotateToVisionTargetCommand(OperatorInterface oi, 
            VisionSubsystem visionSubsystem, CommonLibFactory clf,
            DriveSubsystem drive, PoseSubsystem pose, PropertyFactory propFactory) {
        propFactory.setPrefix(this);
        this.pose = pose;
        this.oi = oi;
        this.drive = drive;
        this.visionSubsystem = visionSubsystem;
        this.visionDeltaLimitProp = propFactory.createPersistentProperty("Freeze Angle Delta Deg", 5.0);
        constantOffsetProp = propFactory.createPersistentProperty("ConstantOffset", 0);
        freezeOffsetProp = propFactory.createPersistentProperty("FreezeOffset", 0);
        frozenProp = propFactory.createEphemeralProperty("IsFrozen", false);

        hm = clf.createHeadingModule(drive.getRotateToHeadingPid());
        this.requires(visionSubsystem);
        this.requires(drive);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        // reset no goal state
        goal = null;
        goalFrozen = false;
        frozenProp.set(false);
    }
    
    /*
     * @param continuousAcquisition the continuousAcquisition to set
     */
    public void setContinuousAcquisition(boolean continuousAcquisition) {
        this.continuousAcquisition = continuousAcquisition;
    }

    @Override
    public void execute() {
        // if not yet acquired, look for target
        if(!goalFrozen && (goal == null || continuousAcquisition) && visionSubsystem.isTargetInView()) {
            double relativeAngle = visionSubsystem.getAngleToTarget();
            relativeAngle += constantOffsetProp.get();
            double newGoal = pose.getCurrentHeading().shiftValue(relativeAngle).getValue();

            // see if the goal has jumped like crazy from last tick, if so vision is probably 
            // giving us bad data as we near the target so stop listening to it
            if(goal != null && Math.abs(goal - newGoal) > visionDeltaLimitProp.get()) {
                goalFrozen = true;
                goal += freezeOffsetProp.get();
            } else {
                goal = newGoal;
            }
        }

        if(goal != null) {
            rotation = hm.calculateHeadingPower(goal); 
        } else {
            rotation = 0;
            hm.reset();
        }
        frozenProp.set(goalFrozen);
        drive.drive(new XYPair(0, oi.driverGamepad.getLeftVector().y), rotation);
    }
}