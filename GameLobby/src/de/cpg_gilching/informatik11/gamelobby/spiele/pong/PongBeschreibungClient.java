package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibungClient;

public class PongBeschreibungClient extends SpielBeschreibungClient {
	@Override
	public String getBezeichnung() {
		return "Pong";
	}
	
	@Override
	public int tickrateGeben() {
		return 20;
	}
	
	@Override
	public ClientSpiel instanzErstellen() {
		// TODO Auto-generated method stub
		return null;
	}
}
