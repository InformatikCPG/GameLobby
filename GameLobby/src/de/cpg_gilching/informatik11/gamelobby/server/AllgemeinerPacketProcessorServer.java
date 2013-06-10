package de.cpg_gilching.informatik11.gamelobby.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.net.PacketProcessor;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketKeepAlive;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionAnnehmen;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionStarten;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionVerlassen;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielVerlassen;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class AllgemeinerPacketProcessorServer extends PacketProcessor {
	
	private LobbySpieler spieler;
	
	public AllgemeinerPacketProcessorServer(LobbySpieler spieler) {
		this.spieler = spieler;
	}
	
	@Override
	public void onUnhandledPacket(Packet packet) {
		if (packet instanceof SpielPacket) {
			int id = ((SpielPacket) packet).spielId;
			ServerSpiel spiel = spieler.getServer().getSpielNachId(id);
			
			if (spiel == null) {
				System.err.println("Ungültige Spiel id: " + id);
				return;
			}
			
			spielPacketVerarbeiten(spiel, (SpielPacket) packet);
			return;
		}
		
		System.err.println("unerwartetes Packet: " + packet.getClass().getSimpleName());
	}
	
	private void spielPacketVerarbeiten(ServerSpiel spiel, SpielPacket packet) {
		PaketManager manager = spiel.getPaketManagerFür(spieler);
		if (manager == null) {
			System.err.println("Spiel kann SpielPacket nicht entgegennehmen: kein PaketManager für Spieler aktiviert!");
		}
		else {
			try {
				Method m = manager.getClass().getMethod("verarbeiten", packet.getClass());
				m.invoke(manager, packet);
			} catch (NoSuchMethodException e) {
				System.err.println("Spiel kann SpielPacket nicht entgegennehmen: " + packet.getClass().getSimpleName());
			} catch (InvocationTargetException e) {
				System.err.println("Exception beim Verarbeiten des SpielPackets: " + packet.getClass().getSimpleName());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
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
		
		String username = packet.username.trim();
		
		if (username.length() > 20) {
			spieler.getServer().kickSpieler(spieler, "Name zu lang");
			return;
		}
		
		if (spieler.getServer().getSpieler(username) != null) {
			spieler.getServer().kickSpieler(spieler, "Name schon vergeben");
			return;
		}
		
		spieler.setName(username);
		spieler.getServer().onSpielerBeigetreten(spieler);
	}
	
	public void handle(PacketChatNachricht packet) {
		String antwortNachricht = "<" + spieler.getName() + "> " + packet.nachricht;
		
		if (packet.spielId > -1) {
			ServerSpiel spiel = spieler.getServer().getSpielNachId(packet.spielId);
			if (spiel == null) {
				System.err.println("Ungültige Spiel id für Chat-Nachricht: " + packet.spielId);
				return;
			}
			
			spiel.getChat().spielerChatAusführen(spieler, packet.nachricht);
		}
		else {
			// auf command prüfen
			if (packet.nachricht.equals("!bot")) {
				ServerMain internerServer = spieler.getServer().getServer();
				internerServer.connectClient(internerServer.createAISocket());
				return;
			}
			if (packet.nachricht.startsWith("!kick") && packet.nachricht.length() > "!kick ".length()) {
				String pname = packet.nachricht.substring("!kick ".length());
				LobbySpieler kickender = spieler.getServer().getSpielerUngefähr(pname);
				if (kickender == null)
					spieler.packetSenden(new PacketChatNachricht(-1, "Spieler ungültig!"));
				else
					spieler.getServer().kickSpieler(kickender, "Per command gekickt!");
				return;
			}
			if (packet.nachricht.equals("!stopserver")) {
				antwortNachricht = "# Server wird heruntergefahren ...";
				spieler.getServer().getServer().stop();
			}
			
			
			// Paket wieder an alle zurücksenden
			spieler.getServer().packetAnAlle(new PacketChatNachricht(-1, antwortNachricht));
		}
	}
	
	public void handle(PacketSessionStarten packet) {
		SpielBeschreibung beschreibung = spieler.getServer().getSpielBeschreibungen().getSpielNachId(packet.spielId);
		if (beschreibung == null) {
			System.err.println("Ungültige id beim Starten der Session: " + packet.spielId);
			return;
		}
		
		if (packet.punktelimit < 1) {
			System.err.println("Ungültiges Punktelimit beim Starten der Session: " + packet.punktelimit);
			return;
		}

		List<LobbySpieler> spielerListe = new ArrayList<LobbySpieler>(packet.eingeladeneSpieler.size() + 1);
		spielerListe.add(spieler);
		
		for (String spielerName : packet.eingeladeneSpieler) {
			LobbySpieler eingeladener = spieler.getServer().getSpieler(spielerName);
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
		
		spieler.getServer().sessionStarten(beschreibung, spielerListe, packet.punktelimit);
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
	
	public void handle(PacketSpielVerlassen packet) {
		ServerSpiel spiel = spieler.getServer().getSpielNachId(packet.spielId);
		if (spiel == null) {
			System.err.println("Ungültige Spiel id beim Verlassen: " + packet.spielId);
			return;
		}
		
		spiel._teilnehmerVerlassen(spieler);
	}
	
	public void handle(PacketDisconnect packet) {
		// das Packet wieder zurücksenden und die Verbindung schließen
		spieler.getServer().kickSpieler(spieler, "Client: " + packet.grund);
	}
	
}
