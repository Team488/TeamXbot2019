package competition;

import xbot.common.injection.ElectricalContract;

public abstract class ElectricalContract2019 extends ElectricalContract {

    // Drive motors
    public abstract DeviceInfo getLeftDriveMaster();

    public abstract DeviceInfo getLeftDriveFollower();

    public abstract DeviceInfo getLeftDriveFollowerSecond();

    public abstract DeviceInfo getRightDriveMaster();

    public abstract DeviceInfo getRightDriveFollower();

    public abstract DeviceInfo getRightDriveFollowerSecond();

    public abstract DeviceInfo getLeftDriveMasterEncoder();

    public abstract DeviceInfo getRightDriveMasterEncoder();


    public abstract DeviceInfo getGripperDiscSolenoid();

    public abstract DeviceInfo getGripperExtensionSolenoid();

    public abstract DeviceInfo getGripperSensor();

    public abstract boolean isGripperReady();

    public abstract DeviceInfo getElevatorMasterMotor();

    public abstract DeviceInfo getElevatorMasterEncoder();

    public abstract DeviceInfo getElevatorCalibrationSensor();
   
    public abstract boolean isElevatorReady();

    public abstract boolean isElevatorLimitSwitchReady();

    public abstract boolean doesDriveHaveThreeMotors();
}