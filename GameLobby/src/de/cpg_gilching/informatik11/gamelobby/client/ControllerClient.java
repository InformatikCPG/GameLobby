package de.cpg_gilching.informatik11.gamelobby.client;

import java.util.HashMap;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.shared.AdapterPaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.PaketListe;
import de.cpg_gilching.informatik11.gamelobby.shared.TaskScheduler;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Connection;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketKeepAlive;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibungClient;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpieleListe;

public class ControllerClient implements Runnable {
	
	private static final int CLIENT_TPS = 30;
	private static final int MS_PER_TICK = 1000 / CLIENT_TPS;
	
	private Connection verbindung;
	private PaketLexikon paketLexikon;
	private String username;
	
	private TaskScheduler scheduler;
	
	private SpieleListe geladeneSpiele;
	private Map<Integer, SpielBeschreibungClient> angemeldeteSpiele = new HashMap<Integer, SpielBeschreibungClient>();
	
	private BildschirmServerLobby serverLobby = null;
	
	private int zeitSeitKeepAlive = 0;
	private boolean aktiv = true;
	
	public ControllerClient(Connection verbindung, String username, AdapterPaketLexikon lexikonAdapter) {
		this.verbindung = verbindung;
		this.paketLexikon = new PaketLexikon(lexikonAdapter);
		this.username = username;
		
		this.scheduler = new TaskScheduler();
		
		PaketListe.normalePaketeAnmelden(lexikonAdapter);
		
		verbindung.setPacketProcessor(new AllgemeinerPacketProcessorClient(this));
		
		verbindung.sendMagicNumber();
		verbindung.sendPacket(new PacketHallo(username));
		
		geladeneSpiele = new SpieleListe();
		geladeneSpiele.clientSpieleLaden();
		
		Thread threadHauptschleife = new Thread(this, "Hauptschleife");
		threadHauptschleife.start();
	}
	
	public void spielAnmelden(int spielId, String bezeichner) {
		SpielBeschreibung clientSpiel = geladeneSpiele.getSpielNachBezeichnung(bezeichner);
		
		if (clientSpiel == null) {
			System.err.println("Spiel mit Bezeichnung " + bezeichner + " nicht gefunden!");
		}
		else {
			clientSpiel.setSpielId(spielId);
			angemeldeteSpiele.put(spielId, (SpielBeschreibungClient) clientSpiel);
			serverLobby.spielAuswahlAktualisieren(angemeldeteSpiele.values());
		}
	}
	
	private void initialisieren() {
		serverLobby = new BildschirmServerLobby(this);
		new FensterGameLobby();
		new FensterSessionLobby();
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
	}
	
	@Override
	public void run() {
		// die Zeit, als der letzte tick stattfand
		long letzterTick = 0;
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
	}
	
	public Connection getVerbindung() {
		return verbindung;
	}
	
	public BildschirmServerLobby getServerLobby() {
		return serverLobby;
	}
	
	public PaketLexikon getPaketLexikon() {
		return paketLexikon;
	}
	
	public TaskScheduler getScheduler() {
		return scheduler;
	}
	
	public String getUsername() {
		return username;
	}
	
}
