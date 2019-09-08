package competition;

import com.google.inject.Inject;

import competition.subsystems.rumble.RumbleSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.XTimer;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class RumbleManagerCommand extends BaseCommand {

    final VisionSubsystem visionSubsystem;
    final RumbleManager rumble;
    final DoubleProperty rumbleIntensity;
    final DoubleProperty rumbleLength;
    final DoubleProperty timeBetweenRumble;
    protected boolean wasTargetInView;
    private boolean isRisingEdge;
    private Double lastRumbleCall;

    @Inject
    public RumbleManagerCommand(VisionSubsystem visionSubsystem, RumbleManager rumble, PropertyFactory propFactory,
            RumbleSubsystem rumbleSubsystem) {
        this.requires(rumbleSubsystem);
        this.visionSubsystem = visionSubsystem;
        this.rumble = rumble;
        propFactory.setPrefix(this.getPrefix());
        rumbleIntensity = propFactory.createPersistentProperty("RumbleIntensity", .5);
        rumbleLength = propFactory.createPersistentProperty("RumbleLength", .1);
        timeBetweenRumble = propFactory.createPersistentProperty("TimeBetweenRumbles", 1);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        wasTargetInView = false;
        lastRumbleCall = null;
    }

    @Override
    public void execute() {
        boolean isTargetInView = visionSubsystem.isTargetInView();
        boolean isTargetNowInView = isTargetInView && !wasTargetInView;
        boolean hasEnoughTimePassed = lastRumbleCall == null || XTimer.getFPGATimestamp() - lastRumbleCall > timeBetweenRumble.get();
        
        if (isTargetInView && hasEnoughTimePassed) {
            rumble.rumbleDriverGamepad(rumbleIntensity.get(), rumbleLength.get());
            lastRumbleCall = XTimer.getFPGATimestamp();
        }
        wasTargetInView = isTargetInView;
    }
}
