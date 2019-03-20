package competition.subsystems.pose;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class NearestLoadingStationTest extends BaseCompetitionTest {

    PoseSubsystem pose;

    @Override
    public void setUp() {
        super.setUp();
        pose = this.injector.getInstance(PoseSubsystem.class);
    }

    @Test
    public void getNearestLoadingStation() {
        pose.setCurrentPosition(30.0, 0.0);
        assertFalse(pose.isRightLoadingStationCloser());

        pose.setCurrentPosition(200.0, 0.0);
        assertTrue(pose.isRightLoadingStationCloser());
    }
}