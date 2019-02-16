package competition.subsystems.drive.commands;
import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class HumanAssistedPurePursuitCommand extends ConfigurablePurePursuitCommand {

    final OperatorInterface oi;

    @Inject
    public HumanAssistedPurePursuitCommand(CommonLibFactory clf, BasePoseSubsystem pose, BaseDriveSubsystem drive,
    XPropertyManager propMan, OperatorInterface oi) {
        super(clf, pose, drive, propMan);
        this.oi = oi;
    }

    @Override
    public void execute() {
        RabbitChaseInfo chaseData = evaluateCurrentPoint();
        drive.drive(new XYPair(0, oi.driverGamepad.getLeftVector().y*chaseData.translation), chaseData.rotation);
    }
}