package competition;

import xbot.common.injection.ElectricalContract;

public abstract class ElectricalContract2019 extends ElectricalContract {

    // Drive motors
    public abstract boolean isDriveReady();

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

    public abstract DeviceInfo getElevatorFollowerMotor();

    public abstract DeviceInfo getElevatorCalibrationSensor();

    public abstract DeviceInfo getBrakeSolenoid();
   
    public abstract boolean isElevatorReady();

    public abstract boolean isElevatorLimitSwitchReady();

    public abstract boolean doesDriveHaveThreeMotors();

    public abstract boolean isClimberReady();

    public abstract DeviceInfo getFrontDeploySolenoid();

    public abstract DeviceInfo getFrontRetractSolenoid();

    public abstract DeviceInfo getBackDeploySolenoid();

    public abstract DeviceInfo getBackRetractSolenoid();
    
    public abstract boolean isFourBarReady();

    public abstract DeviceInfo getFourBarMaster();

    public abstract DeviceInfo getFourBarMasterEncoder();

    public abstract DeviceInfo getFourBarFollower();

    public abstract boolean isFrontMotorClimberReady();

    public abstract boolean isRearMotorClimberReady();

    public abstract DeviceInfo getFrontLeftClimber();
    public abstract DeviceInfo getFrontLeftEncoder();
    public abstract DeviceInfo getFrontLeftLimit();

    public abstract DeviceInfo getFrontRightClimber();
    public abstract DeviceInfo getFrontRightEncoder();
    public abstract DeviceInfo getFrontRightLimit();
    
    public abstract DeviceInfo getRearLeftClimber();
    public abstract DeviceInfo getRearLeftEncoder();
    public abstract DeviceInfo getRearLeftLimit();

    public abstract DeviceInfo getRearRightClimber();
    public abstract DeviceInfo getRearRightEncoder();
    public abstract DeviceInfo getRearRightLimit();
}