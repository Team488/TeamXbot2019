package competition.subsystems.climber.climbercommands;

import org.junit.Test;

import competition.subsystems.climber.ClimberSubsystemTest;
import competition.subsystems.climber.commands.DeployFrontCommand;

public class DeployFrontCommandTest extends ClimberSubsystemTest {
    DeployFrontCommand deployFront;    

    public void setUp() {
        super.setUp();
        deployFront = injector.getInstance(DeployFrontCommand.class);
    }

    @Test 
    public void testExecute() {
        verifyIntialClimberState();
        deployFront.execute();
        verifyFinalFrontState(true, false);
    }
}