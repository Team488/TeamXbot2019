package competition.subsystems.pose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.FieldPosePropertyManager;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.drive.RabbitPoint.PointTerminatingType;
import xbot.common.subsystems.drive.RabbitPoint.PointType;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {

    DriveSubsystem drive;
    LowResField field;

    public enum Side {
        Left,
        Right
    }

    public enum FieldLandmark {
        FrontCargoShip,
        NearCargoShip,
        MidCargoShip,
        FarCargoShip,
        NearRocket,
        MidRocket,
        FarRocket,
        LoadingStation,
        HabLevelTwo,
        HabLevelOne,
        HabLevelZero
    }

    private final Map<String, FieldPosePropertyManager> landmarkToLocation;

    // Some pre-computed interstitial points. Waypoints do not need headings.
    private final FieldPosePropertyManager leftNearRocketWaypoint;
    private final FieldPosePropertyManager leftFarRocketWaypoint;
    private final FieldPosePropertyManager leftCargoShipWaypoint; // all side cargo locations use the same waypoint
    private final DoubleProperty distanceFromCenterOfRobot;
    private final DoubleProperty visionBackoffDistance;
    private CommonLibFactory clf;

    @Inject
    public PoseSubsystem(CommonLibFactory clf, XPropertyManager propManager, DriveSubsystem drive, LowResField field) {
        super(clf, propManager);
        this.drive = drive;
        this.clf = clf;
        this.field = field;

        landmarkToLocation = new HashMap<String, FieldPosePropertyManager>();
        distanceFromCenterOfRobot = propManager.createPersistentProperty(getPrefix() + "DistanceFromCenterOfBot", 18);
        visionBackoffDistance = propManager.createPersistentProperty(getPrefix() + "VisionBackoffDistance", 36);

        // New definitions for each landmark:
        // <Side> <RelativeLocation> <Entity>
        // Side: Left/Right
        // RelativeLocation: Front, Near, Mid, Far
        // The four left side CargoShip hatches would be as follows: LeftFrontCargoShip, LeftNearCargoShip, LeftMidCargoShip, LeftFarCargoShip
        // The two left side Rocket hatches would be as follows: LeftNearRocket, LeftFarRocket (and LeftMidRocket if you are doing cargo)

        // These waypoints represent good places to navigate to on the field before doing a final approach. 
        leftNearRocketWaypoint = clf.createFieldPosePropertyManager(getPrefix() + "LeftNearRocketWaypoint", 40, 135, 0);
        leftFarRocketWaypoint = clf.createFieldPosePropertyManager(getPrefix() + "LeftFarRocketWaypoint", 70, 260, 0);
        leftCargoShipWaypoint = clf.createFieldPosePropertyManager(getPrefix() + "LeftCargoShipWaypoint", 80, 200, 0);
        
        // Near CargoShip
        createLandmarks(FieldLandmark.NearCargoShip, 112, 262, 0);
        // Mid CargoShip
        createLandmarks(FieldLandmark.MidCargoShip, 112, 283, 0);
        // Far CargoShip
        createLandmarks(FieldLandmark.FarCargoShip, 112, 305, 0);
        // Front CargoShip
        createLandmarks(FieldLandmark.FrontCargoShip, 148, 208, 90);
        // Hab Level Two
        createLandmarks(FieldLandmark.HabLevelTwo, 120, 22, 90);
        // Hab Level One
        createLandmarks(FieldLandmark.HabLevelOne, 120, 72, 90);
        // Hab Level Zero
        createLandmarks(FieldLandmark.HabLevelZero, 120, 118, 90);
        // Loading Stations
        createLandmarks(FieldLandmark.LoadingStation, 24, 23, 270);
        // Near Rocket
        createLandmarks(FieldLandmark.NearRocket, 22, 197, 103);
        // Far Rocket
        createLandmarks(FieldLandmark.FarRocket, 22, 264, 180);

    }

    private void createLandmarks(FieldLandmark landmark, double x, double y, double heading) {
        FieldPose leftPose = new FieldPose(x, y, heading);
        String leftName = createLandmarkKey(Side.Left, landmark);
        String rightName = createLandmarkKey(Side.Right, landmark);
        FieldPosePropertyManager left = clf.createFieldPosePropertyManager(getPrefix() +  leftName, leftPose);
        FieldPosePropertyManager right = clf.createFieldPosePropertyManager(getPrefix() + rightName, flipFieldPose(leftPose));
        landmarkToLocation.put(leftName, left);
        landmarkToLocation.put(rightName, right);
    }

    private FieldPose getWaypointForLandmark(Side side, FieldLandmark landmark) {
        // A relatively safe default.
        FieldPose candidate = new FieldPose(20,20,90);
        
        switch (landmark) {
            // This uses switch statement fallthrough to group several cases together
            case NearCargoShip:
            case MidCargoShip:
            case FarCargoShip:
            case MidRocket:
                candidate = leftCargoShipWaypoint.getPose();
                break;
            case LoadingStation:
            case FrontCargoShip: 
            case NearRocket:
                candidate = leftNearRocketWaypoint.getPose();
                break;
            case FarRocket:
                candidate = leftFarRocketWaypoint.getPose();
                break;
            default:
                log.warn("Could not find any waypoint for " + landmark + "! Will head back towards player station.");
                break;
        }

        if (side == Side.Right) {
            candidate = flipFieldPose(candidate);
        }

        return candidate;
    }

    public List<RabbitPoint> getPathToLandmark(Side side, FieldLandmark landmark, boolean automaticWaypoints) {
        var path = new ArrayList<RabbitPoint>();
        String landmarkKey = createLandmarkKey(side, landmark);

        // If this point isn't fully registereds, escape.
        if (!landmarkToLocation.containsKey(landmarkKey)) {
            log.warn("Tried to find a path to a landmark, but could not find it in the maps!");
            return path;
        }        
        
        FieldPose finalPose = landmarkToLocation.get(landmarkKey).getPose();
        FieldPose robotFlushPose = finalPose.getPointAlongPoseLine(-distanceFromCenterOfRobot.get());
        FieldPose visionPose = robotFlushPose.getPointAlongPoseLine(-visionBackoffDistance.get());
        
        RabbitPoint visionPoint = new RabbitPoint(visionPose, PointType.PositionAndHeading, PointTerminatingType.Continue);        
       // RabbitPoint finalPoint = new RabbitPoint(finalPose, PointType.PositionAndHeading, PointTerminatingType.Stop);
        RabbitPoint robotFlushPoint = new RabbitPoint(robotFlushPose, PointType.PositionAndHeading, PointTerminatingType.Stop);

        if (automaticWaypoints) {
            List<RabbitPoint> generatedPoints = field.generatePath(getCurrentFieldPose(), visionPoint);
            for (RabbitPoint p : generatedPoints) {
                path.add(p);
            }
        } else {
            FieldPose waypointPose = getWaypointForLandmark(side, landmark);
            RabbitPoint waypoint = new RabbitPoint(waypointPose, PointType.PositionOnly, PointTerminatingType.Continue);
            path.add(waypoint);
        }
        
        path.add(visionPoint);
        path.add(robotFlushPoint);

      // path.add(finalPoint);
        return path;
    }

    @Override
    protected double getLeftDriveDistance() {
        return drive.getLeftTotalDistance();
    }

    @Override
    protected double getRightDriveDistance() {
        return drive.getRightTotalDistance();
    }

    public FieldPose getFieldPoseForLandmark(Side side, FieldLandmark landmark) {
        String landmarkKey = createLandmarkKey(side, landmark);        
        if (landmarkToLocation.containsKey(landmarkKey)) {
            return landmarkToLocation.get(landmarkKey).getPose();
        }
        else {
            log.warn("Could not find a location for landmark " + landmark.toString() + "! Returning 0, 0, 90");
            return new FieldPose(new XYPair(), new ContiguousHeading(90));
        }
    }

    private String createLandmarkKey(Side side, FieldLandmark landmark) {
        if (side == null || landmark == null) {
            return "";
        }
        return side.toString() + landmark.toString();
    }

    public static FieldPose flipFieldPose(FieldPose current) {
        return new FieldPose(new XYPair((12*27)- current.getPoint().x, current.getPoint().y), new ContiguousHeading(180- current.getHeading().getValue()));
    }
    

}
