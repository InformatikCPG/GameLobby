package de.cpg_gilching.informatik11.gamelobby.client;

import java.util.Arrays;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class BildschirmSessionLobby {
	
	private int sessionId;
	private SpielBeschreibung beschreibung;
	private List<String> eingeladeneSpieler;
	
	private FensterSessionLobby oberfläche;
	
	public BildschirmSessionLobby(int sessionId, SpielBeschreibung beschreibung, List<String> eingeladeneSpieler) {
		this.sessionId = sessionId;
		this.beschreibung = beschreibung;
		this.eingeladeneSpieler = eingeladeneSpieler;
		
		this.oberfläche = new FensterSessionLobby(this);
		System.out.println("client mit den spielern: " + Arrays.toString(eingeladeneSpieler.toArray()));
	}
	
	public SpielBeschreibung getBeschreibung() {
		return beschreibung;
	}
	
}
