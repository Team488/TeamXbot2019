package competition.subsystems.vision;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2019;

import com.fasterxml.jackson.jr.ob.JSON;

import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.sensors.XTimer;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.networking.OffboardCommunicationClient;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.properties.StringProperty;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.drive.RabbitPoint.PointTerminatingType;
import xbot.common.subsystems.drive.RabbitPoint.PointType;

@Singleton
public class VisionSubsystem extends BaseSubsystem implements PeriodicDataSource {

    OffboardCommunicationClient client;
    final StringProperty packetProp;
    private String recentPacket;
    VisionData visionData;
    double lastCalledTime;
    boolean beenTooLong;

    final DoubleProperty differenceBetweenTime;
    final DoubleProperty packetNumberProp;
    final BooleanProperty hasTargetProperty;
    final DoubleProperty yawToTargetProperty;
    final DoubleProperty targetRangeProperty;
    final DoubleProperty targetRotationProperty;
    final ElectricalContract2019 contract;

    @Inject
    public VisionSubsystem(PropertyFactory propMan, CommonLibFactory clf, ElectricalContract2019 contract) {
        this.contract = contract;
        this.client = clf.createZeromqListener("tcp://10.4.88.12:5801", "");
        propMan.setPrefix(this.getPrefix());
        differenceBetweenTime = propMan.createPersistentProperty("differenceBetweenTime", 1);
        recentPacket = "no packets yet";

        packetProp = propMan.createEphemeralProperty("Packet", recentPacket);
        packetNumberProp = propMan.createEphemeralProperty("NumPackets", 0);
        hasTargetProperty = propMan.createEphemeralProperty("hasTarget", false);
        yawToTargetProperty = propMan.createEphemeralProperty("yawToTarget", 0.0);
        targetRangeProperty = propMan.createEphemeralProperty("targetRange", 0.0);
        targetRotationProperty = propMan.createEphemeralProperty("targetRotation", 0.0);

        client.setNewPacketHandler(packet -> handlePacket(packet));
        client.start();
    }

    public void handlePacket(String packet) {
        recentPacket = packet;
        lastCalledTime = XTimer.getFPGATimestamp();
        packetNumberProp.set(packetNumberProp.get() + 1);
        try {
            visionData = JSON.std.beanFrom(VisionData.class, recentPacket);
            // validate the data
            boolean isDataValid = true;
            if (visionData.getYaw() != null) {
                double angle = visionData.getYaw().doubleValue();
                if (angle > 180.0 || angle < -180.0) {
                    isDataValid = false;
                }
            } else {
                isDataValid = false;
            }

            if (!isDataValid) {
                visionData = null;
            }
        } catch (IOException e) {
            visionData = null;
        }

        // report out values
        if (visionData != null) {
            yawToTargetProperty.set(visionData.getYaw() != null ? visionData.getYaw() : 0.0);
            hasTargetProperty.set(visionData.isHasTarget());
            targetRangeProperty.set(visionData.getRange() != null ? visionData.getRange() : 0.0);
            targetRotationProperty.set(visionData.getRotation() != null ? visionData.getRotation() : 0.0);
        } else {
            yawToTargetProperty.set(0.0);
            hasTargetProperty.set(false);
            targetRangeProperty.set(0.0);
            targetRotationProperty.set(0.0);
        }
    }

    public boolean isTargetInView() {
        beenTooLong = ((XTimer.getFPGATimestamp() - lastCalledTime) > differenceBetweenTime.get());
        if (beenTooLong || visionData == null) {
            return false;
        } else {
            return visionData.hasTarget;
        }
    }

    public double getAngleToTarget() {
        if (isTargetInView()) {
            double yaw = visionData.getYaw().doubleValue();
            if (contract.invertVisionData()) {
                yaw = -yaw;
            }
            return yaw;
        }
        return 0.0;
    }

    public List<RabbitPoint> getVisionTargetRelativePosition() {
        return createPathToVisionTarget(visionData.getRange(), visionData.getRotation()+90);
    }

    public List<RabbitPoint> getVisionTargetLine() {
        return createPathToVisionTarget(12*20, visionData.getYaw()+90);
    }

    public List<RabbitPoint> createPathToVisionTarget(double distance, double finalHeading) {
        var points = new ArrayList<RabbitPoint>();

        if (isTargetInView()) {
            double xOffset = Math.cos((visionData.getYaw() + 90) * Math.PI / 180) * distance;
            double yOffset = Math.sin((visionData.getYaw() + 90) * Math.PI / 180) * distance;

            RabbitPoint goalPoint = new RabbitPoint(xOffset, yOffset, finalHeading);
            goalPoint.pointType = PointType.PositionAndHeading;
            goalPoint.terminatingType = PointTerminatingType.Stop;

            points.add(goalPoint);
        }
        return points;
    }
    
    @Override
    public void updatePeriodicData() {
        if (recentPacket != null) {
            packetProp.set(recentPacket);
        }
    }
}