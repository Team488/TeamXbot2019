package competition.subsystems.pose;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

import com.google.inject.Inject;

import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.drive.RabbitPoint.PointTerminatingType;
import xbot.common.subsystems.drive.RabbitPoint.PointType;

public class LowResField {

    List<Obstacle> obstacles;

    @Inject
    public LowResField() {
        obstacles = new ArrayList<Obstacle>();
        // CargoShip
        obstacles.add(new Obstacle(162, 324, 84, 248, "CargoShip"));
        // Hab
        Obstacle hab = new Obstacle(161, 47, 182, 47, "Hab");
        // Disable points next to the alliance station
        hab.defaultBottomLeft = false;
        hab.defaultBottomRight = false;
        hab.resetCorners();
        obstacles.add(hab);
        // Rocket
        FieldPose leftRocketPose = new FieldPose(18, 230, 0);
        FieldPose rightRocketPose = PoseSubsystem.flipFieldPose(leftRocketPose);
        Obstacle leftRocket = new Obstacle(leftRocketPose.getPoint().x, leftRocketPose.getPoint().y, 60, 60, "LeftRocket");
        // Disable points outside the field
        leftRocket.defaultTopLeft = false;
        leftRocket.defaultBottomLeft = false;
        leftRocket.resetCorners();
        Obstacle rightRocket = new Obstacle(rightRocketPose.getPoint().x, rightRocketPose.getPoint().y, 60, 60, "RightRocket");
        rightRocket.defaultTopRight = false;
        rightRocket.defaultBottomRight = false;
        rightRocket.resetCorners();
        // Add both rocket points
        obstacles.add(leftRocket);
        obstacles.add(rightRocket);
    }

    public List<Obstacle> getObstacles() {
        return new ArrayList<Obstacle>(obstacles);
    }

    public List<RabbitPoint> generatePath(FieldPose robotPose, RabbitPoint targetPoint) {
        var path = new ArrayList<RabbitPoint>();
        var rabbitStack = new Stack<RabbitPoint>();
        var sortedObstacles = new TreeMap<Double, Obstacle>();
        // Start from the final point, generate a path backwards.
        // Check to see if there are any intersections. 
        
        RabbitPoint focalPoint = targetPoint;
        boolean collision = true;

        for (Obstacle o : obstacles) {
            o.resetCorners();
        }

        while (collision) {
            collision = false;
            // Sort all the obstacles by distance
            sortedObstacles.clear();
            for (Obstacle o : obstacles) {
                double distance = o.getDistanceToCenter(focalPoint.pose.getPoint());
                sortedObstacles.put(distance, o);
            }
            // find the first intersection, if any
            for (Double d : sortedObstacles.keySet()) {
                Obstacle o = sortedObstacles.get(d);
                
                if (o.intersectsLine(robotPose.getPoint().x, robotPose.getPoint().y, focalPoint.pose.getPoint().x, focalPoint.pose.getPoint().y)) {
                    // The line collidies with this obstacle!
                    collision = true;
                    // find the average intersection point
                    XYPair averageIntersection = o.getIntersectionAveragePoint(robotPose.getPoint(), focalPoint.pose.getPoint());
                    // find the nearest corner
                    XYPair nearestCorner = o.getClosestCornerToPoint(averageIntersection);
                    // turn that corner into a position-only point
                    RabbitPoint cornerPoint = 
                        new RabbitPoint(new FieldPose(nearestCorner.x, nearestCorner.y, 0), PointType.PositionOnly, PointTerminatingType.Continue);
                    // Save the current point
                    rabbitStack.add(focalPoint);
                    // Change the focus to the corner point
                    focalPoint = cornerPoint;
                    break;
                }
            }
            if (!collision) {
                // Didn't hit anything! this is the last point.
            }
        }

        // create the path. Add the focal point, then pop all the other points.
        path.add(focalPoint);
        while (rabbitStack.size() > 0) {
            path.add(rabbitStack.pop());
        }       
        
        return path;
    }
}