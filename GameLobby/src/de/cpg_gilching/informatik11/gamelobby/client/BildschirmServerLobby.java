package de.cpg_gilching.informatik11.gamelobby.client;

import java.util.Map;
import java.util.TreeMap;

class SpielerZustand {
	String name;
	boolean ausgewählt = false;
	
	public SpielerZustand(String name) {
		this.name = name;
	}
}

/**
 * Die View-Komponente der "Server-Lobby".
 */
public class BildschirmServerLobby {
	
	private FensterServerLobby oberflaeche = new FensterServerLobby(this);
	private ControllerClient client;
	
	private Map<String, SpielerZustand> spielerTabelle = new TreeMap<String, SpielerZustand>();
	
	public BildschirmServerLobby(ControllerClient clientController) {
		this.client = clientController;
		oberflaeche.spielerListeAktualisieren(spielerTabelle.values());
	}
	
	public void chatNachrichtAnzeigen(String nachricht) {
		oberflaeche.chatNachrichtAnzeigen(nachricht);
	}
	
	public void verlassen() {
		oberflaeche.fensterSchliessen();
	}
	
	public void verbindungTrennen() {
		client.verbindungTrennen();
	}
	
	public void spielerAnlegen(String username) {
		spielerTabelle.put(username, new SpielerZustand(username));
		oberflaeche.spielerListeAktualisieren(spielerTabelle.values());
	}
	
	public void spielerEntfernen(String username) {
		spielerTabelle.remove(username);
		oberflaeche.spielerListeAktualisieren(spielerTabelle.values());
	}
	
}
