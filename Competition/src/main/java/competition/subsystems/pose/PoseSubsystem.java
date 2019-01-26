package competition.subsystems.pose;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.FieldPosePropertyManager;
import xbot.common.math.XYPair;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {

    DriveSubsystem drive;

    public enum FieldLandmark {
        HabLevelOne,
        RightLoadingStation,
        CargoShipSix
    }

    private Map<FieldLandmark, FieldPosePropertyManager> landmarkToLocation;
    private final FieldPosePropertyManager habLevelOneManager;
    private final FieldPosePropertyManager rightLoadingStationManager;
    private final FieldPosePropertyManager cargoShipSixManager;

    @Inject
    public PoseSubsystem(CommonLibFactory clf, XPropertyManager propManager, DriveSubsystem drive) {
        super(clf, propManager);
        this.drive = drive;

        landmarkToLocation = new HashMap<FieldLandmark, FieldPosePropertyManager>();

        habLevelOneManager = clf.createFieldPosePropertyManager(getPrefix() + "HabLevelOne", 14*12, 4*12, 90);
        landmarkToLocation.put(FieldLandmark.HabLevelOne, habLevelOneManager);
        
        rightLoadingStationManager = clf.createFieldPosePropertyManager(getPrefix() + "RightLoadingStation", 23*12, 10, -90);
        landmarkToLocation.put(FieldLandmark.RightLoadingStation, rightLoadingStationManager);
        
        cargoShipSixManager = clf.createFieldPosePropertyManager(getPrefix() + "CargoShipSix", 16*12, 12*12, 180);
        landmarkToLocation.put(FieldLandmark.CargoShipSix, cargoShipSixManager);
    }

    @Override
    protected double getLeftDriveDistance() {
        return drive.getLeftTotalDistance();
    }

    @Override
    protected double getRightDriveDistance() {
        return drive.getRightTotalDistance();
    }

    public FieldPose getFieldPoseForLandmark(FieldLandmark landmark) {
        if (landmark != null && landmarkToLocation.containsKey(landmark)) {
            return landmarkToLocation.get(landmark).getPose();
        }
        else {
            log.warn("Could not find a location for landmark " + landmark.toString() + "! Returning 0, 0, 90");
            return new FieldPose(new XYPair(), new ContiguousHeading(90));
        }
    }

}