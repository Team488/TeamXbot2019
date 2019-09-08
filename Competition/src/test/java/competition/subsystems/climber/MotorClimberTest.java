package competition.subsystems.climber;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class MotorClimberTest extends BaseCompetitionTest {

    protected FrontMotorClimberSubsystem front;
    protected RearMotorClimberSubsystem rear;

    @Override
    public void setUp() {
        super.setUp();
        front = injector.getInstance(FrontMotorClimberSubsystem.class);
        rear = injector.getInstance(RearMotorClimberSubsystem.class);
        front.setPowerFactor(1);
        rear.setPowerFactor(1);
    }

    @Test
    public void goUp() {
        front.setLiftAndTilt(1, 0);
        rear.setLiftAndTilt(1, 0);

        verifyClimberPower(1, 1, 1, 1);
    }

    @Test
    public void tilt() {
        front.setLiftAndTilt(1, 0.5);
        rear.setLiftAndTilt(1, 0.5);

        verifyClimberPower(0.5, 1, 0.5, 1);
    }

    protected void verifyClimberPower(double frontLeft, double frontRight, double rearLeft, double rearRight) {
        assertEquals(frontLeft, front.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(frontRight, front.rightMotor.getMotorOutputPercent(), 0.001);
        assertEquals(rearLeft, rear.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(rearRight, rear.rightMotor.getMotorOutputPercent(), 0.001);
    }
}