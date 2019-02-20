package competition.subsystems.climber.climbercommands;

import org.junit.Test;

import competition.subsystems.climber.ClimberSubsystemTest;
import competition.subsystems.climber.commands.RetractFrontCommand;

public class RetractFrontCommandTest extends ClimberSubsystemTest {
    RetractFrontCommand retractFront;
    ClimberSubsystemTest test;

    public void setUp() {
        super.setUp();
        retractFront = injector.getInstance(RetractFrontCommand.class);
    }

    @Test 
    public void testExecute() {
        verifyIntialClimberState();
        retractFront.execute();
        verifyFinalFrontState(false, true);
    }
}