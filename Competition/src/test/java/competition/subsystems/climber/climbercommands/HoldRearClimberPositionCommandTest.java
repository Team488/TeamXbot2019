package competition.subsystems.climber.climbercommands;

import org.junit.Test;

import competition.subsystems.climber.commands.HoldRearClimberPositionCommand;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class HoldRearClimberPositionCommandTest extends HoldRobotLevelCommandTest {

    HoldRearClimberPositionCommand command;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(HoldRearClimberPositionCommand.class);
    }

    @Test
    public void testAtLevel() {
        command.initialize();
        command.execute();
        assertPower(0);
    }

    @Test
    public void testRobotFalling() {
        command.initialize();
        command.execute();
        assertPower(0);

        ((MockCANTalon)rear.leftMotor).setPosition(-1000);
        ((MockCANTalon)rear.rightMotor).setPosition(-1000);

        command.execute();
        assertPower(1);
    }

    @Test
    public void testRobotRaising() {
        command.initialize();
        command.execute();
        assertPower(0);

        ((MockCANTalon)rear.leftMotor).setPosition(1000);
        ((MockCANTalon)rear.rightMotor).setPosition(1000);

        command.execute();
        assertPower(-1);
    }
}