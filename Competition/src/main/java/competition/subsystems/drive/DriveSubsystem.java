package competition.subsystems.drive;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.ElectricalContract2019;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.XPropertyManager;

@Singleton
public class DriveSubsystem extends BaseSubsystem {
    private static Logger log = Logger.getLogger(DriveSubsystem.class);

    public final XCANTalon leftMaster;
    public final XCANTalon leftFollower;
    public final XCANTalon rightMaster;
    public final XCANTalon rightFollower;

    @Inject
    public DriveSubsystem(CommonLibFactory factory, XPropertyManager propManager, ElectricalContract2019 contract) {
        log.info("Creating DriveSubsystem");

        this.leftMaster = factory.createCANTalon(contract.getLeftDriveMaster().channel);
        this.leftFollower = factory.createCANTalon(contract.getLeftDriveFollower().channel);
        this.rightMaster = factory.createCANTalon(contract.getRightDriveMaster().channel);
        this.rightFollower = factory.createCANTalon(contract.getRightDriveFollower().channel);

        XCANTalon.configureMotorTeam("LeftDrive", "LeftMaster", leftMaster, leftFollower, 
            contract.getLeftDriveMaster().inverted,
            contract.getLeftDriveFollower().inverted,
            contract.getLeftDriveMasterEncoder().inverted);

        XCANTalon.configureMotorTeam("RightDrive", "RightMaster", rightMaster, rightFollower, 
            contract.getRightDriveMaster().inverted,
            contract.getRightDriveFollower().inverted,
            contract.getRightDriveMasterEncoder().inverted);
    }

    public void tankDrive(double leftPower, double rightPower) {
        this.leftMaster.simpleSet(leftPower);
        this.rightMaster.simpleSet(rightPower);
    }
}
