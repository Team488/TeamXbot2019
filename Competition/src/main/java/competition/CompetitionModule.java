package competition;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.RobotModule;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class CompetitionModule extends RobotModule {

    RobotPlatform platform;

    public enum RobotPlatform {
        Competition2018,
        Competition2019,
        Practice2019,
        Robox
    }
    
    public CompetitionModule(RobotPlatform platform) {
        this.platform = platform;
    }
    
    @Override
    protected void configure() {
        super.configure();
        this.bind(BasePoseSubsystem.class).to(PoseSubsystem.class);
        this.bind(BaseDriveSubsystem.class).to(DriveSubsystem.class);
        switch (platform) {
            case Competition2018:
                this.bind(ElectricalContract2019.class).to(Competition2018Contract.class);
                break;
            case Practice2019:
                this.bind(ElectricalContract2019.class).to(Practice2019Contract.class);
                break;
            case Competition2019:
                this.bind(ElectricalContract2019.class).to(Competition2019Contract.class);
                break;
            case Robox:
                this.bind(ElectricalContract2019.class).to(RoboxContract.class);
                break;  
            default:
                break;
        }
    }
}
