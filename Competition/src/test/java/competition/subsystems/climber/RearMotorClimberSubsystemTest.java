package competition.subsystems.climber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class RearMotorClimberSubsystemTest extends BaseCompetitionTest {

    RearMotorClimberSubsystem rear;
    double leftPower;
    double rightPower;

    @Override
    public void setUp() {
        super.setUp();
        rear = injector.getInstance(RearMotorClimberSubsystem.class);

    }

    @Test
    public void positiveLiftAndTilt() {
        updatePower();
        assertZero();

        rear.positiveLiftAndTilt(1.0, 1.0);
        updatePower();
        assertEquals(0.0, leftPower, 0.001);
        assertEquals(1.0, rightPower, 0.001);

        rear.positiveLiftAndTilt(-1.0, -1.0);
        updatePower();
        assertZero();

        rear.positiveLiftAndTilt(-1.0, 1.0);
        updatePower();
        assertZero();

        rear.positiveLiftAndTilt(1.0, -1.0);
        updatePower();
        assertEquals(1.0, leftPower, 0.001);
        assertEquals(0.0, rightPower, 0.001);
    }

    public void updatePower() {
        leftPower = rear.leftMotor.getMotorOutputPercent();
        rightPower = rear.rightMotor.getMotorOutputPercent();
    }

    public void assertZero() {
        assertEquals(0.0, leftPower, 0.001);
        assertEquals(0.0, rightPower, 0.001);
    }

}