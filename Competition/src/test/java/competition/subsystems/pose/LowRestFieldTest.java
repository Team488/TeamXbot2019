package competition.subsystems.pose;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import competition.BaseCompetitionTest;
import xbot.common.math.FieldPose;
import xbot.common.subsystems.drive.RabbitPoint;

public class LowRestFieldTest extends BaseCompetitionTest {

    @Test
    public void testHabCollision() {
        LowResField f = new LowResField();
        FieldPose robotPose = new FieldPose(15, 15, 90);
        RabbitPoint finalPoint = new RabbitPoint(290, 40, -90);
        
        List<RabbitPoint> path = f.generatePath(robotPose, finalPoint);
        Obstacle hab = f.getObstacles().get(1);

        assertEquals(hab.topLeft.x, path.get(0).pose.getPoint().x, 0.001);
        assertEquals(hab.topLeft.y, path.get(0).pose.getPoint().y, 0.001);

        assertEquals(hab.topRight.x, path.get(1).pose.getPoint().x, 0.001);
        assertEquals(hab.topRight.y, path.get(1).pose.getPoint().y, 0.001);

        assertEquals(finalPoint.pose.getPoint().x, path.get(2).pose.getPoint().x, 0.001);
        assertEquals(finalPoint.pose.getPoint().y, path.get(2).pose.getPoint().y, 0.001);
    }

    @Test
    public void testMultipleCollisions() {
        LowResField f = new LowResField();
        FieldPose robotPose = new FieldPose(15, 15, 90);
        RabbitPoint finalPoint = new RabbitPoint(220, 306, 180);
        
        List<RabbitPoint> path = f.generatePath(robotPose, finalPoint);
        Obstacle cargoShip = f.getObstacles().get(0);
        Obstacle hab = f.getObstacles().get(1);

        assertEquals(hab.topLeft.x, path.get(0).pose.getPoint().x, 0.001);
        assertEquals(hab.topLeft.y, path.get(0).pose.getPoint().y, 0.001);

        assertEquals(cargoShip.bottomRight.x, path.get(1).pose.getPoint().x, 0.001);
        assertEquals(cargoShip.bottomRight.y, path.get(1).pose.getPoint().y, 0.001);

        assertEquals(finalPoint.pose.getPoint().x, path.get(2).pose.getPoint().x, 0.001);
        assertEquals(finalPoint.pose.getPoint().y, path.get(2).pose.getPoint().y, 0.001);
    }

    // multiple tests suggest that path generation takes 4125 nanoseconds, or 0.004 milliseconds, on a desktop PC.
    // The robot tries to get all its work done in under 20 milliseconds, so this seems like plenty of headroom.
    public void testGenerationPerformance() {
        LowResField f = new LowResField();
        FieldPose robotPose = new FieldPose(15, 15, 90);
        RabbitPoint finalPoint = new RabbitPoint(290, 40, -90);
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            List<RabbitPoint> path = f.generatePath(robotPose, finalPoint);
        }
        long stop = System.nanoTime();
        long diff = stop - start;
        System.out.println("Nanotime: " + diff);
    }
}