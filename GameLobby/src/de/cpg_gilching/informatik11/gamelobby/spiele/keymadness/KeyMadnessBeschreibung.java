package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class KeyMadnessBeschreibung extends SpielBeschreibung {
	
	@Override
	public String getBezeichnung() {
		return "Key Madness";
	}
	
	@Override
	public int tickrateGeben() {
		return 30;
	}
	
	@Override
	public ClientSpiel clientInstanzErstellen() {
		return new KeyMadnessClient();
	}
	
	@Override
	public ServerSpiel serverInstanzErstellen() {
		return new KeyMadnessServer();
	}
	
	@Override
	public int minimalspielerGeben() {
		return 2;
	}
	
	@Override
	public int maximalspielerGeben() {
		return 4;
	}
	
	@Override
	public void paketeAnmelden(PaketLexikon lexikon) {
	}
	
}
