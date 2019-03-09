package competition;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.operator_interface.OperatorInterface;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.sensors.XJoystick;
import xbot.common.controls.sensors.XTimer;

@Singleton
public class RumbleManager implements PeriodicDataSource {
    private double lastDriverRequestEndTime = -1;
    private XJoystick driverGamepad;
    private boolean isRumbling;
    
    @Inject
    public RumbleManager(OperatorInterface oi) {
        this.driverGamepad = oi.driverGamepad;
    }
    
    public void stopDriverGamepadRumble() {
        writeRumble(driverGamepad, 0);
        lastDriverRequestEndTime = -1;
    }
    
    public void rumbleDriverGamepad(double intensity, double length) {
        writeRumble(driverGamepad, intensity);
        lastDriverRequestEndTime = XTimer.getFPGATimestamp() + length;
    }
    
    private void writeRumble(XJoystick gamepad, double intensity) {

        GenericHID internalJoystick = gamepad.getRawWPILibJoystick();
        isRumbling = true;
        if (internalJoystick == null) {
            return;
        }
        internalJoystick.setRumble(RumbleType.kLeftRumble, intensity);
        internalJoystick.setRumble(RumbleType.kRightRumble, intensity);
    }

    public boolean getIsRumbling() {
        return isRumbling;
    }

    @Override
    public void updatePeriodicData() {
        if (lastDriverRequestEndTime > 0 && XTimer.getFPGATimestamp() > lastDriverRequestEndTime) {
            writeRumble(driverGamepad, 0);
            lastDriverRequestEndTime = -1;
            isRumbling = false;
        }
    }

    @Override
    public String getName() {
        return "RumbleManager";
    }
}
