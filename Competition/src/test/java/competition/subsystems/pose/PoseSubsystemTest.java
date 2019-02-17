package competition.subsystems.pose;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
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
    @Ignore
    public void getPathWithWaypoints() {
        
        List<RabbitPoint> points = pose.getPathToLandmark(Side.Left, FieldLandmark.NearCargoShip, false);
        assertEquals(3, points.size(), 0.001);
        
        assertEquals(PointTerminatingType.Continue, points.get(0).terminatingType);
        assertEquals(PointType.PositionOnly, points.get(0).pointType);
        System.out.println(points.get(0).pose.toString());

        assertEquals(PointTerminatingType.Continue, points.get(1).terminatingType);
        assertEquals(PointType.PositionAndHeading, points.get(1).pointType);
        System.out.println(points.get(1).pose.toString());

        assertEquals(PointTerminatingType.Stop, points.get(2).terminatingType);
        assertEquals(PointType.PositionAndHeading, points.get(2).pointType);
        System.out.println(points.get(2).pose.toString());

        assertTrue("Middle point should be further left than final point", points.get(1).pose.getPoint().x < points.get(2).pose.getPoint().x);
    }
}