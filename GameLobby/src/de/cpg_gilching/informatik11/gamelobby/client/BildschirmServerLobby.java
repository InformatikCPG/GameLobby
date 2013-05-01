package de.cpg_gilching.informatik11.gamelobby.client;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibungClient;

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
	
	private FensterServerLobby oberfläche = new FensterServerLobby(this);
	private ControllerClient client;
	
	private Map<String, SpielerZustand> spielerTabelle = new TreeMap<String, SpielerZustand>();
	private int ausgewähltAnzahl = 0;
	
	public BildschirmServerLobby(ControllerClient clientController) {
		this.client = clientController;
		oberfläche.spielerListeAktualisieren(spielerTabelle.values());
	}
	
	public void chatNachrichtAnzeigen(String nachricht) {
		oberfläche.chatNachrichtAnzeigen(nachricht);
	}
	
	public void chatNachrichtSenden(String nachricht) {
		nachricht = nachricht.trim();
		if (!nachricht.isEmpty()) {
			client.getVerbindung().sendPacket(new PacketChatNachricht(nachricht));
		}
	}
	
	public void verlassen() {
		oberfläche.fensterSchliessen();
	}
	
	public void verbindungTrennen() {
		client.verbindungTrennen();
	}
	
	public void spielerAnlegen(String username) {
		spielerTabelle.put(username, new SpielerZustand(username));
		oberfläche.spielerListeAktualisieren(spielerTabelle.values());
	}
	
	public void spielerEntfernen(String username) {
		SpielerZustand entfernt = spielerTabelle.remove(username);
		if (entfernt != null && entfernt.istAusgewählt()) {
			spielerAuswahlUmschalten(entfernt);
		}
		
		oberfläche.spielerListeAktualisieren(spielerTabelle.values());
	}
	
	public void spielerAuswahlUmschalten(SpielerZustand spieler) {
		if (spieler.istAusgewählt()) {
			spieler.setAusgewählt(false);
			ausgewähltAnzahl--;
		}
		else {
			spieler.setAusgewählt(true);
			ausgewähltAnzahl++;
		}
		
		oberfläche.ausgewählteSpielerAnzeigen(ausgewähltAnzahl, 2, ausgewähltAnzahl <= 2);
	}
	
	public void spielAuswahlAktualisieren(Collection<SpielBeschreibungClient> spiele) {
		SpielEintrag[] einträge = new SpielEintrag[spiele.size()];
		
		int i = 0;
		for (SpielBeschreibungClient spiel : spiele) {
			einträge[i++] = new SpielEintrag(spiel);
		}
		
		oberfläche.spieleDropdownFüllen(einträge);
	}
	
	public ControllerClient getClient() {
		return client;
	}
	
}
