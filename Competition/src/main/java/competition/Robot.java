
package competition;

import java.io.File;

import competition.CompetitionModule.RobotPlatform;
import competition.operator_interface.OperatorCommandMap;
import competition.subsystems.SubsystemDefaultCommandMap;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseRobot;

public class Robot extends BaseRobot {

    @Override
    protected void initializeSystems() {
        super.initializeSystems();
        this.injector.getInstance(SubsystemDefaultCommandMap.class);
        this.injector.getInstance(OperatorCommandMap.class);

        registerPeriodicDataSource(this.injector.getInstance(PoseSubsystem.class));
        registerPeriodicDataSource(this.injector.getInstance(VisionSubsystem.class));
    }

    private boolean isPracticeRobot() {
        File practiceRobotFlag = new File("/home/lvuser/practicerobot.txt");
        return practiceRobotFlag.exists();
    }

    private boolean is2018Robot() {
        File practiceRobotFlag = new File("/home/lvuser/2018robot.txt");
        return practiceRobotFlag.exists();
    }

    private RobotPlatform getRobotPlatform() {
        if (is2018Robot()) {
            return RobotPlatform.Competition2018;
        }
        if (isPracticeRobot()) {
            return RobotPlatform.Practice2019;
        }
        return RobotPlatform.Competition2019;
    }

    @Override
    protected void setupInjectionModule() {
        this.injectionModule = new CompetitionModule(getRobotPlatform());
    }
}
