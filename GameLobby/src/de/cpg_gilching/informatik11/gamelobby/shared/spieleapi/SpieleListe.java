package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.spiele.osmos.OsmosBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.spiele.pong.PongBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.spiele.snake.SnakeBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.spiele.tictactoe.TicTacToeBeschreibung;

public class SpieleListe implements Iterable<SpielBeschreibung> {
	
	private List<SpielBeschreibung> spiele = new ArrayList<SpielBeschreibung>();
	private PaketLexikon lexikon;
	
	public SpieleListe(PaketLexikon paketLexikon) {
		this.lexikon = paketLexikon;
	}
	
	public void beschreibungenLaden() {
		spielLaden(new PongBeschreibung());
		spielLaden(new SnakeBeschreibung());
		spielLaden(new OsmosBeschreibung());
		spielLaden(new TicTacToeBeschreibung());
	}
	
	private void spielLaden(SpielBeschreibung spiel) {
		spiel.setSpielId(spiele.size()); // TODO logik fixen, das hier bringt nicht viel wenns veränderbar ist
		spiel.paketeAnmelden(lexikon);
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
	
	public SpielBeschreibung getSpielNachId(int id) {
		for (SpielBeschreibung spiel : spiele) {
			if (spiel.getSpielId() == id) {
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
