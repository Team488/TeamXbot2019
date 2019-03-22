package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.commands.PrecisionRotateCommand;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;
import xbot.common.subsystems.pose.MockBasePoseSubsystem;

public class PrecisionRotateCommandTest extends BaseCompetitionTest {

    DriveSubsystem drive;
    MockBasePoseSubsystem pose;
    OperatorInterface oi;
    PrecisionRotateCommand command;

    @Override
    public void setUp() {
        super.setUp();

        drive = injector.getInstance(DriveSubsystem.class);
        pose = injector.getInstance(MockBasePoseSubsystem.class);
        command = injector.getInstance(PrecisionRotateCommand.class);
        oi = injector.getInstance(OperatorInterface.class);
    }

    @Test
    public void testInitialize() {
        command.initialize();
        
    }

    @Test
    public void testRotate() {
        ((MockBasePoseSubsystem)pose).setCurrentHeading(35);
        command.initialize();
        command.execute();
        assertEquals(0.0, drive.rightMaster.getMotorOutputPercent(), 0.001);

        ((MockFTCGamepad)oi.driverGamepad).setRightStick(new XYPair(1, 0));
        command.execute();
        assertTrue("testing power1", drive.rightMaster.getMotorOutputPercent()<0 && drive.rightMaster.getMotorOutputPercent()>-0.2);
        ((MockFTCGamepad)oi.driverGamepad).setRightStick(new XYPair(-1, 0));
        command.execute();
        assertTrue("testing power2", drive.rightMaster.getMotorOutputPercent()>0 && drive.rightMaster.getMotorOutputPercent()<0.2);


    }

}