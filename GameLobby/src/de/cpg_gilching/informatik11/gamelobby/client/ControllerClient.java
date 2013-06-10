package de.cpg_gilching.informatik11.gamelobby.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.PaketListe;
import de.cpg_gilching.informatik11.gamelobby.shared.TaskScheduler;
import de.cpg_gilching.informatik11.gamelobby.shared.Wachhund;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Connection;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketKeepAlive;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpieleListe;

public class ControllerClient implements Runnable {
	
	private static final int CLIENT_TPS = 30;
	private static final int MS_PER_TICK = 1000 / CLIENT_TPS;
	
	private Connection verbindung;
	private PaketLexikon paketLexikon;
	private String username;
	
	private TaskScheduler scheduler;
	
	private SpieleListe beschreibungenListe;
	private Map<Integer, SpielBeschreibung> angemeldeteBeschreibungen = new HashMap<Integer, SpielBeschreibung>();
	
	private BildschirmServerLobby serverLobby = null;
	private Map<Integer, BildschirmSessionLobby> offeneSessions = new HashMap<Integer, BildschirmSessionLobby>();
	private Map<Integer, BildschirmGameLobby> offeneSpiele = new HashMap<Integer, BildschirmGameLobby>();
	
	private int zeitSeitKeepAlive = 0;
	private boolean aktiv = true;
	
	public ControllerClient(Connection verbindung, String username, PaketLexikon lexikon) throws IOException {
		this.verbindung = verbindung;
		this.paketLexikon = lexikon;
		this.username = username;
		
		this.scheduler = new TaskScheduler();
		
		PaketListe.normalePaketeAnmelden(paketLexikon);
		
		verbindung.setPacketProcessor(new AllgemeinerPacketProcessorClient(this));

		System.out.println("Versuche Validierung ...");
		if(!verbindung.sendMagicNumber()) {
			verbindung.close();
			throw new IOException("Fehler bei der Validierung des Servers!");
		}
		
		verbindung.startThreads();
		
		verbindung.sendPacket(new PacketHallo(username));
		
		beschreibungenListe = new SpieleListe(paketLexikon);
		beschreibungenListe.beschreibungenLaden();
		
		Thread threadHauptschleife = new Thread(this, "Hauptschleife");
		threadHauptschleife.start();
	}
	
	public void beschreibungAnmelden(int spielId, String bezeichner) {
		SpielBeschreibung clientSpiel = beschreibungenListe.getSpielNachBezeichnung(bezeichner);
		
		if (clientSpiel == null) {
			System.err.println("Spielbeschreibung mit Bezeichnung " + bezeichner + " nicht gefunden!");
		}
		else {
			// Die intern generierte id wird nicht gebraucht,
			// deswegen wird sie mit der des Servers überschrieben.
			clientSpiel.setSpielId(spielId);
			
			// In die angemeldete Liste speichern.
			angemeldeteBeschreibungen.put(spielId, clientSpiel);
			
			// Dropdown-Menü wird aktualisiert
			serverLobby.spielAuswahlAktualisieren(angemeldeteBeschreibungen.values());
		}
	}
	
	public SpielBeschreibung getBeschreibungNachId(int spielId) {
		return angemeldeteBeschreibungen.get(spielId);
	}
	
	public void sessionErstellen(int sessionId, SpielBeschreibung beschreibung, List<String> eingeladeneSpieler, int punktelimit) {
		offeneSessions.put(sessionId, new BildschirmSessionLobby(this, sessionId, beschreibung, eingeladeneSpieler, punktelimit));
	}
	
	public void sessionLöschen(BildschirmSessionLobby sessionLobby) {
		offeneSessions.remove(sessionLobby.getSessionId());
	}
	
	public BildschirmSessionLobby getSessionNachId(int sessionId) {
		return offeneSessions.get(sessionId);
	}
	
	public void spielErstellen(int spielId, SpielBeschreibung beschreibung, int punktelimit) {
		offeneSpiele.put(spielId, new BildschirmGameLobby(this, spielId, beschreibung, punktelimit));
	}
	
	public void spielLöschen(BildschirmGameLobby spielLobby) {
		offeneSpiele.remove(spielLobby.getSpielId());
	}
	
	public BildschirmGameLobby getSpielNachId(int spielId) {
		return offeneSpiele.get(spielId);
	}
	
	private void initialisieren() {
		serverLobby = new BildschirmServerLobby(this);
	}
	
	private void tick(int ms) {
		// empfangene Pakete behandeln
		verbindung.processPackets();
		
		// Verbindung am Leben erhalten --> alle 5 Sekunden KeepAlive senden
		zeitSeitKeepAlive += ms;
		if (zeitSeitKeepAlive >= 5000) {
			verbindung.sendPacket(new PacketKeepAlive());
			zeitSeitKeepAlive = 0;
		}
		
		if (verbindung.isClosed()) {
			System.out.println("Geschlossene Verbindung entdeckt. Hält an ...");
			anhalten();
			return;
		}
		
		scheduler.tasksAbarbeiten();
		
		for (BildschirmGameLobby gameLobby : offeneSpiele.values()) {
			gameLobby.tick(ms);
		}
	}
	
	@Override
	public void run() {
		// die Zeit, als der letzte tick stattfand
		long letzterTick = System.currentTimeMillis();
		// die jetzige Zeit
		long jetzt;
		
		initialisieren();
		
		// Schleife, so lange die Anwendung läuft
		while (istAktiv()) {
			// die aktuelle Zeit aktualisieren
			jetzt = System.currentTimeMillis();
			
			// wenn tick nötig --> ausführen
			if (jetzt - letzterTick > MS_PER_TICK) {
				tick((int) (jetzt - letzterTick));
				letzterTick = jetzt;
			}
			
			// den Prozessor etwas entlasten
			Helfer.warten(1L);
		}
		
		// ab hier wird nach sauberem Ende des Programms ausgeführt
		
		// Verbindung schließen, falls noch nicht geschehen
		verbindung.close();
		
		// Session-Lobbies beenden
		for (BildschirmSessionLobby session : new ArrayList<BildschirmSessionLobby>(offeneSessions.values())) {
			session.jetztBeenden();
		}
		
		// Ingame-Lobbies beenden
		for (BildschirmGameLobby spiel : new ArrayList<BildschirmGameLobby>(offeneSpiele.values())) {
			spiel.jetztBeenden();
		}
		
		serverLobby.verlassen();
	}
	
	/**
	 * Stoppt die Hauptschleife und beendet damit das Programm. Thread-sicher.
	 */
	public synchronized void anhalten() {
		aktiv = false;
	}
	
	public synchronized boolean istAktiv() {
		return aktiv;
	}
	
	/**
	 * Führt ein sauberes Abbauen der Verbindung herbei. Thread-sicher.
	 */
	public void verbindungTrennen() {
		verbindung.sendPacket(new PacketDisconnect("Server verlassen"));
		new Wachhund().start();
	}
	
	/**
	 * Sendet eine Chat-Nachricht, die entweder zur Server-Lobby oder zu einem Spiel gehört.
	 * 
	 * @param spielId die ID des Spiels, in dem gesendet wurde, oder -1, wenn die Nachricht zur Server-Lobby gehört
	 * @param nachricht die Nachricht, die gesendet werden soll; leere Nachrichten werden automatisch übersprungen
	 */
	public void chatNachrichtSenden(int spielId, String nachricht) {
		nachricht = nachricht.trim();
		if (!nachricht.isEmpty()) {
			verbindung.sendPacket(new PacketChatNachricht(spielId, nachricht));
		}
	}
	
	public Connection getVerbindung() {
		return verbindung;
	}
	
	public BildschirmServerLobby getServerLobby() {
		return serverLobby;
	}
	
	public TaskScheduler getScheduler() {
		return scheduler;
	}
	
	public String getUsername() {
		return username;
	}
	
}
