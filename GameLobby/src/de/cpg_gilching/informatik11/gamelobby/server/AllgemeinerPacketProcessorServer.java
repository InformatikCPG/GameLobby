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
		if (spieler.getServer().getSpielerListe().contains(spieler)) {
			System.err.println("Spieler " + spieler.getName() + " hat versucht, 2x seinen Namen zu senden!");
			return;
		}
		
		if (spieler.getServer().getSpieler(packet.username) != null) {
			spieler.getServer().kickSpieler(spieler, "Name schon vergeben");
			return;
		}
		
		spieler.setName(packet.username);
		spieler.getServer().onSpielerBeigetreten(spieler);
	}
	
	public void handle(PacketDisconnect packet) {
		// das Paket wieder zurücksenden und die Verbindung schließen
		spieler.getServer().kickSpieler(spieler, "Client: " + packet.grund);
	}
	
}
