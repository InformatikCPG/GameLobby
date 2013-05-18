package de.cpg_gilching.informatik11.gamelobby.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.cpg_gilching.informatik11.gamelobby.shared.PaketListe;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Connection;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketServerSpielAnmelden;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielStarten;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielTeilnehmer;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielerListe;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpieleListe;

/**
 * Die Hauptklasse für die Lobby-Logik auf dem Server.
 * Bekommt die vereinfachten Methodenaufrufe von der technischen Klasse {@link ServerMain}, die sich um Verbindungen kümmert und dient als zentrales Verbindungsstück der einzelnen Funktionen.
 */
public class ControllerServer {
	
	private ServerMain server;
	private PaketLexikon paketLexikon;
	private List<Spieler> spielerListe = new ArrayList<Spieler>();
	
	private SpieleListe geladeneSpiele;
	private Map<Integer, Session> offeneSessions = new HashMap<Integer, Session>();
	private Map<Integer, ServerSpiel> offeneSpiele = new HashMap<Integer, ServerSpiel>();
	
	public ControllerServer(ServerMain server, PaketLexikon lexikon) {
		this.server = server;
		this.paketLexikon = lexikon;
		
		PaketListe.normalePaketeAnmelden(paketLexikon);
		
		geladeneSpiele = new SpieleListe(paketLexikon);
		geladeneSpiele.beschreibungenLaden();
	}
	
	public void onSpielerVerbinden(Connection verbindung) {
		// Spieler initialisieren
		Spieler neuerSpieler = new Spieler(verbindung, "Unbekannt", this);
		verbindung.setPacketProcessor(new AllgemeinerPacketProcessorServer(neuerSpieler));
		
		// PacketProcessor wartet jetzt auf PacketHallo, mit dem der Spieler richtig beitritt
	}
	
	public void onSpielerBeigetreten(Spieler neuerSpieler) {
		// Spielerlisten aktualisieren
		PacketSpielerListe neuerSpielerPacket = new PacketSpielerListe(neuerSpieler.getName(), true);
		for (Spieler anderer : spielerListe) {
			anderer.packetSenden(neuerSpielerPacket);
			neuerSpieler.packetSenden(new PacketSpielerListe(anderer.getName(), true));
		}
		
		// neuen Spieler Teil der Liste machen
		spielerListe.add(neuerSpieler);
		
		
		// geladene Spiele senden
		for (SpielBeschreibung spiel : geladeneSpiele) {
			neuerSpieler.packetSenden(new PacketServerSpielAnmelden(spiel.getSpielId(), spiel.getBezeichnung()));
		}
		
		// Chat-Nachricht verbreiten
		packetAnAlle(new PacketChatNachricht(-1, neuerSpieler.getName() + " ist der Lobby beigetreten."));
		
		// automatisch KI-Spieler beitreten lassen
		if (!neuerSpieler.getName().startsWith("AI-"))
			server.connectClient(server.createAISocket());
	}
	
	public void onSpielerVerlassen(Connection verbindung) {
		for (Spieler spieler : spielerListe) {
			if (spieler.getVerbindung() == verbindung) {
				System.out.println("Spieler " + spieler.getName() + " hat den Server verlassen.");
				spielerListe.remove(spieler);
				
				// aus möglichen Sessions werfen
				for (Session session : new ArrayList<Session>(offeneSessions.values())) {
					session.spielerVerlassen(spieler);
				}
				
				// verbleibende Spieler informieren
				packetAnAlle(new PacketSpielerListe(spieler.getName(), false));
				packetAnAlle(new PacketChatNachricht(-1, spieler.getName() + " hat die Lobby verlassen."));
				break;
			}
		}
	}
	
	public void onServerEnde() {
		
	}
	
	public void tick(int ms) {
		for (ServerSpiel spiel : offeneSpiele.values()) {
			spiel.tick();
		}
	}
	
	
	/**
	 * Sucht einen Spieler auf dem Server anhand seines Namens.
	 * 
	 * @param username Der exakte Name des Spielers
	 * @return den Spieler, oder null wenn nicht gefunden
	 */
	public Spieler getSpieler(String username) {
		// TODO performance verbessern (Suche nach Spieler nach Namen --> HashMap)
		for (Spieler s : spielerListe) {
			if (s.getName().equals(username))
				return s;
		}
		return null;
	}
	
	public void sessionStarten(SpielBeschreibung beschreibung, List<Spieler> teilnehmer) {
		Session neueSession = new Session(this, beschreibung, teilnehmer);
		offeneSessions.put(neueSession.getId(), neueSession);
	}
	
	public void sessionSchließen(Session session) {
		offeneSessions.remove(session.getId());
		session.schließen();
	}
	
	public Session getSessionNachId(int sessionId) {
		return offeneSessions.get(sessionId);
	}
	
	
	public void spielStarten(int id, SpielBeschreibung beschreibung, Set<Spieler> teilnehmer) {
		// das ServerSpiel erzeugen und starten
		ServerSpiel serverSpiel = beschreibung.serverInstanzErstellen();
		serverSpiel._init(id, teilnehmer);
		
		// in die Liste der offenen Spiele aufnehmen
		offeneSpiele.put(id, serverSpiel);
		
		// alle Teilnehmer über den Spielstart informieren
		for (Spieler spieler : teilnehmer) {
			spieler.packetSenden(new PacketSpielStarten(id, beschreibung.getSpielId()));
			
			// alle Spieler beitreten lassen
			for (Spieler anderer : teilnehmer) {
				spieler.packetSenden(new PacketSpielTeilnehmer(id, anderer.getName(), PacketSpielTeilnehmer.BEIGETRETEN));
			}
		}
	}
	
	public ServerSpiel getSpielNachId(int id) {
		return offeneSpiele.get(id);
	}
	
	public void kickSpieler(Spieler spieler, String grund) {
		server.kickClient(spieler.getVerbindung(), grund);
	}
	
	/**
	 * Sendet ein {@link Packet} an alle verbundenen Spieler.
	 */
	public void packetAnAlle(Packet packet) {
		server.broadcast(packet);
	}
	
	public List<Spieler> getAlleSpieler() {
		return spielerListe;
	}
	
	public SpieleListe getSpielBeschreibungen() {
		return geladeneSpiele;
	}
	
	public ServerMain getServer() {
		return server;
	}
	
	public PaketLexikon getPaketLexikon() {
		return paketLexikon;
	}
	
}
