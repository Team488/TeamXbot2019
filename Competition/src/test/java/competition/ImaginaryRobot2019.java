package competition;

public class ImaginaryRobot2019 extends Competition2019Contract {
    @Override
    public boolean isElevatorReady() {
        return true;
    }

    @Override
    public boolean isGripperReady() {
        return true;
    }
    
    public boolean doesDriveHaveThreeMotors() {
        return true;
    }
}