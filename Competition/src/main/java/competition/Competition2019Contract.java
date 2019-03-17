package competition;

public class Competition2019Contract extends Practice2019Contract {

    @Override
    public DeviceInfo getGripperDiscSolenoid() {
        return new DeviceInfo(1, true);
    }

    @Override
    public DeviceInfo getGripperExtensionSolenoid() {
        return new DeviceInfo(0, false);
    }

    @Override
    public boolean isElevatorLimitSwitchReady() {
        return true;
    }

    @Override
    public DeviceInfo getFrontLeftClimber() {
        return new DeviceInfo(29, true);
    }

    @Override
    public DeviceInfo getFrontRightClimber() {
        return new DeviceInfo(30, false);
    }

    @Override
    public DeviceInfo getRearLeftClimber() {
        return new DeviceInfo(35, false);
    }

    @Override
    public DeviceInfo getRearLeftEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getRearRightEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getRearRightClimber() {
        return new DeviceInfo(20, true);
    }

    @Override
    public boolean isRearMotorClimberReady() {
        return true;
    }
    
    @Override
    public boolean isFrontMotorClimberReady() {
        return true;
    }

    @Override
    public DeviceInfo getFrontLeftEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getFrontRightEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getFrontLeftLimit() {
        return new DeviceInfo(4, true);
    }

    @Override
    public DeviceInfo getFrontRightLimit() {
        return new DeviceInfo(3, true);
    }

    @Override
    public DeviceInfo getRearLeftLimit() {
        return new DeviceInfo(1, true);
    }

    @Override
    public DeviceInfo getRearRightLimit() {
        return new DeviceInfo(2, true);
    }

    @Override
    public boolean isRollerGrabberReady() {
        return false;
    }

    @Override
    public DeviceInfo getRollerGrabber() {
        return null;
    }
}