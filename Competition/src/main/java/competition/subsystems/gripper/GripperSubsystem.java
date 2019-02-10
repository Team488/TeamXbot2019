package competition.subsystems.gripper;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2019;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class GripperSubsystem extends BaseSubsystem {
    public XSolenoid gripperDiscPiston;
    public XSolenoid gripperExtensionPiston;
    public XDigitalInput diskSensor;
    public boolean gripperReady;

    @Inject
    public GripperSubsystem(CommonLibFactory clf, ElectricalContract2019 contract) {
        gripperReady = contract.isGripperReady();

        if (gripperReady) {
            gripperDiscPiston = clf.createSolenoid(contract.getGripperDiscSolenoid().channel);
            gripperDiscPiston.setInverted(contract.getGripperDiscSolenoid().inverted);
        
            diskSensor = clf.createDigitalInput(contract.getGripperSensor().channel);
            diskSensor.setInverted(contract.getGripperSensor().inverted);

            gripperExtensionPiston = clf.createSolenoid(contract.getGripperExtensionSolenoid().channel);
            gripperExtensionPiston.setInverted(contract.getGripperExtensionSolenoid().inverted);
        }
    }

    public void grabHatch(){
        if (gripperReady)
        {
            gripperDiscPiston.setOn(true);
        } 
    }
    public void releaseHatch(){
        if (gripperReady)
        {
            gripperDiscPiston.setOn(false);
        } 
    }

    public void setExtension(boolean extend) {
        gripperExtensionPiston.setOn(extend);
    }

    public boolean currentlyHasDisk()
    {
        if (gripperReady)
        {
            return diskSensor.get();
        }
        return false;
    }
}