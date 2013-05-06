package de.cpg_gilching.informatik11.gamelobby.server;

import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.server.fakeplayer.ArtificialSocket;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.net.PacketProcessor;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketKeepAlive;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionAnnehmen;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionStarten;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionVerlassen;
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
		// auf command prüfen
		if (packet.nachricht.equals("!stopserver")) {
			spieler.getServer().paketAnAlle(new PacketChatNachricht("# Server wird heruntergefahren ..."));
			spieler.getServer().getServer().stop();
			return;
		}
		if (packet.nachricht.equals("!bot")) {
			spieler.getServer().getServer().connectClient(new ArtificialSocket(spieler.getServer().getPaketLexikon().getInternesLexikon()));
			return;
		}
		if (packet.nachricht.startsWith("!kick")) {
			String pname = packet.nachricht.substring("!kick ".length());
			Spieler kickender = spieler.getServer().getSpieler(pname);
			if (kickender == null)
				spieler.packetSenden(new PacketChatNachricht("Spieler ungültig!"));
			else
				spieler.getServer().kickSpieler(kickender, "Per command gekickt!");
			return;
		}
		
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
		
		if (spielerListe.size() < beschreibung.minimalspielerGeben() || (beschreibung.maximalspielerGeben() > -1 && spielerListe.size() > beschreibung.maximalspielerGeben())) {
			System.err.println("Ungültiger Anzahl an Spielern beim Starten der Session: " + spielerListe.size());
			return;
		}
		
		spieler.getServer().sessionStarten(beschreibung, spielerListe);
	}
	
	public void handle(PacketSessionAnnehmen packet) {
		Session session = spieler.getServer().getSessionNachId(packet.sessionId);
		if (session == null) {
			System.err.println("Session kann nicht angenommen werden: ungültige id " + packet.sessionId);
			return;
		}
		
		session.spielerBereit(spieler);
	}
	
	public void handle(PacketSessionVerlassen packet) {
		Session session = spieler.getServer().getSessionNachId(packet.sessionId);
		if (session == null) {
			System.err.println("Session kann nicht verlassen werden: ungültige id " + packet.sessionId);
			return;
		}
		
		session.spielerVerlassen(spieler);
	}
	
	
	public void handle(PacketDisconnect packet) {
		// das Paket wieder zurücksenden und die Verbindung schließen
		spieler.getServer().kickSpieler(spieler, "Client: " + packet.grund);
	}
	
}
