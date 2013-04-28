package de.cpg_gilching.informatik11.gamelobby.server;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.net.PacketProcessor;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;

public class AllgemeinerPacketProcessorServer extends PacketProcessor {
	
	private Spieler spieler;
	
	public AllgemeinerPacketProcessorServer(Spieler spieler) {
		this.spieler = spieler;
	}
	
	@Override
	public void onUnhandledPacket(Packet packet) {
		System.err.println("unerwartetes Packet: " + packet.getClass().getSimpleName());
	}
	
	@Override
	public void onException(Exception exception, Packet packet) {
		System.err.println("Fehler beim Verarbeiten von " + packet.getClass().getSimpleName());
		exception.printStackTrace();
	}
	
	public void handle(PacketHallo packet) {
		spieler.setName(packet.username);
	}
	
	public void handle(PacketDisconnect packet) {
		// das Paket wieder zurücksenden und die Verbindung schließen
		spieler.getServer().kickSpieler(spieler, "Client: " + packet.grund);
	}
	
}
