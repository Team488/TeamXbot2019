package competition.subsystems.climber;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2019;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;

@Singleton
public class RearMotorClimberSubsystem extends BaseMotorClimberSubsystem {

    ElectricalContract2019 contract;

    @Inject
    public RearMotorClimberSubsystem(CommonLibFactory clf, PropertyFactory propFactory, ElectricalContract2019 contract) {
        super(clf, propFactory, 
        contract.getRearLeftClimber(), contract.getRearLeftEncoder(), contract.getRearLeftLimit(), 
        contract.getRearRightClimber(), contract.getRearRightEncoder(), contract.getRearRightLimit(),
        "Rear", "RearMotorClimberSubsystem", contract.isRearMotorClimberReady());
        this.contract = contract;
    }        
}