package competition.subsystems.climber;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.injection.ElectricalContract.DeviceInfo;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public abstract class BaseMotorClimberSubsystem extends BaseSetpointSubsystem implements PeriodicDataSource {

    public enum EncoderAdjustment {
        Raw, Hard, Floor
    }

    public XCANTalon leftMotor;
    public XCANTalon rightMotor;
    public XDigitalInput leftLimit;
    public XDigitalInput rightLimit;

    private final BooleanProperty leftLimitProp;
    private final BooleanProperty rightLimitProp;
    private final DoubleProperty maximumLegTravelInTicks;

    private final Latch leftCalibrationLatch;
    private final Latch rightCalibrationLatch;

    private double leftHardOffset;
    private double leftFloorOffset;
    private double rightHardOffset;
    private double rightFloorOffset;
    private boolean leftCalibrated;
    private boolean rightCalibrated;

    private boolean areMotorsReady;

    // Used by PID
    private double leftTickGoal = 0;
    private double rightTickGoal = 0;

    /**
     * @return the leftTickGoal
     */
    public double getLeftTickGoal() {
        return leftTickGoal;
    }

    /**
     * @param leftTickGoal the leftTickGoal to set
     */
    public void setLeftTickGoal(double leftTickGoal) {
        this.leftTickGoal = leftTickGoal;
    }

    /**
     * @return the rightTickGoal
     */
    public double getRightTickGoal() {
        return rightTickGoal;
    }

    /**
     * @param rightTickGoal the rightTickGoal to set
     */
    public void setRightTickGoal(double rightTickGoal) {
        this.rightTickGoal = rightTickGoal;
    }

    /**
     * The generic MotorClimberSubsystem that will be extended by the Front and Rear
     * climbers. It has all the logic so they don't need any.
     * 
     * @param clf              CommonLibFactory
     * @param propFactory      PropertyFactory
     * @param leftMotorInfo    Left Motor info
     * @param leftEncoderInfo  Left Encoder info
     * @param leftLimitInfo    Left Limit Switch (hall effect sensor) info
     * @param rightMotorInfo   Right Motor info
     * @param rightEncoderInfo Right encoder info
     * @param rightLimitInfo   Right Limit Switch (hall effect sensor) info
     * @param side             "Front" or "Back"
     * @param prefix           Really the name of the derived class, like
     *                         "FrontMotorClimberSubsystem"
     */
    public BaseMotorClimberSubsystem(CommonLibFactory clf, PropertyFactory propFactory, DeviceInfo leftMotorInfo,
            DeviceInfo leftEncoderInfo, DeviceInfo leftLimitInfo, DeviceInfo rightMotorInfo,
            DeviceInfo rightEncoderInfo, DeviceInfo rightLimitInfo, String side, String prefix,
            boolean areMotorsReady) {
        propFactory.setPrefix(prefix);
        this.areMotorsReady = areMotorsReady;

        // Create the left and right motors, and set them as master motors. They don't
        // have followers,
        // but it's a convenient function that will set up the motor inversion and
        // encoder inversion for us
        // while clearing any bad config. Go read configureAsMasterMotor, it does a lot!
        if (areMotorsReady) {
            leftMotor = clf.createCANTalon(leftMotorInfo.channel);
            leftMotor.configureAsMasterMotor(getPrefix(), side + "Left", leftMotorInfo.inverted,
                    leftEncoderInfo.inverted);

            rightMotor = clf.createCANTalon(rightMotorInfo.channel);
            rightMotor.configureAsMasterMotor(getPrefix(), side + "Right", rightMotorInfo.inverted,
                    rightEncoderInfo.inverted);

            // Set the voltage ramp to a small number - this helps prevent extreme
            // instantaneous motor motions, which would
            // put extra physical strain on the system.
            setVoltageRamp(leftMotor, 0.2);
            setVoltageRamp(rightMotor, 0.2);

            // Set motors to brake mode so they'll resist motion when neutral
            leftMotor.setNeutralMode(NeutralMode.Brake);
            rightMotor.setNeutralMode(NeutralMode.Brake);

            // Create the digital inputs which represent our Hall Effect sensors. They will
            // detect when the legs are too retracted to retract any more.
            leftLimit = clf.createDigitalInput(leftLimitInfo.channel);
            leftLimit.setInverted(leftLimitInfo.inverted);
            rightLimit = clf.createDigitalInput(rightLimitInfo.channel);
            rightLimit.setInverted(rightLimitInfo.inverted);
        }

        leftLimitProp = propFactory.createEphemeralProperty(side + "LeftLimit", false);
        rightLimitProp = propFactory.createEphemeralProperty(side + "RightLimit", false);
        // This property controls how far the legs can extend from the hall effect
        // sensors.
        maximumLegTravelInTicks = propFactory.createPersistentProperty("MaximumLegTravelInTicks", 100000);

        // We also setup two calibration latches, so we will only hard calibrate on the
        // first jump from false->true.
        leftCalibrationLatch = new Latch(false, EdgeType.Both, edge ->
        {
            if (edge == EdgeType.RisingEdge) {
                log.info("Calibrating left!");
                leftHardCalibrate();
            }
        });

        rightCalibrationLatch = new Latch(false, EdgeType.Both, edge -> {
            if (edge == EdgeType.RisingEdge) {
                log.info("Calibrating right!");
                rightHardCalibrate();
            }
        });
    }

    /**
     * This is a simple way to simultaneously control lift and tilt with raw power.
     * 
     * @param lift Lifting power. Positive causes the robot to rise.
     * @param tilt Tilting power. Positive causes the robot to tilt left.
     */
    public void setLiftAndTilt(double lift, double tilt) {
        double leftPower = lift - tilt;
        double rightPower = lift + tilt;
        setLeftPower(leftPower);
        setRightPower(rightPower);
    }

    /**
     * Used to directly set power to the left leg. Useful for PID control by a
     * command.
     * 
     * @param power power to the left leg.
     */
    public void setLeftPower(double power) {
        setPower(leftMotor, leftCalibrationLatch, leftLimit, power);
    }

    /**
     * Used to directly set power to the right leg. Useful for PID control by a
     * command.
     * 
     * @param power power to the right leg.
     */
    public void setRightPower(double power) {
        setPower(rightMotor, rightCalibrationLatch, rightLimit, power);
    }

    /**
     * Used to set power on a leg motor.
     * 
     * @param motor            The leg motor to move.
     * @param calibrationLatch Its calibration latch
     * @param limit            Its limit switch
     * @param power            How much power. Positive causes the leg to extend.
     */
    private void setPower(XCANTalon motor, Latch calibrationLatch, XDigitalInput limit, double power) {
        if (areMotorsReady) {
            calibrationLatch.setValue(limit.get());
            if (limit.get()) {
                power = MathUtils.constrainDouble(power, 0, 1);
            }
            motor.simpleSet(power);
        }
    }

    /**
     * Gets the number of ticks on the left leg, adjusted per your desired offset
     * 
     * @param adjustment which offset to use
     * @return number of ticks
     */
    public double getLeftTicks(EncoderAdjustment adjustment) {
        return getAdjustedTicks(leftMotor, adjustment, leftHardOffset, leftFloorOffset);
    }

    /**
     * Gets the number of ticks on the right leg, adjusted per your desired offset
     * 
     * @param adjustment which offset to use
     * @return number of ticks
     */
    public double getRightTicks(EncoderAdjustment adjustment) {
        return getAdjustedTicks(rightMotor, adjustment, rightHardOffset, rightFloorOffset);
    }

    /**
     * Gets the raw encoder ticks for a motor. Wrapped here so that we only have to
     * check if the motor is ready in one place.
     * 
     * @param motor which motor
     * @return raw encoder ticks
     */
    private double getRawTicks(XCANTalon motor) {
        if (areMotorsReady) {
            return motor.getSelectedSensorPosition(0);
        }
        return 0;
    }

    /**
     * Gets the adjusted tick value for any of our offsets.
     * 
     * @param motor       which motor encoder to measure
     * @param adjustment  which adjustment/offset to use
     * @param hardOffset  the hard offset value
     * @param floorOffset the floor offset value
     * @return the adjusted ticks
     */
    private double getAdjustedTicks(XCANTalon motor, EncoderAdjustment adjustment, double hardOffset,
            double floorOffset) {
        double offset = 0;
        double rawTicks = getRawTicks(motor);
        switch (adjustment) {
        case Raw:
            break;
        case Hard:
            offset = hardOffset;
            break;
        case Floor:
            offset = floorOffset;
            break;
        default:
            break;
        }
        return rawTicks - offset;
    }

    /**
     * Calibrates the left leg hard stop position.
     */
    private void leftHardCalibrate() {
        leftCalibrated = true;
        leftHardOffset = getLeftTicks(EncoderAdjustment.Raw);
        setSoftLimits(leftMotor, leftHardOffset);
    }

    /**
     * Calibrates the right leg hard stop position.
     */
    private void rightHardCalibrate() {
        rightCalibrated = true;
        rightHardOffset = getRightTicks(EncoderAdjustment.Raw);
        setSoftLimits(rightMotor, rightHardOffset);
    }

    /**
     * Gets the hard stop calibration state for the left leg.
     * 
     * @return true if leg has calibrated against hard stop.
     */
    public boolean getLeftCalibrated() {
        return leftCalibrated;
    }

    /**
     * Gets the hard stop calibration state for the right leg.
     * 
     * @return true if leg has calibrated against hard stop.
     */
    public boolean getRightCalibrated() {
        return rightCalibrated;
    }

    /**
     * Calibrates the legs assuming they are firmly pressed into the floor.
     */
    public void calibrateFloor() {
        leftFloorOffset = getLeftTicks(EncoderAdjustment.Raw);
        rightFloorOffset = getRightTicks(EncoderAdjustment.Raw);
    }

    /**
     * Enables and configures the XCANTalon soft limits.
     * 
     * @param motor      which motor to configure
     * @param hardOffset the tick position where the hard stop sensor was triggered.
     */
    private void setSoftLimits(XCANTalon motor, double hardOffset) {
        if (areMotorsReady) {
            motor.configReverseSoftLimitEnable(false, 0);
            motor.configReverseSoftLimitThreshold((int) hardOffset, 0);
            motor.configForwardSoftLimitEnable(true, 0);
            motor.configForwardSoftLimitThreshold((int) (hardOffset + maximumLegTravelInTicks.get()), 0);
        }
    }

    /**
     * Sets the voltage ramp for a given motor.
     * 
     * @param motor                    Which motor to configure
     * @param secondsFromNeutralToFull How many seconds it should take to go from
     *                                 neutral to full forward (or reverse) power.
     */
    public void setVoltageRamp(XCANTalon motor, double secondsFromNeutralToFull) {
        if (areMotorsReady) {
            motor.configOpenloopRamp(secondsFromNeutralToFull, 0);
            motor.configClosedloopRamp(secondsFromNeutralToFull, 0);
        }
    }

    /**
     * Updates properties for the dashboard.
     */
    @Override
    public void updatePeriodicData() {
        if (areMotorsReady) {
            leftLimitProp.set(leftLimit.get());
            rightLimitProp.set(rightLimit.get());
        }
    }

    public void setTickGoalsToCurrent(EncoderAdjustment encoderAdjustment) {
        this.setLeftTickGoal(this.getLeftTicks(encoderAdjustment));
        this.setRightTickGoal(this.getRightTicks(encoderAdjustment));
    }
}