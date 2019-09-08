package competition.subsystems.elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import competition.BaseCompetitionTest;
import competition.subsystems.gripper.GripperSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public abstract class BaseElevatorTest extends BaseCompetitionTest {

    protected ElevatorSubsystem elevatorSubsystem;
    protected GripperSubsystem gripper;

    @Override
    public void setUp() {
        super.setUp();
        elevatorSubsystem = this.injector.getInstance(ElevatorSubsystem.class);
        gripper = this.injector.getInstance(GripperSubsystem.class);
        gripper.setExtension(true);
    }

    protected void elevatorReady() {
        assertTrue(contract.isElevatorReady());
        assertTrue(contract.isElevatorLimitSwitchReady());
    }

    protected double getMasterPower() {
        return elevatorSubsystem.master.getMotorOutputPercent();
    }

    protected void setElevatorTicks(int ticks) {
        ((MockCANTalon)(elevatorSubsystem.master)).setPosition(ticks);
    }

    protected void setElevatorPositionForMovingUpwards() {
        int currentPosition = elevatorSubsystem.getElevatorHeightInRawTicks();
        setElevatorTicks(currentPosition - elevatorSubsystem.getRaiseArmRetractDistanceInTicks() - 1);
    }

    protected void makeElevatorRaiseHandlingRatchet() {
        elevatorSubsystem.raise();
        assertTrue(elevatorSubsystem.master.getMotorOutputPercent() < 0);
        setElevatorPositionForMovingUpwards();
        elevatorSubsystem.raise();
        assertEquals(1*elevatorSubsystem.getMaximumPower(), elevatorSubsystem.master.getMotorOutputPercent(), 0.001);
    }

}