package competition.subsystems.gripper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import edu.wpi.first.wpilibj.MockDigitalInput;

public class GripperSubsystemTest extends BaseCompetitionTest {

    GripperSubsystem gripperSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        gripperSubsystem = this.injector.getInstance(GripperSubsystem.class);
    }

    @Test
    public void testCurrentlyHasDisk()
    {        
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
        gripperSubsystem.gripperDiscPiston.setOn(false);
        assertFalse("Piston is false", (gripperSubsystem.gripperDiscPiston.getAdjusted()));
        gripperSubsystem.grabHatch();
        assertTrue("Piston is true", (gripperSubsystem.gripperDiscPiston.getAdjusted()));
    }

    @Test
    public void testreleaseHatch(){
        gripperSubsystem.gripperDiscPiston.setOn(true);
        assertTrue("Piston is true", (gripperSubsystem.gripperDiscPiston.getAdjusted()));
        gripperSubsystem.releaseHatch();
        assertFalse("Piston is false", (gripperSubsystem.gripperDiscPiston.getAdjusted()));
    }

    @Test
    public void testExtendRetract() {
        verifyExtensionState(false);
        gripperSubsystem.setExtension(true);
        verifyExtensionState(true);
        gripperSubsystem.setExtension(false);
        verifyExtensionState(false);
    }

    public void verifyExtensionState(boolean extended) {
        assertEquals(extended, gripperSubsystem.gripperExtensionPiston.getAdjusted());
    }
}