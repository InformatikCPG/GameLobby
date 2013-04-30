package de.cpg_gilching.informatik11.gamelobby.client;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibungClient;

class SpielerZustand {
	String name;
	boolean ausgewählt = false;
	
	public SpielerZustand(String name) {
		this.name = name;
	}
}

class SpielEintrag {
	String bezeichner;
	int spielId;
	
	public SpielEintrag(SpielBeschreibungClient spiel) {
		this.bezeichner = spiel.getBezeichnung();
		this.spielId = spiel.getSpielId();
	}
	
	@Override
	public String toString() {
		return bezeichner;
	}
}

/**
 * Die Controller-Komponente der "Server-Lobby".
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
	
	public void chatNachrichtSenden(String nachricht) {
		client.getVerbindung().sendPacket(new PacketChatNachricht(nachricht));
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
	
	public void spieleAuswahlAktualisieren(Collection<SpielBeschreibungClient> spiele) {
		SpielEintrag[] einträge = new SpielEintrag[spiele.size()];
		
		int i = 0;
		for (SpielBeschreibungClient spiel : spiele) {
			einträge[i++] = new SpielEintrag(spiel);
		}
		
		oberflaeche.spieleDropdownFüllen(einträge);
	}
	
}
