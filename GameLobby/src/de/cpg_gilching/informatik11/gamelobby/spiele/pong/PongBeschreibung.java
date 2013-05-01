package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class PongBeschreibung extends SpielBeschreibung {
	@Override
	public int maximalspielerGeben() {
		return 2;
	}
	
	@Override
	public int minimalspielerGeben() {
		return 2;
	}
	
	@Override
	public String getBezeichnung() {
		return "Pong";
	}
	
	@Override
	public int tickrateGeben() {
		return 20;
	}
	
	@Override
	public ClientSpiel clientInstanzErstellen() {
		return null;
	}
	
	@Override
	public PongSpielServer serverInstanzErstellen() {
		return new PongSpielServer();
	}
}
