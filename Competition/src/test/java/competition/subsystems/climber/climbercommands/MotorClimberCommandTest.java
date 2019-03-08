package competition.subsystems.climber.climbercommands;

import org.junit.Test;

import competition.subsystems.climber.MotorClimberTest;
import competition.subsystems.climber.commands.MotorClimberCommand;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class MotorClimberCommandTest extends MotorClimberTest {

    MotorClimberCommand command;

    @Override
    public void setUp() {
        super.setUp();
        this.command = injector.getInstance(MotorClimberCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void goUp() {
        ((MockFTCGamepad)oi.operatorGamepad).setLeftStick(new XYPair(0, 1));
        command.initialize();
        command.execute();

        verifyClimberPower(1, 1, 0, 0);

        ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, 1));
        command.initialize();
        command.execute();

        verifyClimberPower(1, 1, 1, 1);
    }

    @Test
    public void tilt() {
        ((MockFTCGamepad)oi.operatorGamepad).setLeftStick(new XYPair(0.5, 1));
        command.initialize();
        command.execute();

        verifyClimberPower(0.5, 1, -0.5, 0.5);

        ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0.6, 1));
        command.initialize();
        command.execute();

        verifyClimberPower(0.4, 1, 0.4, 1);
    }
}