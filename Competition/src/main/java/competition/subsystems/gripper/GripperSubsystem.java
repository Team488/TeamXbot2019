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
    public XSolenoid gripperPiston;
    public XDigitalInput diskSensor;
    public boolean gripperReady;

    @Inject
    public GripperSubsystem(CommonLibFactory clf, ElectricalContract2019 contract) {
        gripperReady = contract.isGripperReady();

        if (gripperReady) {
            gripperPiston = clf.createSolenoid(contract.getGripperSolenoid().channel);
            gripperPiston.setInverted(contract.getGripperSolenoid().inverted);
        
            diskSensor = clf.createDigitalInput(contract.getGripperSensor().channel);
            diskSensor.setInverted(contract.getGripperSensor().inverted);
        }
        
    }

    public void grabHatch(){
        if (gripperReady)
        {
            gripperPiston.setOn(true);
        } 
    }
    public void releaseHatch(){
        if (gripperReady)
        {
            gripperPiston.setOn(false);
        } 
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