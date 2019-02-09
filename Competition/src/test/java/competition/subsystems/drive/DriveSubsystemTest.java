package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class DriveSubsystemTest extends BaseCompetitionTest {
   
    @Test
    public void testTankDrive() {
        DriveSubsystem driveSubsystem = this.injector.getInstance(DriveSubsystem.class);
        driveSubsystem.drive(1, 1);

        assertEquals(1, driveSubsystem.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(1, driveSubsystem.rightMaster.getMotorOutputPercent(), 0.001);
    }
}
