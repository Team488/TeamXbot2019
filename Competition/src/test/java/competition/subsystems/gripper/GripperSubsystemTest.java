package competition.subsystems.gripper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import edu.wpi.first.wpilibj.MockDigitalInput;
import xbot.common.controls.sensors.XDigitalInput;

public class GripperSubsystemTest extends BaseCompetitionTest {

    @Test
    public void testDoesntCrashWhenCreated() {
        GripperSubsystem gripperSubsystem = this.injector.getInstance(GripperSubsystem.class);
    }

    @Test
    public void testCurrentlyHasDisk()
    {
        GripperSubsystem gripperSubsystem = this.injector.getInstance(GripperSubsystem.class);
        
        // Set the sensor to false
        ((MockDigitalInput)gripperSubsystem.diskSensor).setValue(false);
        // Check the method returns false
        assertFalse("Sensor doesn't have disk", gripperSubsystem.currentlyHasDisk());
        // Set the sensor to true
        ((MockDigitalInput)gripperSubsystem.diskSensor).setValue(true);
        // Check the method returns true 
        assertTrue("Sensor does have disk", gripperSubsystem.currentlyHasDisk());;       
    }

    @Test
    public void testGrabHatch(){
        GripperSubsystem gripperSubsystem = this.injector.getInstance(GripperSubsystem.class);
        gripperSubsystem.gripperPiston.setOn(false);
        assertFalse("Piston is false", (gripperSubsystem.gripperPiston.getAdjusted()));
        gripperSubsystem.grabHatch();
        assertTrue("Piston is true", (gripperSubsystem.gripperPiston.getAdjusted()));
    }

    @Test
    public void testreleaseHatch(){
        GripperSubsystem gripperSubsystem = this.injector.getInstance(GripperSubsystem.class);
        gripperSubsystem.gripperPiston.setOn(true);
        assertTrue("Piston is true", (gripperSubsystem.gripperPiston.getAdjusted()));
        gripperSubsystem.releaseHatch();
        assertFalse("Piston is false", (gripperSubsystem.gripperPiston.getAdjusted()));
    }
}