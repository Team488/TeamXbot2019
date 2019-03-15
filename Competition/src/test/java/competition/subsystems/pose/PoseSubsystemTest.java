package competition.subsystems.pose;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.pose.PoseSubsystem.FieldLandmark;
import competition.subsystems.pose.PoseSubsystem.Side;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.drive.RabbitPoint.PointTerminatingType;
import xbot.common.subsystems.drive.RabbitPoint.PointType;

public class PoseSubsystemTest extends BaseCompetitionTest {

    PoseSubsystem pose;

    @Override
    public void setUp() {
        super.setUp();
        this.pose = injector.getInstance(PoseSubsystem.class);
    }

    @Test
    public void getPathWithWaypoints() {
        
        List<RabbitPoint> points = pose.getPathToLandmark(Side.Left, FieldLandmark.NearCargoShip, false);
        assertEquals(2, points.size(), 0.001);
        
        assertEquals(PointTerminatingType.Continue, points.get(0).terminatingType);
        assertEquals(PointType.PositionOnly, points.get(0).pointType);
        System.out.println(points.get(0).pose.toString());

        assertEquals(PointTerminatingType.Stop, points.get(1).terminatingType);
        assertEquals(PointType.PositionAndHeading, points.get(1).pointType);
        System.out.println(points.get(1).pose.toString());
    }

    @Test
    public void testUpdatingPosition() {
        pose.setCurrentPosition(20, 40);
        pose.setCurrentHeading(-80);
        pose.updatePositionDueToGripperActuation();

        assertEquals(22.75, pose.getCurrentFieldPose().getPoint().x, 0.001);
    }
}