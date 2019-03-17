package competition.subsystems.climber;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2019;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;

@Singleton
public class FrontMotorClimberSubsystem extends BaseMotorClimberSubsystem {

    @Inject
    public FrontMotorClimberSubsystem(CommonLibFactory clf, PropertyFactory propFactory, ElectricalContract2019 contract) {
        super(clf, propFactory, 
        contract.getFrontLeftClimber(), contract.getFrontLeftEncoder(), contract.getFrontLeftLimit(), 
        contract.getFrontRightClimber(), contract.getFrontRightEncoder(), contract.getFrontRightLimit(),
        "Front", "FrontMotorClimberSubsystem", contract.isFrontMotorClimberReady());
    }
        
}