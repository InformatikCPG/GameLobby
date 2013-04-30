package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.spiele.pong.PongBeschreibungClient;
import de.cpg_gilching.informatik11.gamelobby.spiele.pong.PongBeschreibungServer;

public class SpieleListe implements Iterable<SpielBeschreibung> {
	
	private List<SpielBeschreibung> spiele = new ArrayList<SpielBeschreibung>();
	
	public SpieleListe() {
	}
	
	public void serverSpieleLaden() {
		spielLaden(new PongBeschreibungServer());
	}
	
	public void clientSpieleLaden() {
		spielLaden(new PongBeschreibungClient());
	}
	
	private void spielLaden(SpielBeschreibung spiel) {
		spiel.setSpielId(spiele.size());
		spiele.add(spiel);
	}
	
	public SpielBeschreibung getSpielNachBezeichnung(String bezeichnung) {
		for (SpielBeschreibung spiel : spiele) {
			if (spiel.getBezeichnung().equals(bezeichnung)) {
				return spiel;
			}
		}
		
		return null;
	}
	
	@Override
	public Iterator<SpielBeschreibung> iterator() {
		return spiele.iterator();
	}
	
}
