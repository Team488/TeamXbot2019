package competition.subsystems.elevator;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.ElectricalContract2019;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ElevatorSubsystem extends BaseSubsystem {
    private static Logger log = Logger.getLogger(ElevatorSubsystem.class);

    public final XCANTalon master;
    public final XCANTalon follower;
    public final XDigitalInput calibrationSensor;
    public final XSolenoid allowElevatorMotionSolenoid;
    private boolean isCalibrated;
    private final DoubleProperty currentCalibrationSensorPosition;
    private final DoubleProperty elevatorStandardPower;
    private final DoubleProperty distanceBetweenLevels;
    protected final DoubleProperty brakePowerLimit;
    private ElectricalContract2019 contract;

    public enum HatchLevel {
        Low, Medium, High,
    }

    @Inject
    public ElevatorSubsystem(CommonLibFactory factory, XPropertyManager propManager, ElectricalContract2019 contract,
            PIDFactory pd) {
        log.info("Creating ElevatorSubsystem");
        this.contract = contract;
        this.allowElevatorMotionSolenoid = factory.createSolenoid(contract.getBrakeSolenoid().channel);
        if (contract.isElevatorReady()) {
            this.master = factory.createCANTalon(contract.getElevatorMasterMotor().channel);
            this.follower = factory.createCANTalon(contract.getElevatorFollowerMotor().channel);
            XCANTalon.configureMotorTeam(getPrefix(), "ElevatorMaster", master, follower,
                    contract.getElevatorMasterMotor().inverted, contract.getElevatorFollowerMotor().inverted,
                    contract.getElevatorMasterEncoder().inverted);
        } else {
            this.master = null;
            this.follower = null;
        }
        if (contract.isElevatorLimitSwitchReady()) {
            this.calibrationSensor = factory.createDigitalInput(contract.getElevatorCalibrationSensor().channel);
        } else {
            this.calibrationSensor = null;
        }
        elevatorStandardPower = propManager.createPersistentProperty(getPrefix() + "StandardPower", 1);
        distanceBetweenLevels = propManager.createPersistentProperty(getPrefix() + "DistanceBetweenLevels", 1);
        brakePowerLimit = propManager.createPersistentProperty(getPrefix() + "BrakePowerLimit", .05);
        currentCalibrationSensorPosition = propManager
                .createPersistentProperty(getPrefix() + "CalibrationSensorPosition", -1);
    }

    public void stop() {
        setPower(0);
    }

    public void raiseElevator() {
        setPower(elevatorStandardPower.get());
    }

    public void lowerElevator() {
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

    protected void setPower(double power) {
        if (contract.isElevatorReady()) {
            if (contract.isElevatorLimitSwitchReady() && isCalibrationSensorPressed()) {
                power = MathUtils.constrainDouble(power, 0, 1);
                calibrate();
            }
            if (Math.abs(power) > brakePowerLimit.get()) {
                allowElevatorMotionSolenoid.setOn(true);
            } else {
                allowElevatorMotionSolenoid.setOn(false);
            }
            master.simpleSet(power);
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
}