package competition.subsystems.vision;

import java.io.IOException;

import com.fasterxml.jackson.jr.ob.JSON;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.sensors.XTimer;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.networking.OffboardCommunicationClient;
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.properties.DoubleProperty;

@Singleton
public class VisionSubsystem extends BaseSubsystem implements PeriodicDataSource {

    OffboardCommunicationClient client;
    final StringProperty packetProp;
    private String recentPacket;
    double lastCalledTime;
    double angleToTarget;
    double parsedAngle;
    boolean isCalled;
    boolean numberIsTooBig;
    boolean cannotParseNumber;
    boolean beenTooLong;
    final DoubleProperty differenceBetweenTime;

    @Inject
    public VisionSubsystem(XPropertyManager propMan, CommonLibFactory clf) {
        this.client = clf.createZeromqListener("tcp://10.4.88.12:5801", "");
        differenceBetweenTime = propMan.createPersistentProperty(getPrefix() + "differenceBetweenTime", 1);
        recentPacket = "no packets yet";
        packetProp = propMan.createEphemeralProperty(getPrefix() + "Packet", recentPacket);        

        client.setNewPacketHandler(packet -> handlePacket(packet));
        client.start();
    }

    public void handlePacket(String packet) {
        recentPacket = packet;
        lastCalledTime = XTimer.getFPGATimestamp();
        VisionData newData;
        try {
            newData = JSON.std.beanFrom(VisionData.class, recentPacket);
            parsedAngle = newData.getTargetYaw().intValue();
            cannotParseNumber = false;
        } catch (IOException e) {
            cannotParseNumber = true;
            parsedAngle = 0.0;
        }

        if (parsedAngle > 180.0 || parsedAngle < -180.0) {
            numberIsTooBig = true;
            parsedAngle = 0.0;
        } else {
            numberIsTooBig = false;
        }
    }

    public boolean isTargetInView() {
        beenTooLong = ((XTimer.getFPGATimestamp() - lastCalledTime) > differenceBetweenTime.get());
        if (beenTooLong || numberIsTooBig || cannotParseNumber) {
            return false;
        }
        return true;
    }

    public double getAngleToTarget() {
        return parsedAngle;
    }

    @Override
    public void updatePeriodicData() {
        if (recentPacket != null) {
            packetProp.set(recentPacket);
        }
    }
}