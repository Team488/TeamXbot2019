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

    public final XCANTalon master;
    public final XCANTalon follower;
    public final DoubleProperty fourBarPower;
    private final ElectricalContract2019 contract;

    @Inject
    public FourBarSubsystem(CommonLibFactory clf, PropertyFactory propFactory, ElectricalContract2019 contract) {
        propFactory.setPrefix(this.getPrefix());
        log.info("Creating Four Bar Climber Subsystem");
        this.contract = contract;
        if (contract.isFourBarReady()) {
            this.master = clf.createCANTalon(contract.getFourBarMaster().channel);
            this.follower = clf.createCANTalon(contract.getFourBarFollower().channel);
        } else {
            this.master = null;
            this.follower = null;
        }
        fourBarPower = propFactory.createPersistentProperty("Standard Four Bar Power", 1);
    }

    public void raiseFourBar() {
        setPower(fourBarPower.get());
    }

    public void lowerFourBar() {
        setPower(-fourBarPower.get());
    }

    public void stopFourBar() {
        setPower(0);
    }

    public void setPower(double power) {
        if (contract.isFourBarReady()) {
            master.simpleSet(power);
        }
    }

}