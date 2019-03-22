package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class PrecisionRotateCommand extends BaseCommand 
{
    protected HeadingModule headingMod;
    protected PropertyFactory propFactory;
    final OperatorInterface oi;
    protected DriveSubsystem driveSubsystem;
    protected PoseSubsystem poseSubsystem;
    protected ContiguousHeading goal;
    protected final DoubleProperty rotationValue;
    
    


    @Inject
    public PrecisionRotateCommand(CommonLibFactory clf, PropertyFactory propFactory, OperatorInterface oi, DriveSubsystem driveSubsystem, 
    PoseSubsystem poseSubsystem)
    {
        
        headingMod = clf.createHeadingModule(driveSubsystem.getRotateToHeadingPid());
        this.propFactory = propFactory;
        this.oi = oi;
        this.driveSubsystem = driveSubsystem;
        this.poseSubsystem = poseSubsystem;
        
        goal = new ContiguousHeading();
        propFactory.setPrefix(this);
        rotationValue = propFactory.createPersistentProperty("rotationValue", 0.5);
        
    }
    @Override
    public void initialize() {
       goal = poseSubsystem.getCurrentHeading();
       headingMod.reset();

    }

    //douple property
    @Override
    public void execute() {
        //maybe need to adjust value
        goal.shiftValue(oi.driverGamepad.getRightStickX()*rotationValue.get());

        double rotatePower = headingMod.calculateHeadingPower(goal.getValue());
        double forwardPower = oi.driverGamepad.getRightStickY();
        
        driveSubsystem.drive(new XYPair(0, forwardPower), rotatePower);
    }

}