package competition.subsystems.vision;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.networking.OffboardCommunicationClient;
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class VisionSubsystem extends BaseSubsystem implements PeriodicDataSource {

    OffboardCommunicationClient client;
    final StringProperty packetProp;
    private String recentPacket;

    @Inject
    public VisionSubsystem(XPropertyManager propMan, OffboardCommunicationClient client) {
        this.client = client;
        packetProp = propMan.createEphemeralProperty(getPrefix() + "Packet", "");

        client.setNewPacketHandler(packet -> handlePacket(packet));
        client.start();
    }

    private void handlePacket(String packet) {
        recentPacket = packet;
    }

    @Override
    public void updatePeriodicData() {
        packetProp.set(recentPacket);
    }
}