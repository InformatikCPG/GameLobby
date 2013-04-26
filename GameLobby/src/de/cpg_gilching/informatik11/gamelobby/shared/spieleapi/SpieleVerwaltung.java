package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.ArrayList;
import java.util.List;

public class SpieleVerwaltung {
	
	private List<SpielBeschreibung> spiele;
	
	public SpieleVerwaltung() {
		spiele = new ArrayList<SpielBeschreibung>();
	}
	
	public void spielAnmelden(SpielBeschreibung spiel) {
		spiele.add(spiel);
	}
	
	public List<SpielBeschreibung> spieleGeben() {
		return spiele;
	}
	
}
