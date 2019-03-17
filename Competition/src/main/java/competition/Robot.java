
package competition;

import java.io.File;

import competition.CompetitionModule.RobotPlatform;
import competition.operator_interface.OperatorCommandMap;
import competition.subsystems.SubsystemDefaultCommandMap;
import competition.subsystems.climber.FrontMotorClimberSubsystem;
import competition.subsystems.climber.RearMotorClimberSubsystem;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseRobot;

public class Robot extends BaseRobot {
    protected PoseSubsystem pose;

    @Override
    protected void initializeSystems() {
        super.initializeSystems();
        this.injector.getInstance(SubsystemDefaultCommandMap.class);
        this.injector.getInstance(OperatorCommandMap.class);

        this.pose = this.injector.getInstance(PoseSubsystem.class);
        registerPeriodicDataSource(pose);
        registerPeriodicDataSource(this.injector.getInstance(VisionSubsystem.class));
        registerPeriodicDataSource(this.injector.getInstance(RumbleManager.class));
        registerPeriodicDataSource(this.injector.getInstance(DriveSubsystem.class));
        registerPeriodicDataSource(this.injector.getInstance(ElevatorSubsystem.class));
        registerPeriodicDataSource(this.injector.getInstance(FrontMotorClimberSubsystem.class));
        registerPeriodicDataSource(this.injector.getInstance(RearMotorClimberSubsystem.class));
    }

    private boolean isPracticeRobot() {
        File robotFlag = new File("/home/lvuser/practicerobot.txt");
        return robotFlag.exists();
    }

    private boolean is2018Robot() {
        File robotFlag = new File("/home/lvuser/2018robot.txt");
        return robotFlag.exists();
    }

    private boolean isRobox() {
        File robotFlag = new File("/home/lvuser/robox.txt");
        return robotFlag.exists();
    }

    private RobotPlatform getRobotPlatform() {
        if (is2018Robot()) {
            System.out.println("Robot is 2018 robot.");
            return RobotPlatform.Competition2018;
        }
        if (isPracticeRobot()) {
            System.out.println("Robot is 2019 Practice Robot.");
            return RobotPlatform.Practice2019;
        }
        if (isRobox()) {
            return RobotPlatform.Robox;
        }
        System.out.println("Robot is 2019 COMPETITION ROBOT.");
        return RobotPlatform.Competition2019;
    }

    @Override
    protected void setupInjectionModule() {
        this.injectionModule = new CompetitionModule(getRobotPlatform());
    }

    @Override
    public void autonomousInit() {        
        super.autonomousInit();
        pose.setCurrentHeading(90);
    }
}
