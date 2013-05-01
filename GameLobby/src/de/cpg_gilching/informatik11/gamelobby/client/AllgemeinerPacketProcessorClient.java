package de.cpg_gilching.informatik11.gamelobby.client;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.net.PacketProcessor;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketKeepAlive;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketServerSpielAnmelden;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionSpielerStatus;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionStarten;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionVerlassen;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielerListe;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class AllgemeinerPacketProcessorClient extends PacketProcessor {
	
	private ControllerClient client;
	
	public AllgemeinerPacketProcessorClient(ControllerClient client) {
		this.client = client;
	}
	
	@Override
	public void onUnhandledPacket(Packet packet) {
		System.err.println("unerwartetes Packet: " + packet.getClass().getSimpleName());
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
		client.getServerLobby().chatNachrichtAnzeigen(packet.nachricht);
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
		
		client.sessionErstellen(packet.sessionId, beschreibung, packet.eingeladeneSpieler);
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
	
}
