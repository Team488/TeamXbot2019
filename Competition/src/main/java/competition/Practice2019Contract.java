package competition;

public class Practice2019Contract extends ElectricalContract2019 {

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
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getRightDriveMasterEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getElevatorMasterMotor() {
        return new DeviceInfo(25, false);
    }

    @Override 
    public DeviceInfo getElevatorMasterEncoder() {
        return new DeviceInfo(5, false);
    }

    @Override
    public DeviceInfo getElevatorCalibrationSensor() {
        return null;
    }

    @Override
    public boolean isElevatorReady() {
        return false;
    }

    @Override
    public boolean doesDriveHaveThreeMotors() {
        return true;
    }

    @Override
    public DeviceInfo getGripperSolenoid() {
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



}