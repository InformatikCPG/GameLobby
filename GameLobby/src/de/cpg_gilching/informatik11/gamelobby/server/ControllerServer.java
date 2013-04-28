package de.cpg_gilching.informatik11.gamelobby.server;

import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Connection;

/**
 * Die Hauptklasse für die Lobby-Logik auf dem Server.
 * Bekommt die vereinfachten Methodenaufrufe von der technischen Klasse {@link ServerMain}, die sich um Verbindungen kümmert.
 * 
 */
public class ControllerServer {
	
	private ServerMain server;
	private List<Spieler> spielerListe = new ArrayList<Spieler>();
	
	public ControllerServer(ServerMain server) {
		this.server = server;
	}
	
	public void onSpielerVerbinden(Connection verbindung) {
		Spieler neuerSpieler = new Spieler(verbindung, "Unbekannt", this);
		spielerListe.add(neuerSpieler);
		verbindung.setPacketProcessor(new AllgemeinerPacketProcessorServer(neuerSpieler));
	}
	
	public void onSpielerVerlassen(Connection verbindung) {
		for (Spieler spieler : spielerListe) {
			if (spieler.getVerbindung() == verbindung) {
				System.out.println("Spieler " + spieler.getName() + " hat den Server verlassen.");
				spielerListe.remove(spieler);
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
	
	public ServerMain getServer() {
		return server;
	}
	
}
