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
}