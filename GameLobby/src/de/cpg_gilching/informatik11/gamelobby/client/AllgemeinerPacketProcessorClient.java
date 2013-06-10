package de.cpg_gilching.informatik11.gamelobby.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.net.PacketProcessor;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.*;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class AllgemeinerPacketProcessorClient extends PacketProcessor {
	
	private ControllerClient client;
	
	public AllgemeinerPacketProcessorClient(ControllerClient client) {
		this.client = client;
	}
	
	@Override
	public void onUnhandledPacket(Packet packet) {
		// SpielPackets werden vom jeweiligen Spiel behandelt, also wird es hier weitergebeben
		if (packet instanceof SpielPacket) {
			int id = ((SpielPacket) packet).spielId;
			BildschirmGameLobby spielBildschirm = client.getSpielNachId(id);
			
			if (spielBildschirm == null) {
				System.err.println("Ungültige Spiel id: " + id);
				return;
			}
			
			spielPacketVerarbeiten(spielBildschirm.getSpiel(), (SpielPacket) packet);
			return;
		}
		
		System.err.println("unerwartetes Packet: " + packet.getClass().getSimpleName());
	}
	
	
	private void spielPacketVerarbeiten(ClientSpiel spiel, SpielPacket packet) {
		PaketManager manager = spiel.getPaketManager();
		if (manager == null) {
			System.err.println("Spiel kann SpielPacket nicht entgegennehmen: kein PaketManager aktiviert!");
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
		System.err.println("Fehler beim Verarbeiten von " + packet);
		exception.printStackTrace();
	}
	
	public void handle(PacketKeepAlive packet) {
	}
	
	public void handle(PacketDisconnect packet) {
		System.out.println("Disconnect von Server: " + packet.grund);
		client.getVerbindung().close();
	}
	
	public void handle(PacketSpielerListe packet) {
		if (packet.neu) {
			client.getServerLobby().spielerAnlegen(packet.username);
		}
		else {
			client.getServerLobby().spielerEntfernen(packet.username);
		}
	}
	
	public void handle(PacketChatNachricht packet) {
		if (packet.spielId > -1) {
			BildschirmGameLobby spielBildschirm = client.getSpielNachId(packet.spielId);
			if (spielBildschirm == null) {
				System.err.println("Ungültige Spiel id für Chat-Nachricht: " + packet.spielId);
				return;
			}
			
			spielBildschirm.chatNachrichtAnzeigen(packet.nachricht);
		}
		else {
			client.getServerLobby().chatNachrichtAnzeigen(packet.nachricht);
		}
	}
	
	public void handle(PacketServerSpielAnmelden packet) {
		client.beschreibungAnmelden(packet.spielId, packet.spielBezeichner);
	}
	
	public void handle(PacketSessionStarten packet) {
		SpielBeschreibung beschreibung = client.getBeschreibungNachId(packet.spielId);
		if (beschreibung == null) {
			System.err.println("Session kann nicht gestartet werden: Spielbeschreibung mit ungültiger id " + packet.spielId);
			return;
		}
		
		client.sessionErstellen(packet.sessionId, beschreibung, packet.eingeladeneSpieler, packet.punktelimit);
	}
	
	public void handle(PacketSessionVerlassen packet) {
		BildschirmSessionLobby sessionLobby = client.getSessionNachId(packet.sessionId);
		if (sessionLobby == null) {
			System.err.println("Ungültige Session id bei SessionVerlassen: " + packet.sessionId);
			return;
		}
		
		sessionLobby.jetztBeenden();
	}
	
	public void handle(PacketSessionSpielerStatus packet) {
		BildschirmSessionLobby sessionLobby = client.getSessionNachId(packet.sessionId);
		if (sessionLobby == null) {
			System.err.println("Ungültige Session id bei SpielerStatus: " + packet.sessionId);
			return;
		}
		
		if (packet.istBereit)
			sessionLobby.spielerBereitSetzen(packet.spielerName);
		else
			sessionLobby.spielerEntfernen(packet.spielerName);
	}
	
	public void handle(PacketSpielStarten packet) {
		SpielBeschreibung beschreibung = client.getBeschreibungNachId(packet.beschreibungId);
		if (beschreibung == null) {
			System.err.println("Spiel kann nicht gestartet werden: Spielbeschreibung mit ungültiger id " + packet.beschreibungId);
			return;
		}
		
		client.spielErstellen(packet.spielId, beschreibung, packet.punktelimit);
	}
	
	public void handle(PacketSpielVerlassen packet) {
		BildschirmGameLobby spiel = client.getSpielNachId(packet.spielId);
		if (spiel == null) {
			System.err.println("PacketSpielVerlassen: Spiel id ungültig: " + packet.spielId);
			return;
		}
		
		spiel.jetztBeenden();
		
		if (packet.grund != null) {
			Helfer.meldungAnzeigenAsynchron(packet.grund, false);
		}
	}
	
	public void handle(PacketSpielTeilnehmer packet) {
		BildschirmGameLobby spiel = client.getSpielNachId(packet.spielId);
		if (spiel == null) {
			System.err.println("PacketSpielTeilnehmer: Spiel id ungültig: " + packet.spielId);
			return;
		}
		
		switch (packet.aktion) {
		case PacketSpielTeilnehmer.BEIGETRETEN:
			spiel.spielerHinzufügen(packet.spielerName);
			break;
		
		case PacketSpielTeilnehmer.VERLASSEN:
			spiel.spielerEntfernen(packet.spielerName);
			break;
		
		default:
			System.err.println("PacketSpielTeilnehmer: ungültige Aktion " + packet.aktion);
			break;
		}
	}
	
	public void handle(PacketSpielTeilnehmerDaten packet) {
		BildschirmGameLobby spiel = client.getSpielNachId(packet.spielId);
		if (spiel == null) {
			System.err.println("PacketSpielPunkte: Spiel id ungültig: " + packet.spielId);
			return;
		}
		
		spiel.spielerPunkteSetzen(packet.spielerName, packet.farbe, packet.punkte, packet.tempPunkte);
	}

}
