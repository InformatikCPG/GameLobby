package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibungServer;

public class PongBeschreibungServer extends SpielBeschreibungServer {
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
	public PongSpielServer instanzErstellen() {
		return new PongSpielServer();
	}
}
