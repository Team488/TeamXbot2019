package competition.subsystems.vision.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class RotateToVisionTargetCommand extends BaseCommand {

    final OperatorInterface oi;
    final VisionSubsystem visionSubsystem;
    final PoseSubsystem pose;
    final DriveSubsystem drive;
    Double goal = null;
    HeadingModule hm;
    double rotation;
    private boolean continuousAcquisition = false;


    @Inject
    public RotateToVisionTargetCommand(OperatorInterface oi, 
    VisionSubsystem visionSubsystem, CommonLibFactory clf,
    DriveSubsystem drive, PoseSubsystem pose) {
        this.pose = pose;
        this.oi = oi;
        this.drive = drive;
        this.visionSubsystem = visionSubsystem;
        hm = clf.createHeadingModule(drive.getRotateToHeadingPid());
        this.requires(visionSubsystem);
        this.requires(drive);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        // reset no goal state
        goal = null;
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
        if((goal == null || continuousAcquisition) && visionSubsystem.isTargetInView()) {
            double relativeAngle = visionSubsystem.getAngleToTarget();
            goal = pose.getCurrentHeading().shiftValue(relativeAngle).getValue();
        }

        if(goal != null) {
            rotation = hm.calculateHeadingPower(goal); 
        } else {
            rotation = 0;
            hm.reset();
        }

        drive.drive(new XYPair(0, oi.driverGamepad.getLeftVector().y), rotation);
    }
}