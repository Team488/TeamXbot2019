package competition.subsystems.pose;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.PoseSubsystem.Side;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.FieldPose;

public class SetPoseToFieldLandmarkCommand extends BaseCommand {

    PoseSubsystem pose;
    private boolean setHeading;
    private FieldLandmark chosenLandmark;
    private Side side;

    @Inject
    public SetPoseToFieldLandmarkCommand(PoseSubsystem pose, CommonLibFactory clf) {
        this.pose = pose;
    }

    /**
     * When this command runs, should it force the heading in addition to position?
     * @param value if true, forces heading
     */
    public void forceHeading(boolean value) {
        setHeading = value;
    }

    public void setLandmark(Side side, FieldLandmark landmark) {
        chosenLandmark = landmark;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        log.info("Robot position will be set to landmark " + chosenLandmark.toString());

        FieldPose landmarkLocation = pose.getFieldPoseForLandmark(side, chosenLandmark);
        pose.setCurrentPosition(landmarkLocation.getPoint().x, landmarkLocation.getPoint().y);

        if (setHeading) {
            pose.setCurrentHeading(landmarkLocation.getHeading().getValue());
        }
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}