package competition.subsystems.climber;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2019;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class FourBarSubsystem extends BaseSubsystem {

    protected XCANTalon master;
    protected XCANTalon follower;
    private final ElectricalContract2019 contract;
    public final DoubleProperty fourBarPower;
    public final DoubleProperty fourBarMaxHeight;
    public double startingPos;
    public int upperLimit;
    public int lowerLimit;

    @Inject
    public FourBarSubsystem(CommonLibFactory clf, PropertyFactory propFactory, ElectricalContract2019 contract) {
        propFactory.setPrefix(this.getPrefix());
        log.info("Creating Four Bar Climber Subsystem");
        this.contract = contract;
        if (contract.isFourBarReady()) {
            this.master = clf.createCANTalon(contract.getFourBarMaster().channel);
            this.follower = clf.createCANTalon(contract.getFourBarFollower().channel);
        }
        fourBarPower = propFactory.createPersistentProperty("Standard Four Bar Power", 1);
        fourBarMaxHeight = propFactory.createPersistentProperty("Maxiumum movement for Four Bar", 1000);
        enableSoftLimit();
        configSoftLimit();
    }
    public void deploy() {
        setPower(fourBarPower.get());
    }
    public void retract() {
        setPower(-fourBarPower.get());
    }

    public void stop() {
        setPower(0);
    }

    public void setPower(double power) {
        if (contract.isFourBarReady()) {
            master.simpleSet(power);
        }
    }

    public double getStartPos() {
        startingPos = master.getSelectedSensorPosition(0);
        return startingPos;
    }

    public void configSoftLimit() {
        upperLimit = (int)startingPos + (int)fourBarMaxHeight.get();
        lowerLimit = (int)startingPos;

        master.configForwardSoftLimitThreshold(upperLimit, 0);
        master.configReverseSoftLimitThreshold(lowerLimit, 0);
    }

    private void enableSoftLimit() {
        master.configForwardSoftLimitEnable(true, 0);
        master.configReverseSoftLimitEnable(true, 0);
    }

    private void disableSoftLimit() {
        master.configForwardSoftLimitEnable(false, 0);
        master.configReverseSoftLimitEnable(false, 0);
    }


    

}