package competition.subsystems.current;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;

@Singleton
public class CurrentMonitoringSubsystem {

    DriveSubsystem drive;
    ElevatorSubsystem elevator;

    @Inject
    public CurrentMonitoringSubsystem(DriveSubsystem drive, ElevatorSubsystem elevator) {
        this.drive = drive;
        this.elevator = elevator;
    }

    public double getDriveCurrent() {
        return Math.abs(drive.rightMaster.getOutputCurrent()) + Math.abs(drive.rightFollower.getOutputCurrent()
                + Math.abs(drive.rightFollowerSecond.getOutputCurrent()) + Math.abs(drive.leftMaster.getOutputCurrent())
                + Math.abs(drive.leftFollower.getOutputCurrent())
                + Math.abs(drive.leftFollowerSecond.getOutputCurrent()));
        
    }

    public double getArmCurrent() {
        return Math.abs(elevator.master.getOutputCurrent() + Math.abs(elevator.follower.getOutputCurrent()));
    }

    public double getTotalCurrent() {
        return Math.abs(drive.rightMaster.getOutputCurrent()) + Math.abs(drive.rightFollower.getOutputCurrent()
                + Math.abs(drive.rightFollowerSecond.getOutputCurrent()) + Math.abs(drive.leftMaster.getOutputCurrent())
                + Math.abs(drive.leftFollower.getOutputCurrent())
                + Math.abs(drive.leftFollowerSecond.getOutputCurrent()))
                + Math.abs(elevator.master.getOutputCurrent() + Math.abs(elevator.follower.getOutputCurrent()));
    }

}