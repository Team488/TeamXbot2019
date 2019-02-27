package competition.subsystems.current;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2019;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCompressor;
import xbot.common.injection.ElectricalContract;

@Singleton
public class CurrentMonitoringSubsystem implements PeriodicDataSource {

    DriveSubsystem drive;
    ElevatorSubsystem elevator;
    XCompressor compressor;
    double powerUsed;
    double drivePowerUsed;
    double armPowerUsed;
    double compressorPowerUsed;
    ElectricalContract2019 contract;

    
    @Inject
    public CurrentMonitoringSubsystem(DriveSubsystem drive, ElevatorSubsystem elevator, XCompressor compressor, ElectricalContract2019 contract) {
        this.drive = drive;
        this.elevator = elevator;
        this.compressor = compressor;
        this.contract = contract;
    }
   
    public double getDriveCurrent(){
        double sum = Math.abs(drive.rightMaster.getOutputCurrent()) + Math.abs(drive.rightFollower.getOutputCurrent())
                    + Math.abs(drive.leftMaster.getOutputCurrent())
                    + Math.abs(drive.leftFollower.getOutputCurrent());
        if (contract.doesDriveHaveThreeMotors() == true){
            sum+= Math.abs(drive.rightFollowerSecond.getOutputCurrent()) + Math.abs(drive.leftFollowerSecond.getOutputCurrent());
        }
        return sum;
    }
   
    public double getArmCurrent() {
        if (contract.isElevatorReady()){
            return Math.abs(elevator.master.getOutputCurrent()) + Math.abs(elevator.follower.getOutputCurrent());
        }
        return 0;
    }

    public double getTotalCurrent() {
        
        return Math.abs(getArmCurrent()) + Math.abs(getDriveCurrent()) + Math.abs(getCompressorCurrentFromMoniter());
    }

    public double getCompressorCurrentFromMoniter() {
        return Math.abs(compressor.getCompressorCurrent());
    }

    public void updatePeriodicData(){
        powerUsed += getTotalCurrent();
        drivePowerUsed += getDriveCurrent();
        armPowerUsed += getArmCurrent();
        compressorPowerUsed += getCompressorCurrentFromMoniter();
    }
    
    public String getName(){
        return "CurrentMonitoring";
        
        //imaginary contract
    }
}