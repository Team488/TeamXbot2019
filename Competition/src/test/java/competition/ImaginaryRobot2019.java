package competition;

public class ImaginaryRobot2019 extends Competition2019Contract {
    
    @Override
    public boolean isDriveReady() {
        return true;
    }
    
    @Override
    public boolean isElevatorReady() {
        return true;
    }

    @Override
    public boolean isGripperReady() {
        return true;
    }

    @Override
    public boolean isElevatorLimitSwitchReady() {
        return true;
    }

    public boolean doesDriveHaveThreeMotors() {
        return true;
    }
    @Override
    public boolean isFourBarReady() {
        return true;
    }

    @Override
    public boolean isClimberReady() {
        return true;
    }

    @Override
    public boolean isFrontMotorClimberReady() {
        return true;
    }

    @Override
    public boolean isRearMotorClimberReady() {
        return true;
    }

    @Override
    public boolean invertVisionData() {
        return false;
    }
}