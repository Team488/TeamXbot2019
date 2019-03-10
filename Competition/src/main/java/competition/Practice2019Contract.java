package competition;

public class Practice2019Contract extends ElectricalContract2019 {

    @Override
    public boolean isDriveReady() {
        return true;
    }

    @Override
    public DeviceInfo getLeftDriveMaster() {
        return new DeviceInfo(33, false);
    }

    @Override
    public DeviceInfo getLeftDriveFollower() {
        return new DeviceInfo(34, false);
    }

    @Override
    public DeviceInfo getLeftDriveFollowerSecond() {
        return new DeviceInfo(32, false);
    }

    @Override
    public DeviceInfo getRightDriveMaster() {
        return new DeviceInfo(22, true);
    }

    @Override
    public DeviceInfo getRightDriveFollower() {
        return new DeviceInfo(21, true);
    }

    @Override
    public DeviceInfo getRightDriveFollowerSecond() {
        return new DeviceInfo(23, true);
    }

    @Override
    public DeviceInfo getLeftDriveMasterEncoder() {
        return new DeviceInfo(0, true);
    }

    @Override
    public DeviceInfo getRightDriveMasterEncoder() {
        return new DeviceInfo(0, true);
    }

    @Override
    public DeviceInfo getElevatorMasterMotor() {
        return new DeviceInfo(25, false);
    }

    @Override
    public DeviceInfo getElevatorFollowerMotor() {
        return new DeviceInfo(50, false);
    }

    @Override
    public DeviceInfo getElevatorMasterEncoder() {
        return new DeviceInfo(5, false);
    }

    @Override
    public DeviceInfo getElevatorCalibrationSensor() {
        return new DeviceInfo(9, true);
    }

    public DeviceInfo getBrakeSolenoid() {
        return new DeviceInfo(2, false);
    }

    @Override
    public boolean isElevatorReady() {
        return true;
    }

    @Override
    public boolean isElevatorLimitSwitchReady() {
        return false;
    }

    @Override
    public boolean doesDriveHaveThreeMotors() {
        return true;
    }

    public DeviceInfo getGripperDiscSolenoid() {
        return new DeviceInfo(0, true);
    }

    @Override
    public DeviceInfo getGripperSensor() {
        return new DeviceInfo(1, false);
    }

    @Override
    public boolean isGripperReady() {
        return true;
    }

    @Override
    public DeviceInfo getGripperExtensionSolenoid() {
        return new DeviceInfo(1, false);
    }

    @Override
    public boolean isClimberReady() {
        return false;
    }

    @Override
    public DeviceInfo getFrontDeploySolenoid() {
        return new DeviceInfo(6, false);
    }

    @Override
    public DeviceInfo getFrontRetractSolenoid() {
        return new DeviceInfo(3, false);
    }

    @Override
    public DeviceInfo getBackDeploySolenoid() {
        return new DeviceInfo(4, false);
    }

    @Override
    public DeviceInfo getBackRetractSolenoid() {
        return new DeviceInfo(5, false);
    }

    @Override
    public boolean isFourBarReady() {
        return false;
    }

    @Override
    public DeviceInfo getFourBarMaster() {
        return new DeviceInfo(1, false);
    }

    @Override
    public DeviceInfo getFourBarFollower() {
        return new DeviceInfo(2, true);
    }

    @Override
    public DeviceInfo getFourBarMasterEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public boolean isFrontMotorClimberReady() {
        return false;
    }

    @Override
    public DeviceInfo getFrontLeftClimber() {
        return new DeviceInfo(35, false);
    }

    @Override
    public DeviceInfo getFrontRightClimber() {
        return new DeviceInfo(20, false);
    }

    @Override
    public DeviceInfo getRearLeftClimber() {
        return new DeviceInfo(48, false);
    }

    @Override
    public DeviceInfo getRearRightClimber() {
        return new DeviceInfo(49, false);
    }

    @Override
    public boolean isRearMotorClimberReady() {
        return false;
    }

}