package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class OsmosBeschreibung extends SpielBeschreibung {
	
	@Override
	public String getBezeichnung() {
		return "Osmos";
	}
	
	@Override
	public int tickrateGeben() {
		return 30;
	}
	
	@Override
	public ClientSpiel clientInstanzErstellen() {
		return new OsmosClient();
	}
	
	@Override
	public ServerSpiel serverInstanzErstellen() {
		return new OsmosServer();
	}
	
	@Override
	public void paketeAnmelden(PaketLexikon lexikon) {
		lexikon.anmelden(PacketSetup.class);
		lexikon.anmelden(PacketNeueBlase.class);
		lexikon.anmelden(PacketBlaseBewegen.class);
	}
	
}
