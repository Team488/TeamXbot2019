package competition;

public class RoboxContract extends ElectricalContract2019 {

    @Override
    public boolean isDriveReady() {
        return false;
    }

    @Override
    public DeviceInfo getLeftDriveMaster() {
        return null;
    }

    @Override
    public DeviceInfo getLeftDriveFollower() {
        return null;
    }

    @Override
    public DeviceInfo getLeftDriveFollowerSecond() {
        return null;
    }

    @Override
    public DeviceInfo getRightDriveMaster() {
        return null;
    }

    @Override
    public DeviceInfo getRightDriveFollower() {
        return null;
    }

    @Override
    public DeviceInfo getRightDriveFollowerSecond() {
        return null;
    }

    @Override
    public DeviceInfo getLeftDriveMasterEncoder() {
        return null;
    }

    @Override
    public DeviceInfo getRightDriveMasterEncoder() {
        return null;
    }

    @Override
    public DeviceInfo getGripperDiscSolenoid() {
        return null;
    }

    @Override
    public DeviceInfo getGripperExtensionSolenoid() {
        return null;
    }

    @Override
    public DeviceInfo getGripperSensor() {
        return null;
    }

    @Override
    public boolean isGripperReady() {
        return false;
    }

    @Override
    public DeviceInfo getElevatorMasterMotor() {
        return null;
    }

    @Override
    public DeviceInfo getElevatorMasterEncoder() {
        return null;
    }

    @Override
    public DeviceInfo getElevatorFollowerMotor() {
        return null;
    }

    @Override
    public DeviceInfo getElevatorCalibrationSensor() {
        return null;
    }

    @Override
    public DeviceInfo getBrakeSolenoid() {
        return null;
    }

    @Override
    public boolean isElevatorReady() {
        return false;
    } 

    @Override
    public boolean isElevatorLimitSwitchReady() {
        return false;
    }
    
    @Override
    public boolean doesDriveHaveThreeMotors() {
        return false;
    }

    @Override 
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