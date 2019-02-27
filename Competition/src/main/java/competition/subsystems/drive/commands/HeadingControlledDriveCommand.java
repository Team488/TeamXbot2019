package competition.subsystems.drive.commands;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.control_logic.HeadingAssistModule;

public abstract class HeadingControlledDriveCommand extends BaseCommand {

    protected CommonLibFactory clf;
    protected DriveSubsystem drive;
    protected XPropertyManager propMan;
    final DoubleProperty deadbandProp;
    final HeadingAssistModule ham;

    protected abstract double getHumanTranslationInput();

    protected abstract double getHumanRotationInput();

    protected double getTranslation() {
        if (Math.abs(getHumanTranslationInput()) < Math.abs(deadbandProp.get())) 
        {
            return 0;
        }
        return getHumanTranslationInput();
    }

    protected double getRotation() {
        return ham.calculateHeadingPower(getHumanRotationInput());
    }

    public HeadingControlledDriveCommand(CommonLibFactory clf, DriveSubsystem drive, XPropertyManager propMan) {
        this.clf = clf;
        this.drive = drive;
        this.propMan = propMan;

        deadbandProp = propMan.createPersistentProperty(getPrefix() + "Deadband", 0.05);

        ham = clf.createHeadingAssistModule(
            clf.createHeadingModule(drive.getRotateToHeadingPid()),
            clf.createHeadingModule(drive.getRotateDecayPid()));

            this.requires(drive);
    }

    @Override
    public void initialize() {
        ham.reset();
    }

    @Override
    public void execute() {
        drive.drive(new XYPair(0, getTranslation()), getRotation());
    }
}