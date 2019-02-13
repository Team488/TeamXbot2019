package competition.subsystems.vision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JSONObjectException;

import org.junit.Test;

public class VisionDataTest {
    @Test
    public void testVisionDataParsing() throws IOException {
        String input = "{\"targetYaw\":3}";
        VisionData m = JSON.std.beanFrom(VisionData.class, input);
        assertEquals(3, m.getTargetYaw().doubleValue(), 0.001);     
    }

    @Test
    public void testTypeMismatch() throws IOException {
        String input = "{\"targetYaw\": \"a\"}";
        VisionData m = JSON.std.beanFrom(VisionData.class, input);
        assertEquals(0, m.getTargetYaw().doubleValue(), 0.001);     
    }

    @Test
    public void testMissingData() throws IOException {
        String input = "{ }";
        VisionData m = JSON.std.beanFrom(VisionData.class, input);
        assertEquals("default int value", null, m.getTargetYaw());     
    }

    @Test
    public void testBadJSON() {
        String input = "{\"targetYaw\":3";
        try {
            VisionData m = JSON.std.beanFrom(VisionData.class, input);
            assertTrue("Should have raised exception", false);
        } catch(IOException e) {
            // expected
        }
    }
    
    
}