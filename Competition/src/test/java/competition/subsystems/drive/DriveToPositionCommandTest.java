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
    public void test1() {
        //from 0
        ((MockCANTalon)drive.leftMaster).setPosition(0);
        ((MockCANTalon)drive.rightMaster).setPosition(0);

        command.setDistanceToDrive(10);
        command.initialize();
        command.execute();

        assertTrue("checking if power decreases", drive.rightMaster.getMotorOutputPercent() >  0);
        assertTrue("checking if power decreases", drive.leftMaster.getMotorOutputPercent() >  0);

        // from 5
        ((MockCANTalon)drive.leftMaster).setPosition(5);
        ((MockCANTalon)drive.rightMaster).setPosition(5);

        command.execute();

        assertTrue("checking if power decreases",(drive.rightMaster).getMotorOutputPercent() > 0);
        assertTrue("checking if power decreases",(drive.leftMaster).getMotorOutputPercent() > 0);    

        //from 11
        ((MockCANTalon)drive.leftMaster).setPosition(11);
        ((MockCANTalon)drive.rightMaster).setPosition(11);

        command.execute();

        assertTrue("checking if robot stops",(drive.rightMaster).getMotorOutputPercent() < 0);
        assertTrue("checking if robot stops",(drive.leftMaster).getMotorOutputPercent() < 0);      
    }
}
