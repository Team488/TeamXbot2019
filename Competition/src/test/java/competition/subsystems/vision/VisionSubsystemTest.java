package competition.subsystems.vision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;

import competition.BaseCompetitionTest;

public class VisionSubsystemTest extends BaseCompetitionTest {
    VisionSubsystem visionSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        visionSubsystem = this.injector.getInstance(VisionSubsystem.class);
    }

    @org.junit.Test
    public void testVisionSubsystemConstructor() {
        VisionSubsystem visionSubsystemTest = this.injector.getInstance(VisionSubsystem.class);
    }
    
    @org.junit.Test
    public void testHandleNormalNumbersPacket() {
        visionSubsystem.handlePacket("{ \"yaw\":100, \"hasTarget\":true }");
        assertTrue(visionSubsystem.isTargetInView());
        assertEquals(100, visionSubsystem.getAngleToTarget(), 0.001);      
        visionSubsystem.handlePacket("{ \"yaw\":-100, \"hasTarget\":true }");  
        assertTrue(visionSubsystem.isTargetInView());
        assertEquals(-100, visionSubsystem.getAngleToTarget(), 0.001);   
        visionSubsystem.handlePacket("{ \"yaw\":-181, \"hasTarget\":true }");
        assertFalse(visionSubsystem.isTargetInView());
        assertEquals(0, visionSubsystem.getAngleToTarget(), 0.001); 
        visionSubsystem.handlePacket("{ \"yaw\":181, \"hasTarget\":true }");
        assertFalse(visionSubsystem.isTargetInView());
        assertEquals(0, visionSubsystem.getAngleToTarget(), 0.001);
    }
    @org.junit.Test
    public void testBadPackets(){
        visionSubsystem.handlePacket("hello");
        assertFalse(visionSubsystem.isTargetInView());
        assertEquals(0, visionSubsystem.getAngleToTarget(), 0.001);

        visionSubsystem.handlePacket("!230**");
        assertFalse(visionSubsystem.isTargetInView());
        assertEquals(0, visionSubsystem.getAngleToTarget(), 0.001);
    }

}