package competition;

import com.google.inject.Inject;

import competition.subsystems.rumble.RumbleSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class RumbleManagerCommand extends BaseCommand {

    final VisionSubsystem visionSubsystem;
    final RumbleManager rumble;
    final DoubleProperty rumbleIntensity;
    final DoubleProperty rumbleLength;
    protected boolean wasTargetInView;

    @Inject
    public RumbleManagerCommand(VisionSubsystem visionSubsystem, RumbleManager rumble, XPropertyManager propManager,
            RumbleSubsystem rumbleSubsystem) {
        this.requires(rumbleSubsystem);
        this.visionSubsystem = visionSubsystem;
        this.rumble = rumble;
        rumbleIntensity = propManager.createPersistentProperty(getPrefix() + "RumbleIntensity", .5);
        rumbleLength = propManager.createPersistentProperty(getPrefix() + "RumbleLength", .1);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        wasTargetInView = visionSubsystem.isTargetInView();
        if (visionSubsystem.isTargetInView() && wasTargetInView == false) {
            rumble.rumbleDriverGamepad(rumbleIntensity.get(), rumbleLength.get());
        } 
    }
}
