package competition;

import com.google.inject.Singleton;

@Singleton
public class Competition2018Contract extends ElectricalContract2019 {

    @Override
    public boolean isDriveReady() {
        return true;
    }
    
    @Override
    public DeviceInfo getLeftDriveMaster() {
        return new DeviceInfo(34, true);
    }

    @Override
    public DeviceInfo getLeftDriveFollower() {
        return new DeviceInfo(35, true);
    }

    @Override
    public DeviceInfo getLeftDriveFollowerSecond() {
        return new DeviceInfo(-5, false);
    }

    @Override
    public DeviceInfo getLeftDriveMasterEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getRightDriveMaster() {
        return new DeviceInfo(21, false);
    }

    @Override
    public DeviceInfo getRightDriveFollower() {
        return new DeviceInfo(20, false);
    }

    @Override
    public DeviceInfo getRightDriveFollowerSecond() {
        return new DeviceInfo(-4, false);
    }

    @Override
    public DeviceInfo getRightDriveMasterEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getGripperDiscSolenoid() {
        return new DeviceInfo(-1, false);
    }

    @Override
    public DeviceInfo getGripperSensor() {
        return new DeviceInfo(-1, false);
    }

    @Override
    public DeviceInfo getElevatorMasterMotor() {
        return new DeviceInfo(-3, false);
    }

    @Override
    public DeviceInfo getElevatorFollowerMotor(){
        return new DeviceInfo(-10, false);
    }

    @Override
    public DeviceInfo getElevatorMasterEncoder() {
        return new DeviceInfo(-1, false);
    }

    @Override
    public DeviceInfo getBrakeSolenoid() {
        return new DeviceInfo(-49, false);
    }

    @Override
    public boolean isGripperReady() {
        return false;
    }

    @Override
    public DeviceInfo getElevatorCalibrationSensor() {
        return new DeviceInfo(-2, false);
    }

    @Override
    public boolean isElevatorReady() {
        return false;
    }

    @Override
    public boolean doesDriveHaveThreeMotors() {
        return false;
    }

    @Override
    public boolean isElevatorLimitSwitchReady() {
        return false;
    }
    
    public DeviceInfo getGripperExtensionSolenoid() {
        return null;
    }

    public boolean isClimberReady() {
        return false;
    }

    @Override
    public DeviceInfo getFrontDeploySolenoid() {
        return null;
    }

    @Override
    public DeviceInfo getFrontRetractSolenoid() {
        return null;
    }

    @Override
    public DeviceInfo getBackDeploySolenoid() {
        return null;
    }

    @Override
    public DeviceInfo getBackRetractSolenoid() {
        return null;
    }

    @Override
    public boolean isFourBarReady() {
        return false;
    }

    @Override
    public DeviceInfo getFourBarMaster() {
        return null;
    }

    @Override
    public DeviceInfo getFourBarFollower() {
        return null;
    }

    @Override
    public DeviceInfo getFourBarMasterEncoder() {
        return null;
    }

    @Override
    public boolean isFrontMotorClimberReady() {
        return false;
    }

    @Override
    public DeviceInfo getFrontLeftClimber() {
        return null;
    }

    @Override
    public DeviceInfo getFrontRightClimber() {
        return null;
    }

    @Override
    public DeviceInfo getRearLeftClimber() {
        return null;
    }

    @Override
    public DeviceInfo getRearRightClimber() {
        return null;
    }

    @Override
    public boolean isRearMotorClimberReady() {
        return false;
    }

    @Override
    public DeviceInfo getRearLeftEncoder() {
        return null;
    }

    @Override
    public DeviceInfo getRearRightEncoder() {
        return null;
    }

    @Override
    public DeviceInfo getFrontLeftEncoder() {
        return null;
    }

    @Override
    public DeviceInfo getFrontRightEncoder() {
        return null;
    }

    @Override
    public DeviceInfo getFrontLeftLimit() {
        return null;
    }

    @Override
    public DeviceInfo getFrontRightLimit() {
        return null;
    }

    @Override
    public DeviceInfo getRearLeftLimit() {
        return null;
    }

    @Override
    public DeviceInfo getRearRightLimit() {
        return null;
    }

    @Override
    public boolean invertVisionData() {
        return true;
    }
}