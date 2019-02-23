package competition.subsystems.current;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class CurrentMonitoringSubsystemTest extends BaseCompetitionTest {

    CurrentMonitoringSubsystem currentMonitoring;
    DriveSubsystem drive;
    ElevatorSubsystem elevator;

    @Override
    public void setUp() {
        super.setUp();
        this.drive = injector.getInstance(DriveSubsystem.class);
        this.elevator = injector.getInstance(ElevatorSubsystem.class);
        this.currentMonitoring = injector.getInstance(CurrentMonitoringSubsystem.class);

    }

    @Test
    public void AllMotorsSetTo1()
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
    public void AllMotorsSetToPositive()
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
    public void AllMotorsSetToNegative()
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
    public void EveryOtherMotorNegative()
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
    public void ThreeAndNineNegative()
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

}
