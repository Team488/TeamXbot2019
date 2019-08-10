package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.commands.DriveToPositionCommand;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class DriveToPositionCommandTest extends BaseCompetitionTest {
   
    DriveSubsystem drive;
    PoseSubsystem pose;
    DriveToPositionCommand command;

    @Override
    public void setUp() {
        super.setUp();
        drive = injector.getInstance(DriveSubsystem.class);
        pose = injector.getInstance(PoseSubsystem.class);
        command = injector.getInstance(DriveToPositionCommand.class);
    }

    @Test
    public void testInitialize() {
        command.initialize();
    }

    @Test
    public void testHasPowerIfGoalNotReached() {
        //from 0
        ((MockCANTalon)drive.leftMaster).setPosition(0);
        ((MockCANTalon)drive.rightMaster).setPosition(0);

        command.setDistanceToDrive(10);
        command.initialize();
        command.execute();

        assertTrue("checking if power is on", drive.rightMaster.getMotorOutputPercent() >  0);
        assertTrue("checking if power is on", drive.leftMaster.getMotorOutputPercent() >  0);

        // from 5
        ((MockCANTalon)drive.leftMaster).setPosition(5);
        ((MockCANTalon)drive.rightMaster).setPosition(5);

        command.execute();

        assertTrue("checking if power is on",(drive.rightMaster).getMotorOutputPercent() > 0);
        assertTrue("checking if power is on ",(drive.leftMaster).getMotorOutputPercent() > 0);     
    }

    @Test
    public void testIfPowerAtGoal() {
        //from 0
        pose.updatePeriodicData();
        pose.updatePeriodicData();
        pose.setCurrentHeading(90);

        ((MockCANTalon)drive.leftMaster).setPosition(inchesToTicks(0));
        ((MockCANTalon)drive.rightMaster).setPosition(inchesToTicks(0));

        command.setDistanceToDrive(10);
        command.initialize();
        command.execute();

        ((MockCANTalon)drive.leftMaster).setPosition(inchesToTicks(10));
        ((MockCANTalon)drive.rightMaster).setPosition(inchesToTicks(10));

        command.execute();

        assertTrue("checking if power is off",(drive.rightMaster).getMotorOutputPercent() < Math.abs(0.2));
        assertTrue("checking if power is off",(drive.leftMaster).getMotorOutputPercent() < Math.abs(0.2));       
    }

    @Test
    public void testPowerPastGoal() {
        //from 0

        pose.updatePeriodicData();
        pose.updatePeriodicData();
        pose.setCurrentHeading(90);

        ((MockCANTalon)drive.leftMaster).setPosition(inchesToTicks(0));
        ((MockCANTalon)drive.rightMaster).setPosition(inchesToTicks(0));

        command.setDistanceToDrive(10);
        command.initialize();
        command.execute();

        ((MockCANTalon)drive.leftMaster).setPosition(inchesToTicks(11));
        ((MockCANTalon)drive.rightMaster).setPosition(inchesToTicks(11));
        pose.updatePeriodicData();
        command.execute();

        assertTrue("checking if power is negative",(drive.rightMaster).getMotorOutputPercent() < 0);
        assertTrue("checking if power is negative",(drive.leftMaster).getMotorOutputPercent() < 0);       
    }

    private int inchesToTicks(double inches) {
        return (int)(inches * 12348.8 / 60);
    }
}
