package competition.subsystems.climber;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class MotorClimberTest extends BaseCompetitionTest {

    protected MotorClimberSubsystem climber;

    @Override
    public void setUp() {
        super.setUp();
        climber = injector.getInstance(MotorClimberSubsystem.class);
    }

    @Test
    public void goUp() {
        climber.setFrontPower(1, 0);
        climber.setRearPower(1, 0);

        verifyClimberPower(1, 1, 1, 1);
    }

    @Test
    public void tilt() {
        climber.setFrontPower(1, 0.5);
        climber.setRearPower(1, 0.5);

        verifyClimberPower(0.5, 1, 0.5, 1);
    }

    protected void verifyClimberPower(double frontLeft, double frontRight, double rearLeft, double rearRight) {
        assertEquals(frontLeft, climber.frontLeft.getMotorOutputPercent(), 0.001);
        assertEquals(frontRight, climber.frontRight.getMotorOutputPercent(), 0.001);
        assertEquals(rearLeft, climber.rearLeft.getMotorOutputPercent(), 0.001);
        assertEquals(rearRight, climber.rearRight.getMotorOutputPercent(), 0.001);
    }
}