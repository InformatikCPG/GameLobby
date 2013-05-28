package de.cpg_gilching.informatik11.gamelobby.spiele.tictactoe;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class TicTacToeBeschreibung extends SpielBeschreibung {
	
	@Override
	public String getBezeichnung() {
		return "Tic Tac Toe";
	}
	
	@Override
	public int tickrateGeben() {
		return 4;
	}
	
	@Override
	public ClientSpiel clientInstanzErstellen() {
		return new TicTacToeClient();
	}
	
	@Override
	public ServerSpiel serverInstanzErstellen() {
		return new TicTacToeServer();
	}
	
	@Override
	public int minimalspielerGeben() {
		return 2;
	}
	
	@Override
	public int maximalspielerGeben() {
		return 2;
	}
	
	@Override
	public void paketeAnmelden(PaketLexikon lexikon) {
		lexikon.anmelden(PacketFeldKlick.class);
		lexikon.anmelden(PacketFeldSetzen.class);
	}
	
}
