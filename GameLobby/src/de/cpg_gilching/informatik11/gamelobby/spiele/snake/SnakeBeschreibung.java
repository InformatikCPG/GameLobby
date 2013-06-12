package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class SnakeBeschreibung extends SpielBeschreibung {

	@Override
	public String getBezeichnung() {
		return "Multiplayer Snake";
	}
	
	@Override
	public int maximalspielerGeben() {
		return 10;
	}

	@Override
	public int tickrateGeben() {
		return 20;
	}

	@Override
	public ClientSpiel clientInstanzErstellen() {
		return new SnakeSpielClient();
	}

	@Override
	public ServerSpiel serverInstanzErstellen() {
		return new SnakeSpielServer();
	}
	
	@Override
	public void paketeAnmelden(PaketLexikon lexikon) {
		lexikon.anmelden(PacketFeldSetzen.class);
		lexikon.anmelden(PacketNachrichtSenden.class);
		lexikon.anmelden(PacketReset.class);
	}
}
