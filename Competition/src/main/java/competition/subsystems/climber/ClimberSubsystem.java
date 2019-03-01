package competition.subsystems.climber;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.ElectricalContract2019;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class ClimberSubsystem extends BaseSubsystem {

    private static Logger log = Logger.getLogger(ClimberSubsystem.class);
    public final XSolenoid frontDeploySolenoid;
    public final XSolenoid frontRetractSolenoid;
    public final XSolenoid backDeploySolenoid;
    public final XSolenoid backRetractSolenoid;

    @Inject
    public ClimberSubsystem(CommonLibFactory clf, ElectricalContract2019 contract) {
        log.info("Creating Climber Subsystem");
        if (contract.isClimberReady()) {
            this.frontDeploySolenoid = clf.createSolenoid(contract.getFrontDeploySolenoid().channel);
            this.frontRetractSolenoid = clf.createSolenoid(contract.getFrontRetractSolenoid().channel);
            this.backDeploySolenoid = clf.createSolenoid(contract.getBackDeploySolenoid().channel);
            this.backRetractSolenoid = clf.createSolenoid(contract.getBackRetractSolenoid().channel);

        } else {
            this.frontDeploySolenoid = null;
            this.frontRetractSolenoid = null;
            this.backDeploySolenoid = null;
            this.backRetractSolenoid = null;
        }
    }

    public void deployFront() {
        frontDeploySolenoid.setOn(true);
        frontRetractSolenoid.setOn(false);
    }

    public void retractFront() {
        frontDeploySolenoid.setOn(false);
        frontRetractSolenoid.setOn(true);
    }

    public void deployBack() {
        backDeploySolenoid.setOn(true);
        backRetractSolenoid.setOn(false);
    }

    public void retractBack() {
        backDeploySolenoid.setOn(false);
        backRetractSolenoid.setOn(true);
    }
}