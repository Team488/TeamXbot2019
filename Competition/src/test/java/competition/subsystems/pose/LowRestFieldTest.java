package competition.subsystems.pose;

import java.util.List;

import org.junit.Test;

import competition.BaseCompetitionTest;
import xbot.common.math.FieldPose;
import xbot.common.subsystems.drive.RabbitPoint;

public class LowRestFieldTest extends BaseCompetitionTest {

    @Test
    public void testGeneration() {
        LowResField f = new LowResField();
        FieldPose robotPose = new FieldPose(15, 15, 90);
        RabbitPoint finalPoint = new RabbitPoint(290, 40, -90);

        List<RabbitPoint> path = f.generatePath(robotPose, finalPoint);
    }

    // multiple tests suggest that path generation takes 2516 nanoseconds, or 0.002 milliseconds, on a desktop PC.
    // This gives me some hope for the robot.
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