package competition.subsystems.climber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class ClimberSubsystemTest extends BaseCompetitionTest {

    ClimberSubsystem climber;

    @Override
    public void setUp() {
        super.setUp();
        climber = this.injector.getInstance(ClimberSubsystem.class);
    }

    @Test
    public void deployFrontTest() {
        verifyIntialClimberState();
        climber.deployFront();
        verifyFinalFrontState(true, false);
    }
    @Test
    public void retractFrontTest() {
        verifyIntialClimberState();
        climber.retractFront();
        verifyFinalFrontState(false, true);
    }
    @Test
    public void deployBackTest() {
        verifyIntialClimberState();
        climber.deployBack();
        verifyFinalBackState(true, false);
    }
    @Test
    public void retractBackTest() {
        verifyIntialClimberState();
        climber.retractBack();
        verifyFinalBackState(false, true);
    }

    protected void verifyIntialClimberState() {
        assertFalse(climber.frontDeploySolenoid.getAdjusted());
        assertFalse(climber.frontRetractSolenoid.getAdjusted());
        assertFalse(climber.backDeploySolenoid.getAdjusted());
        assertFalse(climber.backRetractSolenoid.getAdjusted());
    }

    protected void verifyFinalFrontState(boolean deploy, boolean retract) {
        assertEquals(deploy, climber.frontDeploySolenoid.getAdjusted());
        assertEquals(retract, climber.frontRetractSolenoid.getAdjusted());
    }

    protected void verifyFinalBackState(boolean deploy, boolean retract) {
        assertEquals(deploy, climber.backDeploySolenoid.getAdjusted());
        assertEquals(retract, climber.backRetractSolenoid.getAdjusted());
    }
}