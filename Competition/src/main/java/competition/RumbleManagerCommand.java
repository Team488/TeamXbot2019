package competition;

import com.google.inject.Inject;

import competition.subsystems.rumble.RumbleSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class RumbleManagerCommand extends BaseCommand {

    final VisionSubsystem visionSubsystem;
    final RumbleManager rumble;
    final DoubleProperty rumbleIntensity;
    final DoubleProperty rumbleLength;
    protected boolean wasTargetInView;

    @Inject
    public RumbleManagerCommand(VisionSubsystem visionSubsystem, RumbleManager rumble, PropertyFactory propFactory,
            RumbleSubsystem rumbleSubsystem) {
        this.requires(rumbleSubsystem);
        this.visionSubsystem = visionSubsystem;
        this.rumble = rumble;
        propFactory.setPrefix(this.getPrefix());
        rumbleIntensity = propFactory.createPersistentProperty("RumbleIntensity", .5);
        rumbleLength = propFactory.createPersistentProperty("RumbleLength", .1);
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
