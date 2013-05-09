package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
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
		return 50;
	}
	
	@Override
	public void paketeAnmelden(PaketLexikon lexikon) {
		lexikon.anmelden(PacketSchlägerBewegen.class);
	}
	
	@Override
	public ClientSpiel clientInstanzErstellen() {
		return new PongSpielClient();
	}
	
	@Override
	public ServerSpiel serverInstanzErstellen() {
		return new PongSpielServer();
	}
}
