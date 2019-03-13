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
        return new DeviceInfo(49, false);
    }

    @Override
    public DeviceInfo getFrontRightClimber() {
        return new DeviceInfo(48, false);
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
        return false;
    }
}