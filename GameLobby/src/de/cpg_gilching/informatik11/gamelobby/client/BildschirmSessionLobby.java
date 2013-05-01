package de.cpg_gilching.informatik11.gamelobby.client;

import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class BildschirmSessionLobby {
	
	private int sessionId;
	private SpielBeschreibung beschreibung;
	private List<String> spieler;
	
	private FensterSessionLobby oberfläche;
	
	public BildschirmSessionLobby(int sessionId, SpielBeschreibung beschreibung, List<String> eingeladeneSpieler) {
		this.sessionId = sessionId;
		this.beschreibung = beschreibung;
		this.spieler = new ArrayList<String>(eingeladeneSpieler);
		
		this.oberfläche = new FensterSessionLobby(this);
		
		oberfläche.spielerListeAktualisieren(spieler);
	}
	
	public SpielBeschreibung getBeschreibung() {
		return beschreibung;
	}
	
}
