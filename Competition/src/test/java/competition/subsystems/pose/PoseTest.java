package competition.subsystems.pose;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;

import xbot.common.math.XYPair;

public class PoseTest {
    @Test
    public void testFlipFieldPoseFrom180To0() {
        
        FieldPose right1 = new FieldPose(new XYPair(324, 10), new ContiguousHeading(180));
        FieldPose leftExpected1 = new FieldPose(new XYPair(0, 10), new ContiguousHeading(0));

        assertEquals(leftExpected1.getPoint().x, PoseSubsystem.flipFieldPose(right1).getPoint().x, 0);  
        assertEquals(leftExpected1.getPoint().y, PoseSubsystem.flipFieldPose(right1).getPoint().y, 0);
        assertEquals(leftExpected1.getHeading().getValue(), PoseSubsystem.flipFieldPose(right1).getHeading().getValue(), 0);     
    }

    @Test
    public void testFlipFieldPoseFrom92To88() {
        
        FieldPose right1 = new FieldPose(new XYPair(324, 10), new ContiguousHeading(92));
        FieldPose leftExpected1 = new FieldPose(new XYPair(0, 10), new ContiguousHeading(88));

        assertEquals(leftExpected1.getPoint().x, PoseSubsystem.flipFieldPose(right1).getPoint().x, 0);  
        assertEquals(leftExpected1.getPoint().y, PoseSubsystem.flipFieldPose(right1).getPoint().y, 0);
        assertEquals(leftExpected1.getHeading().getValue(), PoseSubsystem.flipFieldPose(right1).getHeading().getValue(), 0);     
    }

    @Test
    public void testFlipFieldPoseFrom88To92() {
        
        FieldPose right1 = new FieldPose(new XYPair(0, 30), new ContiguousHeading(88));
        FieldPose leftExpected1 = new FieldPose(new XYPair(324, 30), new ContiguousHeading(92));

        assertEquals(leftExpected1.getPoint().x, PoseSubsystem.flipFieldPose(right1).getPoint().x, 0);  
        assertEquals(leftExpected1.getPoint().y, PoseSubsystem.flipFieldPose(right1).getPoint().y, 0);
        assertEquals(leftExpected1.getHeading().getValue(), PoseSubsystem.flipFieldPose(right1).getHeading().getValue(), 0);         
    }
    @Test
    public void testFlipFieldPoseFrom190To350() {
        
        FieldPose right1 = new FieldPose(new XYPair(0, 30), new ContiguousHeading(190));
        FieldPose leftExpected1 = new FieldPose(new XYPair(324, 30), new ContiguousHeading(350));

        assertEquals(leftExpected1.getPoint().x, PoseSubsystem.flipFieldPose(right1).getPoint().x, 0);  
        assertEquals(leftExpected1.getPoint().y, PoseSubsystem.flipFieldPose(right1).getPoint().y, 0);
        assertEquals(leftExpected1.getHeading().getValue(), PoseSubsystem.flipFieldPose(right1).getHeading().getValue(), 0);         
    }

    @Test
    public void testFlipFieldPoseFrom270To270() {
        
        FieldPose right1 = new FieldPose(new XYPair(0, 30), new ContiguousHeading(270));
        FieldPose leftExpected1 = new FieldPose(new XYPair(324, 30), new ContiguousHeading(270));

        assertEquals(leftExpected1.getPoint().x, PoseSubsystem.flipFieldPose(right1).getPoint().x, 0);  
        assertEquals(leftExpected1.getPoint().y, PoseSubsystem.flipFieldPose(right1).getPoint().y, 0);
        assertEquals(leftExpected1.getHeading().getValue(), PoseSubsystem.flipFieldPose(right1).getHeading().getValue(), 0);         
    }
    
    @Test
    public void testFlipFieldPoseFrom0To180() {
        
        FieldPose right1 = new FieldPose(new XYPair(0, 30), new ContiguousHeading(0));
        FieldPose leftExpected1 = new FieldPose(new XYPair(324, 30), new ContiguousHeading(180));

        assertEquals(leftExpected1.getPoint().x, PoseSubsystem.flipFieldPose(right1).getPoint().x, 0);  
        assertEquals(leftExpected1.getPoint().y, PoseSubsystem.flipFieldPose(right1).getPoint().y, 0);
        assertEquals(leftExpected1.getHeading().getValue(), PoseSubsystem.flipFieldPose(right1).getHeading().getValue(), 0);         
    }
    
    @Test
    public void testFlipFieldPoseFrom370To170() {
        
        FieldPose right1 = new FieldPose(new XYPair(0, 30), new ContiguousHeading(370));
        FieldPose leftExpected1 = new FieldPose(new XYPair(324, 30), new ContiguousHeading(170));

        assertEquals(leftExpected1.getPoint().x, PoseSubsystem.flipFieldPose(right1).getPoint().x, 0);  
        assertEquals(leftExpected1.getPoint().y, PoseSubsystem.flipFieldPose(right1).getPoint().y, 0);
        assertEquals(leftExpected1.getHeading().getValue(), PoseSubsystem.flipFieldPose(right1).getHeading().getValue(), 0);         
    }
    
    @Test
    public void testFlipFieldPoseFromNeg10To190() {
        
        FieldPose right1 = new FieldPose(new XYPair(0, 30), new ContiguousHeading(-10));
        FieldPose leftExpected1 = new FieldPose(new XYPair(324, 30), new ContiguousHeading(190));

        assertEquals(leftExpected1.getPoint().x, PoseSubsystem.flipFieldPose(right1).getPoint().x, 0);  
        assertEquals(leftExpected1.getPoint().y, PoseSubsystem.flipFieldPose(right1).getPoint().y, 0);
        assertEquals(leftExpected1.getHeading().getValue(), PoseSubsystem.flipFieldPose(right1).getHeading().getValue(), 0);         
    }    
  
}
