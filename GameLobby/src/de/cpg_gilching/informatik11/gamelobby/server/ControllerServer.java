package de.cpg_gilching.informatik11.gamelobby.server;

import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.AdapterPaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.PaketListe;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Connection;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketServerSpielAnmelden;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielerListe;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpieleListe;

/**
 * Die Hauptklasse für die Lobby-Logik auf dem Server.
 * Bekommt die vereinfachten Methodenaufrufe von der technischen Klasse {@link ServerMain}, die sich um Verbindungen kümmert.
 * 
 */
public class ControllerServer {
	
	private ServerMain server;
	private PaketLexikon paketLexikon;
	private List<Spieler> spielerListe = new ArrayList<Spieler>();
	
	private SpieleListe geladeneSpiele;
	
	public ControllerServer(ServerMain server) {
		this.server = server;
		
		AdapterPaketLexikon adapter = new AdapterPaketLexikon();
		PaketListe.normalePaketeAnmelden(adapter);
		
		paketLexikon = new PaketLexikon(adapter);
		
		geladeneSpiele = new SpieleListe();
		geladeneSpiele.serverSpieleLaden();
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
		paketAnAlle(new PacketChatNachricht(neuerSpieler.getName() + " ist der Lobby beigetreten."));
		
		// zum debuggen
		int zufallsAnzahl = Helfer.zufallsZahl(5, 10);
		for (int i = 0; i < zufallsAnzahl; i++) {
			server.broadcast(new PacketSpielerListe("bbb" + Helfer.zufallsZahl(10, 2000), true));
		}
	}
	
	public void onSpielerVerlassen(Connection verbindung) {
		for (Spieler spieler : spielerListe) {
			if (spieler.getVerbindung() == verbindung) {
				System.out.println("Spieler " + spieler.getName() + " hat den Server verlassen.");
				spielerListe.remove(spieler);
				
				// verbleibende Spieler informieren
				paketAnAlle(new PacketSpielerListe(spieler.getName(), false));
				paketAnAlle(new PacketChatNachricht(spieler.getName() + " hat die Lobby verlassen."));
				break;
			}
		}
	}
	
	public void onServerEnde() {
		
	}
	
	public void kickSpieler(Spieler spieler, String grund) {
		server.kickClient(spieler.getVerbindung(), grund);
	}
	
	public void tick(int ms) {
		
	}
	
	public List<Spieler> getSpielerListe() {
		return spielerListe;
	}
	
	/**
	 * Sendet ein {@link Packet} an alle verbundenen Spieler.
	 */
	public void paketAnAlle(Packet packet) {
		server.broadcast(packet);
	}
	
	/**
	 * Sucht einen Spieler auf dem Server anhand seines Namens.
	 * 
	 * @param username Der exakte Name des Spielers
	 * @return den Spieler, oder null wenn nicht gefunden
	 */
	public Spieler getSpieler(String username) {
		// TODO performance verbessern
		for (Spieler s : spielerListe) {
			if (s.getName().equals(username))
				return s;
		}
		return null;
	}
	
	public ServerMain getServer() {
		return server;
	}
	
	public PaketLexikon getPaketLexikon() {
		return paketLexikon;
	}
	
}
