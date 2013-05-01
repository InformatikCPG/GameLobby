package de.cpg_gilching.informatik11.gamelobby.server;

import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.net.PacketProcessor;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketKeepAlive;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionStarten;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

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
	
	public void handle(PacketKeepAlive packet) {
		spieler.packetSenden(new PacketKeepAlive());
	}
	
	public void handle(PacketHallo packet) {
		if (spieler.getServer().getAlleSpieler().contains(spieler)) {
			System.err.println("Spieler " + spieler.getName() + " hat versucht, mehrmals seinen Namen zu senden!");
			return;
		}
		
		if (spieler.getServer().getSpieler(packet.username) != null) {
			spieler.getServer().kickSpieler(spieler, "Name schon vergeben");
			return;
		}
		
		spieler.setName(packet.username);
		spieler.getServer().onSpielerBeigetreten(spieler);
	}
	
	public void handle(PacketChatNachricht packet) {
		// Paket wieder an alle zurücksenden
		spieler.getServer().paketAnAlle(new PacketChatNachricht("<" + spieler.getName() + "> " + packet.nachricht));
	}
	
	
	public void handle(PacketSessionStarten packet) {
		SpielBeschreibung beschreibung = spieler.getServer().getSpielBeschreibungen().getSpielNachId(packet.spielId);
		if (beschreibung == null) {
			System.err.println("Ungültige id beim Starten der Session: " + packet.spielId);
			return;
		}
		
		List<Spieler> spielerListe = new ArrayList<Spieler>(packet.eingeladeneSpieler.size() + 1);
		spielerListe.add(spieler);
		
		for (String spielerName : packet.eingeladeneSpieler) {
			Spieler eingeladener = spieler.getServer().getSpieler(spielerName);
			if (eingeladener == null) {
				System.err.println("Ungültiger Spielername beim Starten der Session: " + spielerName);
				return;
			}
			
			spielerListe.add(eingeladener);
		}
		
		spieler.getServer().sessionStarten(beschreibung, spielerListe);
	}
	
	
	public void handle(PacketDisconnect packet) {
		// das Paket wieder zurücksenden und die Verbindung schließen
		spieler.getServer().kickSpieler(spieler, "Client: " + packet.grund);
	}
	
}
