package competition.subsystems.drive;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.ElectricalContract2019;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;

@Singleton
public class DriveSubsystem extends BaseDriveSubsystem {

    public enum Side {
        Left, Right
    }

    private final DoubleProperty leftTicksPerFiveFeet;
    private final DoubleProperty rightTicksPerFiveFeet;

    private static Logger log = Logger.getLogger(DriveSubsystem.class);

    public final XCANTalon leftMaster;
    public final XCANTalon leftFollower;
    public final XCANTalon rightMaster;
    public final XCANTalon rightFollower;

    private Map<XCANTalon, MotionRegistration> masterTalons;

    private final PIDManager positionalPid;
    private final PIDManager rotateToHeadingPid;
    private final PIDManager rotateDecayPid;

    @Inject
    public DriveSubsystem(
        CommonLibFactory factory, 
        XPropertyManager propManager, 
        ElectricalContract2019 contract,
        PIDFactory pf) {
        log.info("Creating DriveSubsystem");

        positionalPid = pf.createPIDManager(getPrefix()+"Drive to position", 0.1, 0, 0, 0, 0.75, -0.75, 3, 1, 0.5);
        
        rotateToHeadingPid = pf.createPIDManager(getPrefix()+"DriveHeading", 0.02, 0, 0, 0, 1, -1, 5, 1, 0.5);
        rotateToHeadingPid.setEnableErrorThreshold(true);
        rotateToHeadingPid.setEnableDerivativeThreshold(true);
        rotateToHeadingPid.setEnableTimeThreshold(true);

        rotateDecayPid = pf.createPIDManager(getPrefix()+"DriveDecay", 0, 0, 1);

        leftTicksPerFiveFeet = propManager.createPersistentProperty(getPrefix() + "leftDriveTicksPer5Feet", 100281);
        rightTicksPerFiveFeet = propManager.createPersistentProperty(getPrefix() + "rightDriveTicksPer5Feet", 100281);

        this.leftMaster = factory.createCANTalon(contract.getLeftDriveMaster().channel);
        this.leftFollower = factory.createCANTalon(contract.getLeftDriveFollower().channel);
        this.rightMaster = factory.createCANTalon(contract.getRightDriveMaster().channel);
        this.rightFollower = factory.createCANTalon(contract.getRightDriveFollower().channel);

        XCANTalon.configureMotorTeam("LeftDrive", "LeftMaster", leftMaster, leftFollower,
                contract.getLeftDriveMaster().inverted, contract.getLeftDriveFollower().inverted,
                contract.getLeftDriveMasterEncoder().inverted);

        XCANTalon.configureMotorTeam("RightDrive", "RightMaster", rightMaster, rightFollower,
                contract.getRightDriveMaster().inverted, contract.getRightDriveFollower().inverted,
                contract.getRightDriveMasterEncoder().inverted);

        masterTalons = new HashMap<XCANTalon, BaseDriveSubsystem.MotionRegistration>();
        masterTalons.put(leftMaster, new MotionRegistration(0, 1, -1));
        masterTalons.put(rightMaster, new MotionRegistration(0, 1, 1));
    }

    @Override
    public PIDManager getPositionalPid() {
        return positionalPid;
    }

    @Override
    public PIDManager getRotateToHeadingPid() {
        return rotateToHeadingPid;
    }

    @Override
    public PIDManager getRotateDecayPid() {
        return rotateDecayPid;
    }

    @Override
    protected Map<XCANTalon, MotionRegistration> getAllMasterTalons() {
        return masterTalons;
    }

    @Override
    public double getLeftTotalDistance() {
        return ticksToInches(Side.Left, leftMaster.getSelectedSensorPosition(0));
    }

    @Override
    public double getRightTotalDistance() {
        return ticksToInches(Side.Right, rightMaster.getSelectedSensorPosition(0));
    }

    @Override
    public double getTransverseDistance() {
        return 0;
    }

    public double ticksToInches(Side side, double ticks) {
        double ticksPerInch = getSideTicksPerInch(side);

        // Escape if nobody ever defined any ticks per inch
        if (ticksPerInch == 0) {
            return 0;
        }
        return ticks / ticksPerInch;
    }

    public double getSideTicksPerInch(Side side) {
        switch (side) {
        case Left:
            return leftTicksPerFiveFeet.get() / 60;
        case Right:
            return rightTicksPerFiveFeet.get() / 60;
        default:
            return 0;
        }
    }
}
