package de.cpg_gilching.informatik11.gamelobby.spiele.tictactoe;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class TTTPaketM implements PaketManager {
	private TicTacToeServer ticTacToeServer;
	private Spieler spieler;

	public TTTPaketM(Spieler spieler, TicTacToeServer ticTacToeServer) {
		this.spieler = spieler;
		this.ticTacToeServer = ticTacToeServer;
	}

	public void verarbeiten(PacketFeldKlick packet){
		ticTacToeServer.feldSetzen(spieler, packet.feld);
	}

}
