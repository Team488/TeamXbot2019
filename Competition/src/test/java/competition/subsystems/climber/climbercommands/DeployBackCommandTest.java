package competition.subsystems.climber.climbercommands;

import org.junit.Test;

import competition.subsystems.climber.ClimberSubsystemTest;
import competition.subsystems.climber.commands.DeployBackCommand;

public class DeployBackCommandTest extends ClimberSubsystemTest {
    DeployBackCommand deployBack;
    ClimberSubsystemTest test;

    

    public void setUp() {
        super.setUp();
        deployBack = injector.getInstance(DeployBackCommand.class);
    }

    @Test 
    public void testExecute() {
        verifyIntialClimberState();
        deployBack.execute();
        verifyFinalBackState(true, false);
    }
}