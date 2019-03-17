package competition.subsystems.climber;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2019;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.ElectricalContract.DeviceInfo;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class MotorClimberSubsystem extends BaseSubsystem implements PeriodicDataSource {

    public XCANTalon frontLeft;
    public XCANTalon frontRight;
    public XCANTalon rearLeft;
    public XCANTalon rearRight;

    CommonLibFactory clf;
    ElectricalContract2019 contract;

    private final BooleanProperty frontLeftLimitProp;
    private final BooleanProperty frontRightLimitProp;
    private final BooleanProperty rearLeftLimitProp;
    private final BooleanProperty rearRightLimitProp;

    private List<XCANTalon> motors;

    @Inject
    public MotorClimberSubsystem(CommonLibFactory clf, PropertyFactory propFactory, ElectricalContract2019 contract) {
        this.clf = clf;
        this.contract = contract;
        propFactory.setPrefix(this.getPrefix());
        motors = new ArrayList<XCANTalon>();

        if (contract.isFrontMotorClimberReady()) {
            frontLeft = setupMotor(contract.getFrontLeftClimber());
            frontLeft.configureAsMasterMotor(getPrefix(), "FrontLeft", contract.getFrontLeftClimber().inverted,
                    contract.getFrontLeftEncoder().inverted);

            frontRight = setupMotor(contract.getFrontRightClimber());
            frontRight.configureAsMasterMotor(getPrefix(), "FrontRight", contract.getFrontRightClimber().inverted,
                    contract.getFrontRightEncoder().inverted);

            motors.add(frontLeft);
            motors.add(frontRight);
        }

        if (contract.isRearMotorClimberReady()) {
            rearLeft = setupMotor(contract.getRearLeftClimber());
            rearLeft.configureAsMasterMotor(getPrefix(), "RearLeft", contract.getRearLeftClimber().inverted,
                    contract.getRearLeftEncoder().inverted);

            rearRight = setupMotor(contract.getRearRightClimber());
            rearRight.configureAsMasterMotor(getPrefix(), "RearRight", contract.getRearRightClimber().inverted,
                    contract.getRearRightEncoder().inverted);

            motors.add(rearLeft);
            motors.add(rearRight);
        }

        frontLeftLimitProp = propFactory.createEphemeralProperty("FrontLeftLimit", false);
        frontRightLimitProp = propFactory.createEphemeralProperty("FrontRightLimit", false);
        rearLeftLimitProp = propFactory.createEphemeralProperty("RearLeftLimit", false);
        rearRightLimitProp = propFactory.createEphemeralProperty("RearRightLimit", false);

        enableSafeties();
    }

    private void enableSafeties() {
        /*
        for (XCANTalon motor : motors) {
            motor.configReverseSoftLimitEnable(true, 0);
            motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen,
                    0);
        }
        */
    }

    private XCANTalon setupMotor(DeviceInfo info) {
        XCANTalon talon = clf.createCANTalon(info.channel);
        talon.setInverted(info.inverted);
        return talon;
    }

    public void setFrontPower(double lift, double tilt) {
        if (contract.isFrontMotorClimberReady()) {
            frontLeft.simpleSet(lift - tilt);
            frontRight.simpleSet(lift + tilt);
        }
    }

    public void setRearPower(double lift, double tilt) {
        if (contract.isRearMotorClimberReady()) {
            rearLeft.simpleSet(lift - tilt);
            rearRight.simpleSet(lift + tilt);
        }
    }

    @Override
    public void updatePeriodicData() {
        frontLeftLimitProp.set(frontLeft.isRevLimitSwitchClosed());
        frontRightLimitProp.set(frontRight.isRevLimitSwitchClosed());
        rearLeftLimitProp.set(rearLeft.isRevLimitSwitchClosed());
        rearRightLimitProp.set(rearRight.isRevLimitSwitchClosed());
    }
}