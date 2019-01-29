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
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {

    DriveSubsystem drive;

    public enum FieldLandmark {
        CargoShipRightThree,
        CargoShipFrontRight,
        HabRightTwo,
        HabRightOne,
        HabTouchingRampRight,
        PanelLoadRight,
        RocketFrontRight,

        CargoShipLeftThree,
        CargoShipFrontLeft,
        HabLeftTwo,
        HabLeftOne,
        HabTouchingRampLeft,
        PanelLoadLeft,
        RocketFrontLeft,
        
    }

    private Map<FieldLandmark, FieldPosePropertyManager> landmarkToLocation;
    private final FieldPosePropertyManager cargoShipRightThreeManager;
    private final FieldPosePropertyManager cargoShipFrontRightManager;
    private final FieldPosePropertyManager habRightTwoManager;
    private final FieldPosePropertyManager habRightOneManager;
    private final FieldPosePropertyManager habTouchingRampRightManager;
    private final FieldPosePropertyManager panelLoadRightManager;
    private final FieldPosePropertyManager rocketFrontRightManager;

    private final FieldPosePropertyManager cargoShipLeftThreeManager;
    private final FieldPosePropertyManager cargoShipFrontLeftManager;
    private final FieldPosePropertyManager habLeftTwoManager;
    private final FieldPosePropertyManager habLeftOneManager;
    private final FieldPosePropertyManager habTouchingRampLeftManager;
    private final FieldPosePropertyManager panelLoadLeftManager;
    private final FieldPosePropertyManager rocketFrontLeftManager;
    
    

    @Inject
    public PoseSubsystem(CommonLibFactory clf, XPropertyManager propManager, DriveSubsystem drive) {
        super(clf, propManager);
        this.drive = drive;

        landmarkToLocation = new HashMap<FieldLandmark, FieldPosePropertyManager>();

        //right side
        cargoShipRightThreeManager = clf.createFieldPosePropertyManager(getPrefix() + "CargoShipRightThree", 212, 262, 180);
        landmarkToLocation.put(FieldLandmark.CargoShipRightThree, cargoShipRightThreeManager);
          
        cargoShipFrontRightManager= clf.createFieldPosePropertyManager(getPrefix() + "CargoShipFrontRight", 176, 208, 90);
        landmarkToLocation.put(FieldLandmark.CargoShipFrontRight, cargoShipFrontRightManager);

        habRightTwoManager = clf.createFieldPosePropertyManager(getPrefix() + "HabRightTwo", 204, 22, 90);
        landmarkToLocation.put(FieldLandmark.HabRightTwo, habRightTwoManager);

        habRightOneManager = clf.createFieldPosePropertyManager(getPrefix() + "HabRightOne", 204, 72, 90);
        landmarkToLocation.put(FieldLandmark.HabRightOne, habRightOneManager);

        habTouchingRampRightManager = clf.createFieldPosePropertyManager(getPrefix() + "HabTouchingRamp", 204, 118, 90);
        landmarkToLocation.put(FieldLandmark.HabTouchingRampRight, habTouchingRampRightManager);

        panelLoadRightManager = clf.createFieldPosePropertyManager(getPrefix() + "PanelLoadRight", 300, 23, 270);
        landmarkToLocation.put(FieldLandmark.PanelLoadRight, panelLoadRightManager);

        rocketFrontRightManager = clf.createFieldPosePropertyManager(getPrefix() + "RocketFrontRight", 302, 197, 84);
        landmarkToLocation.put(FieldLandmark.RocketFrontRight, rocketFrontRightManager);

        //left side
        cargoShipLeftThreeManager = clf.createFieldPosePropertyManager(getPrefix() + "CargoShipRightThree", 112, 262, 180);
        landmarkToLocation.put(FieldLandmark.CargoShipRightThree, cargoShipRightThreeManager);
          
        cargoShipFrontLeftManager= clf.createFieldPosePropertyManager(getPrefix() + "CargoShipFrontRight", 148, 208, 90);
        landmarkToLocation.put(FieldLandmark.CargoShipFrontRight, cargoShipFrontRightManager);

        habLeftTwoManager = clf.createFieldPosePropertyManager(getPrefix() + "HabRightTwo", 120, 22, 90);
        landmarkToLocation.put(FieldLandmark.HabRightTwo, habRightTwoManager);

        habLeftOneManager = clf.createFieldPosePropertyManager(getPrefix() + "HabRightOne", 120, 72, 90);
        landmarkToLocation.put(FieldLandmark.HabRightOne, habRightOneManager);

        habTouchingRampLeftManager = clf.createFieldPosePropertyManager(getPrefix() + "HabTouchingRamp", 120, 118, 90);
        landmarkToLocation.put(FieldLandmark.HabTouchingRampLeft, habTouchingRampRightManager);

        panelLoadLeftManager = clf.createFieldPosePropertyManager(getPrefix() + "PanelLoadRight", 24, 23, 270);
        landmarkToLocation.put(FieldLandmark.PanelLoadRight, panelLoadRightManager);

        rocketFrontLeftManager = clf.createFieldPosePropertyManager(getPrefix() + "RocketFrontRight", 22, 197, 84);
        landmarkToLocation.put(FieldLandmark.RocketFrontRight, rocketFrontRightManager);

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