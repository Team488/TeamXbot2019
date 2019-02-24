package competition.subsystems.current;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.MockCompressor;
import xbot.common.controls.actuators.XCompressor;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class CurrentMonitoringSubsystemTest extends BaseCompetitionTest {

    CurrentMonitoringSubsystem currentMonitoring;
    DriveSubsystem drive;
    ElevatorSubsystem elevator;
    MockCompressor compressor;

    @Override
    public void setUp() {
        super.setUp();
        this.drive = injector.getInstance(DriveSubsystem.class);
        this.elevator = injector.getInstance(ElevatorSubsystem.class);
        this.currentMonitoring = injector.getInstance(CurrentMonitoringSubsystem.class);
        this.compressor = (MockCompressor)injector.getInstance(XCompressor.class);
    }

    @Test
    public void allMotorsSetTo1()
    {
        //setting drive motors
        ((MockCANTalon)(drive.rightMaster)).setOutputCurrent(1.0);
        ((MockCANTalon)(drive.rightFollower)).setOutputCurrent(1.0); 
        ((MockCANTalon)(drive.rightFollowerSecond)).setOutputCurrent(1.0);
        ((MockCANTalon)(drive.leftMaster)).setOutputCurrent(1.0); 
        ((MockCANTalon)(drive.leftFollower)).setOutputCurrent(1.0);
        ((MockCANTalon)(drive.leftFollowerSecond)).setOutputCurrent(1.0);

        //setting elevator motors
        ((MockCANTalon)(elevator.master)).setOutputCurrent(1.0);
        ((MockCANTalon)(elevator.follower)).setOutputCurrent(1.0);

        
        assertEquals(6.0, currentMonitoring.getDriveCurrent(), 0.01);
        assertEquals(2.0, currentMonitoring.getArmCurrent(), 0.01);
        assertEquals(8.0, currentMonitoring.getTotalCurrent(), 0.01);
    }

    @Test
    public void allMotorsSetToPositive()
    {
        //setting drive motors
        ((MockCANTalon)(drive.rightMaster)).setOutputCurrent(2.0);
        ((MockCANTalon)(drive.rightFollower)).setOutputCurrent(3.0); 
        ((MockCANTalon)(drive.rightFollowerSecond)).setOutputCurrent(4.0);
        ((MockCANTalon)(drive.leftMaster)).setOutputCurrent(5.0); 
        ((MockCANTalon)(drive.leftFollower)).setOutputCurrent(6.0);
        ((MockCANTalon)(drive.leftFollowerSecond)).setOutputCurrent(7.0);

        //setting elevator motors
        ((MockCANTalon)(elevator.master)).setOutputCurrent(8.0);
        ((MockCANTalon)(elevator.follower)).setOutputCurrent(1.0);

        
        assertEquals(27.0, currentMonitoring.getDriveCurrent(), 0.01);
        assertEquals(9.0, currentMonitoring.getArmCurrent(), 0.01);
        assertEquals(36.0, currentMonitoring.getTotalCurrent(), 0.01);
    }

    @Test
    public void allMotorsSetToNegative()
    {
        //setting drive motors
        ((MockCANTalon)(drive.rightMaster)).setOutputCurrent(-2.0);
        //something wrong with rightFollower negative
        ((MockCANTalon)(drive.rightFollower)).setOutputCurrent(-3.0); 
        ((MockCANTalon)(drive.rightFollowerSecond)).setOutputCurrent(-4.0);
        ((MockCANTalon)(drive.leftMaster)).setOutputCurrent(-5.0); 
        ((MockCANTalon)(drive.leftFollower)).setOutputCurrent(-6.0);
        ((MockCANTalon)(drive.leftFollowerSecond)).setOutputCurrent(-7.0);

        //setting elevator motors
        //something wrong with negative master
        ((MockCANTalon)(elevator.master)).setOutputCurrent(-9.0);
        ((MockCANTalon)(elevator.follower)).setOutputCurrent(-1.0);

        
        assertEquals(27.0, currentMonitoring.getDriveCurrent(), 0.01);
        assertEquals(10.0, currentMonitoring.getArmCurrent(), 0.01);
        assertEquals(37.0, currentMonitoring.getTotalCurrent(), 0.01);
    }

    @Test
    public void everyOtherMotorNegative()
    {
        //setting drive motors
        ((MockCANTalon)(drive.rightMaster)).setOutputCurrent(-2.0);
        //something wrong with rightFollower negative
        ((MockCANTalon)(drive.rightFollower)).setOutputCurrent(3.0); 
        ((MockCANTalon)(drive.rightFollowerSecond)).setOutputCurrent(-4.0);
        ((MockCANTalon)(drive.leftMaster)).setOutputCurrent(5.0); 
        ((MockCANTalon)(drive.leftFollower)).setOutputCurrent(-6.0);
        ((MockCANTalon)(drive.leftFollowerSecond)).setOutputCurrent(-7.0);

        //setting elevator motors
        //something wrong with negative master
        ((MockCANTalon)(elevator.master)).setOutputCurrent(9.0);
        ((MockCANTalon)(elevator.follower)).setOutputCurrent(-1.0);

        
        assertEquals(27.0, currentMonitoring.getDriveCurrent(), 0.01);
        assertEquals(10.0, currentMonitoring.getArmCurrent(), 0.01);
        assertEquals(37.0, currentMonitoring.getTotalCurrent(), 0.01);
    }

    @Test
    public void threeAndNineNegative()
    {
        //setting drive motors
        ((MockCANTalon)(drive.rightMaster)).setOutputCurrent(2.0);
        //something wrong with rightFollower negative
        ((MockCANTalon)(drive.rightFollower)).setOutputCurrent(-3.0); 
        ((MockCANTalon)(drive.rightFollowerSecond)).setOutputCurrent(4.0);
        ((MockCANTalon)(drive.leftMaster)).setOutputCurrent(5.0); 
        ((MockCANTalon)(drive.leftFollower)).setOutputCurrent(6.0);
        ((MockCANTalon)(drive.leftFollowerSecond)).setOutputCurrent(7.0);

        //setting elevator motors
        //something wrong with negative master
        ((MockCANTalon)(elevator.master)).setOutputCurrent(-9.0);
        ((MockCANTalon)(elevator.follower)).setOutputCurrent(1.0);

        
        assertEquals(27.0, currentMonitoring.getDriveCurrent(), 0.01);
        assertEquals(10.0, currentMonitoring.getArmCurrent(), 0.01);
        assertEquals(37.0, currentMonitoring.getTotalCurrent(), 0.01);
    }

    @Test
    public void compressorTest()
    {
        
        compressor.setCompressorCurrent(2.0);
        
        assertEquals(2.0, compressor.getCompressorCurrent(), 0.01);
    }

    @Test
    public void allNegativeCompressorTest()
    {
        
        compressor.setCompressorCurrent(-2.0);
        
        assertEquals(2.0, currentMonitoring.getCompressorCurrentFromMoniter(), 0.01);
    }

    @Test
    public void allSubsytems()
    {
        //setting drive motors
        ((MockCANTalon)(drive.rightMaster)).setOutputCurrent(1.0);
        //something wrong with rightFollower negative
        ((MockCANTalon)(drive.rightFollower)).setOutputCurrent(1.0); 
        ((MockCANTalon)(drive.rightFollowerSecond)).setOutputCurrent(1.0);
        ((MockCANTalon)(drive.leftMaster)).setOutputCurrent(1.0); 
        ((MockCANTalon)(drive.leftFollower)).setOutputCurrent(1.0);
        ((MockCANTalon)(drive.leftFollowerSecond)).setOutputCurrent(1.0);

        //setting elevator motors
        //something wrong with negative master
        ((MockCANTalon)(elevator.master)).setOutputCurrent(1.0);
        ((MockCANTalon)(elevator.follower)).setOutputCurrent(1.0);

        //setting current
        compressor.setCompressorCurrent(1.0);
        
        assertEquals(6.0, currentMonitoring.getDriveCurrent(), 0.01);
        assertEquals(2.0, currentMonitoring.getArmCurrent(), 0.01);
        assertEquals(1.0, currentMonitoring.getCompressorCurrentFromMoniter(), 0.01);
        assertEquals(9.0, currentMonitoring.getTotalCurrent(), 0.01);
    }

    @Test
    public void allSubsytemsNegative()
    {
        //setting drive motors
        ((MockCANTalon)(drive.rightMaster)).setOutputCurrent(-1.0);
        //something wrong with rightFollower negative
        ((MockCANTalon)(drive.rightFollower)).setOutputCurrent(-1.0); 
        ((MockCANTalon)(drive.rightFollowerSecond)).setOutputCurrent(-1.0);
        ((MockCANTalon)(drive.leftMaster)).setOutputCurrent(-1.0); 
        ((MockCANTalon)(drive.leftFollower)).setOutputCurrent(-1.0);
        ((MockCANTalon)(drive.leftFollowerSecond)).setOutputCurrent(-1.0);

        //setting elevator motors
        //something wrong with negative master
        ((MockCANTalon)(elevator.master)).setOutputCurrent(-1.0);
        ((MockCANTalon)(elevator.follower)).setOutputCurrent(-1.0);

        //setting current
        compressor.setCompressorCurrent(1.0);
        
        assertEquals(6.0, currentMonitoring.getDriveCurrent(), 0.01);
        assertEquals(2.0, currentMonitoring.getArmCurrent(), 0.01);
        assertEquals(1.0, currentMonitoring.getCompressorCurrentFromMoniter(), 0.01);
        assertEquals(9.0, currentMonitoring.getTotalCurrent(), 0.01);
    }
}
