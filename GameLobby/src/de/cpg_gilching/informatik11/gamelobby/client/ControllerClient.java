package de.cpg_gilching.informatik11.gamelobby.client;

import de.cpg_gilching.informatik11.gamelobby.shared.AdapterPaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.PaketListe;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Connection;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;

public class ControllerClient implements Runnable {
	
	private static final int CLIENT_TPS = 30;
	private static final int MS_PER_TICK = 1000 / CLIENT_TPS;
	
	private Connection verbindung;
	private PaketLexikon paketLexikon;
	private String username;
	
	private BildschirmServerLobby serverLobby = null;
	
	private boolean aktiv = true;
	
	public ControllerClient(Connection verbindung, String username, AdapterPaketLexikon lexikonAdapter) {
		this.verbindung = verbindung;
		this.paketLexikon = new PaketLexikon(lexikonAdapter);
		this.username = username;
		
		PaketListe.normalePaketeAnmelden(lexikonAdapter);
		
		verbindung.setPacketProcessor(new AllgemeinerPacketProcessorClient(this));
		
		verbindung.sendMagicNumber();
		verbindung.sendPacket(new PacketHallo(username));
		
		Thread threadHauptschleife = new Thread(this, "Hauptschleife");
		threadHauptschleife.start();
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
	
	private void initialisieren() {
		serverLobby = new BildschirmServerLobby(this);
	}
	
	private void tick(int ms) {
		// empfangene Pakete behandeln
		verbindung.processPackets();
		
		if (verbindung.isClosed()) {
			System.out.println("Geschlossene Verbindung entdeckt. Hält an ...");
			anhalten();
		}
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
	
	public Connection getVerbindung() {
		return verbindung;
	}
	
	public BildschirmServerLobby getServerLobby() {
		return serverLobby;
	}
	
	public PaketLexikon getPaketLexikon() {
		return paketLexikon;
	}
	
	public String getUsername() {
		return username;
	}
	
}
