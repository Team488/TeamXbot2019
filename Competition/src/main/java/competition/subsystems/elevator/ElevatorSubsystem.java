package competition.subsystems.elevator;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.ElectricalContract2019;
import competition.subsystems.gripper.GripperSubsystem;
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

    private final DoubleProperty tickGoal;
    public XCANTalon master;
    public XCANTalon follower;
    public XDigitalInput calibrationSensor;
    private final DoubleProperty calibrationOffsetProp;
    private final DoubleProperty elevatorHeightTicks;
    public XSolenoid allowElevatorMotionSolenoid;
    private final BooleanProperty isCalibratedProp;
    private final DoubleProperty elevatorStandardPower;
    private final DoubleProperty midHeightProp;
    private final DoubleProperty topHeightProp;
    private final DoubleProperty brakePowerLimit;
    private ElectricalContract2019 contract;
    private final DoubleProperty armForcedDownToFreeRatchetDuration;
    private double raiseArmRequestedTime;
    private double raiseArmStartingPosition;
    private final DoubleProperty raiseArmRetractDistanceInTicks;
    private Latch positivePowerLatch;
    private Latch calibrationLatch;
    private final DoubleProperty armDeadBand;
    private final BooleanProperty armLimitSwitchProp;
    private final DoubleProperty winchUnlockPower;
    private final DoubleProperty elevatorMaximumPower;
    private final DoubleProperty outputPower;

    private boolean outreachMode;
    private double ratchetRelasePower;

    private final GripperSubsystem gripper;

    private WinchUnlockState winchUnlockState;

    public enum HatchLevel {
        Low, Medium, High,
    }

    public enum WinchUnlockState {
        ForcedDrop,
        Free
    }

    @Inject
    public ElevatorSubsystem(CommonLibFactory factory, PropertyFactory propManager, ElectricalContract2019 contract,
            PIDFactory pd, GripperSubsystem gripper) {
        log.info("Creating ElevatorSubsystem");
        propManager.setPrefix(this.getPrefix());
        this.gripper = gripper;
        this.contract = contract;

        winchUnlockState = WinchUnlockState.Free;
        
        
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
        elevatorHeightTicks = propManager.createEphemeralProperty("ElevatorHeightTicks", 0);
        armDeadBand = propManager.createPersistentProperty("Arm Deadband", 0.05);
        armForcedDownToFreeRatchetDuration = propManager.createPersistentProperty("Arm Forced Down To Free Ratchet Duration", .1);
        armLimitSwitchProp = propManager.createEphemeralProperty("LowerLimitSwitch", false);
        outputPower = propManager.createEphemeralProperty("OutputPower", 0);
        raiseArmRetractDistanceInTicks = propManager.createPersistentProperty("RaiseArmRetractDistanceInTicks", 10);
        isCalibratedProp = propManager.createEphemeralProperty("IsCalibrated", false);
        raiseArmRequestedTime = -9999999;
        raiseArmStartingPosition = 9999999;
        tickGoal = propManager.createEphemeralProperty("TickGoal", 0.0);
        
        positivePowerLatch = new Latch(false, EdgeType.Both, edge -> {
            if(edge == EdgeType.RisingEdge) {
                log.info("Arm wants to rise");
                raiseArmRequestedTime = XTimer.getFPGATimestamp();
                raiseArmStartingPosition = getElevatorHeightInRawTicks();
                winchUnlockState = WinchUnlockState.ForcedDrop;
                ratchetRelasePower = -0.1;
            }
        });

        calibrationLatch = new Latch(false, EdgeType.Both, edge -> {
            if(edge == EdgeType.RisingEdge) {
                log.info("Calibration sensor rising edge detected.");
                calibrate();
            }
        });
    }

    public void setOutreachModeEnabled(boolean enabled) {
        outreachMode = enabled;
    }

    public boolean getOutreachModeEnabled() {
        return outreachMode;
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
        return isCalibratedProp.get();
    }

    public int getElevatorHeightInRawTicks() {
        return master.getSelectedSensorPosition(0);
    }

    public int getElevatorHeightInCalibratedTicks() {
        return getElevatorHeightInRawTicks() - getCalibrationOffset();
    }

    private int getCalibrationOffset() {
        return (int)calibrationOffsetProp.get();
    }

    public void calibrate() {
        calibrationOffsetProp.set(getElevatorHeightInRawTicks());
        isCalibratedProp.set(true);
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

            // If the calibration sensor is pressed, or we are below it somehow, limit
            // power to positive ranges.
            if (contract.isElevatorLimitSwitchReady() 
                && (isCalibrationSensorPressed())) { //|| getElevatorHeightInCalibratedTicks() < 0)) {
                power = MathUtils.constrainDouble(power, 0, 1);
            }
            
            // we only need to disengage the ratchet when going up.
            if (power > brakePowerLimit.get()) {
                releaseRatchet = true;
            } else {
                releaseRatchet = false;
            }

            if (getOutreachModeEnabled()) {
                power *= 0.35;
            }

            // The ratchet may have trouble disengaging when under heavy strain. Therefore,
            // if we want to go up, then we should first apply some downwards power for a moment
            // while releasing the ratchet.

            // Old approach - go down for a set amount of time.
            /*
            if (XTimer.getFPGATimestamp() - raiseArmRequestedTime <= armForcedDownToFreeRatchetDuration.get()) {
                releaseRatchet = true;
                power = winchUnlockPower.get();
            }*/

            // New approach - go down for a set amount of distance
            // Note that this will allow the robot to go below its calibrated minimum point.
            if (winchUnlockState == WinchUnlockState.ForcedDrop) {                    
                releaseRatchet = true;

                if (getElevatorHeightInRawTicks() <= raiseArmStartingPosition - raiseArmRetractDistanceInTicks.get()) {
                    winchUnlockState = WinchUnlockState.Free;
                }

                if (XTimer.getFPGATimestamp() - raiseArmRequestedTime > 0.3) {
                    winchUnlockState = WinchUnlockState.Free;
                }
                // The amount of power needed to lower the arm a bit changes with the position of the arm.
                // The lower it is, the harder it is to make it go any lower.

                // As a result, we need to emulate an I-only PID system, and just keep trying harder until we've gone the 
                // appropriate distance.

                // This is kind of dangerous, so we also need some kind of stall detector to make sure we're not
                // going down at 100% power against the hard stop.
                if (winchUnlockState != WinchUnlockState.Free) {
                    power = ratchetRelasePower;
                    ratchetRelasePower -= 0.05;
                }
            }

            power *= elevatorMaximumPower.get();

            // If the gripper isn't extended, then it's too dangerous
            // to operate the elevator
            if (!gripper.getExtended()) {
                power = 0;
                releaseRatchet = false;
            }

            outputPower.set(power);
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
        tickGoal.set(value);
    }

    public double getTickGoal() {
        return tickGoal.get();
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

    public int getRaiseArmRetractDistanceInTicks() {
        return (int)raiseArmRetractDistanceInTicks.get();
    }

    @Override
    public void updatePeriodicData() {
        if (contract.isElevatorLimitSwitchReady()) {
            armLimitSwitchProp.set(calibrationSensor.get());
        }
        elevatorHeightTicks.set(this.getElevatorHeightInCalibratedTicks());
        master.updateTelemetryProperties();
    }

}