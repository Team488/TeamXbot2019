package competition.subsystems.climber;

import javax.sql.CommonDataSource;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2019;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.ElectricalContract.DeviceInfo;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;

@Singleton
public class MotorClimberSubsystem extends BaseSubsystem {

    public XCANTalon frontLeft;
    public XCANTalon frontRight;
    public XCANTalon rearLeft;
    public XCANTalon rearRight;

    CommonLibFactory clf;
    ElectricalContract2019 contract;

    @Inject
    public MotorClimberSubsystem(CommonLibFactory clf, PropertyFactory propFactory, ElectricalContract2019 contract) {
        this.clf = clf;
        this.contract = contract;

        if (contract.isMotorClimberReady()) {
            frontLeft = setupMotor(contract.getFrontLeftClimber());
            frontRight = setupMotor(contract.getFrontRightClimber());
            rearLeft = setupMotor(contract.getRearLeftClimber());
            rearRight = setupMotor(contract.getRearRightClimber());
        }
    }

    private XCANTalon setupMotor(DeviceInfo info) {
        XCANTalon talon = clf.createCANTalon(info.channel);
        talon.setInverted(info.inverted);
        return talon;
    }

    public void setFrontPower(double lift, double tilt) {
        if (contract.isMotorClimberReady()) {
            frontLeft.simpleSet(lift - tilt);
            frontRight.simpleSet(lift + tilt);
        }
    }

    public void setRearPower(double lift, double tilt) {
        if (contract.isMotorClimberReady()) {
            rearLeft.simpleSet(lift - tilt);
            rearRight.simpleSet(lift + tilt);
        }
    }
}