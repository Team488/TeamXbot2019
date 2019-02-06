package competition.subsystems.drive;

import java.awt.EventQueue;
import java.util.List;

import competition.subsystems.pose.LowResField;
import xbot.common.math.FieldPose;
import xbot.common.math.PlanarTestVisualizer;
import xbot.common.subsystems.drive.RabbitPoint;

public class CompetitionPurePursuitTest extends PlanarTestVisualizer {

    CompetitionPurePursuitTest(List<RabbitPoint> points) {
        super(points);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    LowResField f = new LowResField();
                    FieldPose robot = new FieldPose(15, 15, 90);
                    RabbitPoint finalPoint = new RabbitPoint(212, 260, 180);
                    List<RabbitPoint> points = f.generatePath(robot, finalPoint);

                    PlanarTestVisualizer window = new CompetitionPurePursuitTest(points);
                    window.frmLinearTestVisualizer.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}