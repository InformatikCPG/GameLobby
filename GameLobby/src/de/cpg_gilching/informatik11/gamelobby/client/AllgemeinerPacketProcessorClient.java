package de.cpg_gilching.informatik11.gamelobby.client;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.net.PacketProcessor;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;

public class AllgemeinerPacketProcessorClient extends PacketProcessor {
	
	private ControllerClient client;
	
	public AllgemeinerPacketProcessorClient(ControllerClient client) {
		this.client = client;
	}
	
	@Override
	public void onUnhandledPacket(Packet p) {
	}
	
	@Override
	public void onException(Exception exception, Packet packet) {
		System.err.println("Fehler beim Verarbeiten von " + packet);
		exception.printStackTrace();
	}
	
	public void handle(PacketDisconnect packet) {
		System.out.println("Disconnect von Server: " + packet.grund);
		client.getVerbindung().close();
	}
	
}
