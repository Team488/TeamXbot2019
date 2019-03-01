package competition.subsystems.vision.commands;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class DrivesToVisionTargetCommand extends BaseCommand {

    final OperatorInterface oi;
    final VisionSubsystem visionSubsystem;
    final DriveSubsystem driveSubsystem;
    DoubleProperty leftPower;
    DoubleProperty rightPower; 

    public DrivesToVisionTargetCommand(OperatorInterface oi, 
    VisionSubsystem visionSubsystem, DriveSubsystem driveSubsystem, 
    DoubleProperty power, PropertyFactory propManager) {
        this.driveSubsystem = driveSubsystem;
        this.oi = oi;
        this.visionSubsystem = visionSubsystem;
        this.requires(this.visionSubsystem);
        propManager.setPrefix(this);
        leftPower = propManager.createPersistentProperty("leftPower", 1);       
        rightPower = propManager.createPersistentProperty("rightPower", 1);

    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if(visionSubsystem.isTargetInView()){
            driveSubsystem.drive(leftPower.get(), rightPower.get());
        } else {
            driveSubsystem.stop();
        }
    }
}