package competition;

public class Practice2019Contract extends ElectricalContract2019 {

    @Override
    public DeviceInfo getLeftDriveMaster() {
        return new DeviceInfo(33, true);
    }

    @Override
    public DeviceInfo getLeftDriveFollower() {
        return new DeviceInfo(34, true);
    }

    @Override
    public DeviceInfo getLeftDriveFollowerSecond() {
        return new DeviceInfo(32, true);
    }

    @Override
    public DeviceInfo getRightDriveMaster() {
        return new DeviceInfo(22, false);
    }

    @Override
    public DeviceInfo getRightDriveFollower() {
        return new DeviceInfo(21, false);
    }

    @Override
    public DeviceInfo getRightDriveFollowerSecond() {
        return new DeviceInfo(23, false);
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



}