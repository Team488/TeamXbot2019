package competition;

import com.google.inject.Singleton;

@Singleton
public class Competition2018Contract extends ElectricalContract2019 {

    @Override
    public DeviceInfo getLeftDriveMaster() {
        return new DeviceInfo(34, true);
    }

    @Override
    public DeviceInfo getLeftDriveFollower() {
        return new DeviceInfo(35, true);
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
    public DeviceInfo getRightDriveMasterEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getElevatorMasterMotor() {
        return new DeviceInfo(-1, false);
    }

    @Override
    public DeviceInfo getElevatorMasterEncoder() {
        return new DeviceInfo(-1, false);
    }

    @Override
    public DeviceInfo getElevatorCalibrationSensor() {
        return new DeviceInfo(-1, false);
    }

    @Override
    public boolean isElevatorReady() {
        return false;
    }
}