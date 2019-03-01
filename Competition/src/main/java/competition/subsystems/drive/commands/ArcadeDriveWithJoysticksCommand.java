package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.properties.PropertyFactory;

public class ArcadeDriveWithJoysticksCommand extends HeadingControlledDriveCommand {

    final OperatorInterface oi;

    @Inject
    public ArcadeDriveWithJoysticksCommand(OperatorInterface oi, DriveSubsystem drive, 
            PropertyFactory propFactory, CommonLibFactory clf) {
        super(clf, drive, propFactory);        
        this.oi = oi;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        log.info("Initializing");
    }

    @Override
    protected double getHumanTranslationInput() {
        return oi.driverGamepad.getLeftVector().y;
    }

    @Override
    protected double getHumanRotationInput() {
        return MathUtils.squareAndRetainSign(oi.driverGamepad.getRightVector().x);
    }
}