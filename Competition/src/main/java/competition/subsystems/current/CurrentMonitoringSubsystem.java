package competition.subsystems.current;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.controls.actuators.XCompressor;

@Singleton
public class CurrentMonitoringSubsystem {

    DriveSubsystem drive;
    ElevatorSubsystem elevator;
    XCompressor compressor;

    @Inject
    public CurrentMonitoringSubsystem(DriveSubsystem drive, ElevatorSubsystem elevator, XCompressor compressor) {
        this.drive = drive;
        this.elevator = elevator;
        this.compressor = compressor;
    }
   
    public double getDriveCurrent(){
        return Math.abs(drive.rightMaster.getOutputCurrent()) + Math.abs(drive.rightFollower.getOutputCurrent())
                + Math.abs(drive.rightFollowerSecond.getOutputCurrent()) + Math.abs(drive.leftMaster.getOutputCurrent())
                + Math.abs(drive.leftFollower.getOutputCurrent())
                + Math.abs(drive.leftFollowerSecond.getOutputCurrent());
    }
   
    public double getArmCurrent() {
        return Math.abs(elevator.master.getOutputCurrent()) + Math.abs(elevator.follower.getOutputCurrent());
    }

    public double getTotalCurrent() {
        
        return Math.abs(getArmCurrent()) + Math.abs(getDriveCurrent()) + Math.abs(getCompressorCurrentFromMoniter());
    }

    public double getCompressorCurrentFromMoniter() {
        return Math.abs(compressor.getCompressorCurrent());
    }
}