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
    private final DoubleProperty calibrationOffsetProp;
    public XSolenoid allowElevatorMotionSolenoid;
    private boolean isCalibrated;
    private final DoubleProperty elevatorStandardPower;
    private final DoubleProperty midHeightProp;
    private final DoubleProperty topHeightProp;
    private final DoubleProperty brakePowerLimit;
    private ElectricalContract2019 contract;
    private final DoubleProperty armForcedDownToFreeRatchetDuration;
    public double raiseArmRequestedTime;
    private Latch positivePowerLatch;
    private Latch calibrationLatch;
    private final DoubleProperty armDeadBand;
    private final BooleanProperty armLimitSwitchProp;
    private final DoubleProperty winchUnlockPower;
    private final DoubleProperty elevatorMaximumPower;

    public enum HatchLevel {
        Low, Medium, High,
    }

    @Inject
    public ElevatorSubsystem(CommonLibFactory factory, PropertyFactory propManager, ElectricalContract2019 contract,
            PIDFactory pd) {
        log.info("Creating ElevatorSubsystem");
        propManager.setPrefix(this.getPrefix());
        this.contract = contract;
        
        
        if (contract.isElevatorReady()) {
            this.allowElevatorMotionSolenoid = factory.createSolenoid(contract.getBrakeSolenoid().channel);
            this.master = factory.createCANTalon(contract.getElevatorMasterMotor().channel);
            this.follower = factory.createCANTalon(contract.getElevatorFollowerMotor().channel);
            XCANTalon.configureMotorTeam(this.getPrefix(), "ElevatorMaster", master, follower,
                    contract.getElevatorMasterMotor().inverted, contract.getElevatorFollowerMotor().inverted,
                    contract.getElevatorMasterEncoder().inverted);
        }

        if (contract.isElevatorLimitSwitchReady()) {
            this.calibrationSensor = factory.createDigitalInput(contract.getElevatorCalibrationSensor().channel);
            this.calibrationSensor.setInverted(contract.getElevatorCalibrationSensor().inverted);
        }

        elevatorStandardPower = propManager.createPersistentProperty("StandardPower", 1);
        elevatorMaximumPower = propManager.createPersistentProperty("MaximumPower", 0.5);
        winchUnlockPower = propManager.createPersistentProperty("UnlockPower", -.1);
        midHeightProp = propManager.createPersistentProperty("MidHeight", 0);
        topHeightProp = propManager.createPersistentProperty("TopHeight", 0);
        brakePowerLimit = propManager.createPersistentProperty("BrakePowerLimit", 0.05);
        calibrationOffsetProp = propManager.createEphemeralProperty("CalibrationOffset", 0);
        armDeadBand = propManager.createPersistentProperty("Arm Deadband", 0.05);
        armForcedDownToFreeRatchetDuration = propManager.createPersistentProperty("Arm Forced Down To Free Ratchet Duration", .1);
        armLimitSwitchProp = propManager.createEphemeralProperty("LowerLimitSwitch", false);
        raiseArmRequestedTime = -9999999;
        
        positivePowerLatch = new Latch(false, EdgeType.Both, edge -> {
            if(edge == EdgeType.RisingEdge) {
                log.info("Arm wants to rise");
                raiseArmRequestedTime = XTimer.getFPGATimestamp();
            }
        });

        calibrationLatch = new Latch(false, EdgeType.Both, edge -> {
            if(edge == EdgeType.RisingEdge) {
                log.info("Calibration sensor rising edge detected.");
                calibrate();
            }
        });
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
        return isCalibrated;
    }

    public double getElevatorHeightInRawTicks() {
        return master.getSelectedSensorPosition(0);
    }

    public void calibrate() {
        calibrationOffsetProp.set(getElevatorHeightInRawTicks());
        isCalibrated = true;
        //setSafeties(true);
        //master.configReverseSoftLimitThreshold((int)getElevatorHeightInRawTicks(), 0);
    }

    public void setSafeties(boolean on) {
        master.configReverseSoftLimitEnable(on, 0);
    }

    public double getCalibrationHeight() {
        return calibrationOffsetProp.get();
    }

    public void setPower(double power) {
        if (contract.isElevatorReady()) {
            boolean releaseRatchet = false;
            positivePowerLatch.setValue(power > armDeadBand.get());
            
            if (contract.isElevatorLimitSwitchReady()){
                calibrationLatch.setValue(calibrationSensor.get());
            }

            if (Math.abs(power) < armDeadBand.get()) {
                power = 0;
            }

            if (contract.isElevatorLimitSwitchReady() && isCalibrationSensorPressed()) {
                power = MathUtils.constrainDouble(power, 0, 1);
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

            power *= elevatorMaximumPower.get();
            master.simpleSet(power);
            allowElevatorMotionSolenoid.setOn(releaseRatchet);
        }
    }

    public double getMaximumPower() {
        return elevatorMaximumPower.get();
    }

    public double getTickHeightForLevel(HatchLevel level) {
        if (level == HatchLevel.Low) {
            return calibrationOffsetProp.get();
        } else if (level == HatchLevel.Medium) {
            return calibrationOffsetProp.get() + midHeightProp.get();
        } else {
            return calibrationOffsetProp.get() + topHeightProp.get();
        }
    }

    public void setTickGoal(double value) {
        tickGoal = value;
    }

    public double getTickGoal() {
        return tickGoal;
    }

    public void insanelyDangerousSetPower(double power) {
        boolean releaseRatchet = false;
        if (power > brakePowerLimit.get()) {
            releaseRatchet = true;
        }
        master.simpleSet(power);
        allowElevatorMotionSolenoid.setOn(releaseRatchet);
    }

    public void setCurrentPositionAsGoalPosition() {
        setTickGoal(getElevatorHeightInRawTicks());
    }

    @Override
    public void updatePeriodicData() {
        if (contract.isElevatorLimitSwitchReady()) {
            armLimitSwitchProp.set(calibrationSensor.get());
        }
    }

}