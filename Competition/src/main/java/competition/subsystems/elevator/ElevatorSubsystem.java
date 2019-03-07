package competition.subsystems.elevator;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.ElectricalContract2019;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.controls.sensors.XTimer;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDFactory;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class ElevatorSubsystem extends BaseSetpointSubsystem implements PeriodicDataSource {
    private static Logger log = Logger.getLogger(ElevatorSubsystem.class);

    private double tickGoal;
    public XCANTalon master;
    public XCANTalon follower;
    public XDigitalInput calibrationSensor;
    private final DoubleProperty currentCalibrationSensorPosition;
    public final XSolenoid allowElevatorMotionSolenoid;
    private boolean isCalibrated;
    private final DoubleProperty elevatorStandardPower;
    private final DoubleProperty distanceBetweenLevels;
    protected final DoubleProperty brakePowerLimit;
    private ElectricalContract2019 contract;
    public DoubleProperty armForcedDownToFreeRatchetDuration;
    public double raiseArmRequestedTime;
    private Latch positivePowerLatch;
    public final DoubleProperty armDeadBand;
    private BooleanProperty armLimitSwitchProp;
    public DoubleProperty winchUnlockPower;

    public enum HatchLevel {
        Low, Medium, High,
    }

    @Inject
    public ElevatorSubsystem(CommonLibFactory factory, PropertyFactory propManager, ElectricalContract2019 contract,
            PIDFactory pd) {
        log.info("Creating ElevatorSubsystem");
        propManager.setPrefix(this.getPrefix());
        this.contract = contract;
        this.allowElevatorMotionSolenoid = factory.createSolenoid(contract.getBrakeSolenoid().channel);
        if (contract.isElevatorReady()) {
            this.master = factory.createCANTalon(contract.getElevatorMasterMotor().channel);
            this.follower = factory.createCANTalon(contract.getElevatorFollowerMotor().channel);
            XCANTalon.configureMotorTeam(this.getPrefix(), "ElevatorMaster", master, follower,
                    contract.getElevatorMasterMotor().inverted, contract.getElevatorFollowerMotor().inverted,
                    contract.getElevatorMasterEncoder().inverted);
        } else {
            this.master = null;
            this.follower = null;
        }
        if (contract.isElevatorLimitSwitchReady()) {
            this.calibrationSensor = factory.createDigitalInput(contract.getElevatorCalibrationSensor().channel);
            this.calibrationSensor.setInverted(contract.getElevatorCalibrationSensor().inverted);
        } else {
            this.calibrationSensor = null;
        }
        elevatorStandardPower = propManager.createPersistentProperty("StandardPower", 1);
        winchUnlockPower = propManager.createPersistentProperty("UnlockPower", -.1);
        distanceBetweenLevels = propManager.createPersistentProperty("DistanceBetweenLevels", 1);
        brakePowerLimit = propManager.createPersistentProperty("BrakePowerLimit", 0.05);
        currentCalibrationSensorPosition = propManager.createPersistentProperty("CalibrationSensorPosition", -1);
        armDeadBand = propManager.createPersistentProperty("Arm Deadband", 0.05);
        armForcedDownToFreeRatchetDuration = propManager.createPersistentProperty("Arm Forced Down To Free Ratchet Duration", .1);
        armLimitSwitchProp = propManager.createEphemeralProperty("LowerLimitSwitch", false);
        positivePowerLatch = new Latch(false, EdgeType.Both, edge -> {
            if(edge == EdgeType.RisingEdge) {
                log.info("Arm wants to rise");
                raiseArmRequestedTime = XTimer.getFPGATimestamp();
            }
        });
        raiseArmRequestedTime = -9999999;
    }

    public void stop() {
        setPower(0);
    }

    public void raise() {
        setPower(elevatorStandardPower.get());
    }

    public void lower() {
        setPower(-elevatorStandardPower.get());
    }

    public boolean isCalibrationSensorPressed() {
        return calibrationSensor.get();
    }

    public boolean getIsCalibrated() {
        if (isCalibrationSensorPressed()) {
            isCalibrated = true;
        }
        return isCalibrated;
    }

    public double getElevatorHeightInTicks() {
        return master.getSelectedSensorPosition(0);
    }

    public void calibrate() {
        currentCalibrationSensorPosition.set(getElevatorHeightInTicks());
    }

    public double getCalibrationHeight() {
        return currentCalibrationSensorPosition.get();
    }

    public void setPower(double power) {
        if (contract.isElevatorReady()) {
            boolean releaseRatchet = false;
            positivePowerLatch.setValue(power > armDeadBand.get());

            if (Math.abs(power) < armDeadBand.get()) {
                power = 0;
            }

            if (contract.isElevatorLimitSwitchReady() && isCalibrationSensorPressed()) {
                power = MathUtils.constrainDouble(power, 0, 1);
                calibrate();
            }
            
            // we only need to disengage the ratchet when going up.
            if (power > brakePowerLimit.get()) {
                releaseRatchet = true;
            } else {
                releaseRatchet = false;
            }

            // The ratchet may have trouble disengaging when under heavy strain. Therefore,
            // if we want to go up, then we should first apply some downwards power for a moment
            // while releasing the ratchet.
            if (XTimer.getFPGATimestamp() - raiseArmRequestedTime <= armForcedDownToFreeRatchetDuration.get()) {
                    releaseRatchet = true;
                    power = winchUnlockPower.get();
                }
            master.simpleSet(power);
            allowElevatorMotionSolenoid.setOn(releaseRatchet);
        }
    }

    public double getMasterPower() {
        return master.getMotorOutputPercent();
    }

    private double getTickHeightForLevel(HatchLevel level) {
        getCalibrationHeight();
        if (level == HatchLevel.Low) {
            return currentCalibrationSensorPosition.get();
        } else if (level == HatchLevel.Medium) {
            return currentCalibrationSensorPosition.get() + distanceBetweenLevels.get();
        } else {
            return currentCalibrationSensorPosition.get() + distanceBetweenLevels.get() * 2;
        }
    }

    public void setTickGoal(double value) {
        tickGoal = value;
    }

    public double getTickGoal() {
        return tickGoal;
    }

    public void insanelyDangerousElevatorMode(double power) {
        master.simpleSet(power);
    }

    public void setCurrentPositionAsGoalPosition() {
        setTickGoal(getElevatorHeightInTicks());
    }

    @Override
    public void updatePeriodicData() {
        if (contract.isElevatorLimitSwitchReady()) {
            armLimitSwitchProp.set(calibrationSensor.get());
        }
    }

}