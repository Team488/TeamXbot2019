package competition.subsystems.roller_gripper;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2019;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class RollerGripperSubsystem extends BaseSubsystem {

    final public boolean gripperReady;
    final public DoubleProperty grabPowerProperty;
    final public DoubleProperty releasePowerProperty;
    final public XCANTalon rollerMotor;
   
    @Inject
    public RollerGripperSubsystem(CommonLibFactory clf, ElectricalContract2019 contract, PropertyFactory propFactory) {
        gripperReady = contract.isRollerGrabberReady();
        propFactory.setPrefix(this.getPrefix());

        grabPowerProperty = propFactory.createPersistentProperty("Grab Power", 0.5);
        releasePowerProperty = propFactory.createPersistentProperty("Release Power", -0.5);
        if (gripperReady) {
            rollerMotor = clf.createCANTalon(contract.getRollerGrabber().channel);
            rollerMotor.configureAsMasterMotor(getPrefix(), "roller", contract.getRollerGrabber().inverted, false);
        } else {
            rollerMotor = null;
        }
    }

    public void grabHatch() {
        if (gripperReady) {
            rollerMotor.simpleSet(this.grabPowerProperty.get());
        }
    }

    public void releaseHatch() {
        if (gripperReady) {
            rollerMotor.simpleSet(this.releasePowerProperty.get());
        }
    }

    public void stop() {
        if (gripperReady) {
            rollerMotor.simpleSet(0.0);
        }
    }
}