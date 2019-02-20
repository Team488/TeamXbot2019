package competition.subsystems.climber.climbercommands;

import org.junit.Test;

import competition.subsystems.climber.ClimberSubsystemTest;
import competition.subsystems.climber.commands.RetractBackCommand;

public class RetractBackCommandTest extends ClimberSubsystemTest {
    RetractBackCommand retractBack;

    public void setUp() {
        super.setUp();
        retractBack = injector.getInstance(RetractBackCommand.class);
    }

    @Test 
    public void testExecute() {
        verifyIntialClimberState();
        retractBack.execute();
        verifyFinalBackState(false, true);
    }
}