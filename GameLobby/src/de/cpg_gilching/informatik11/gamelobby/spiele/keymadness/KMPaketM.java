package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class KMPaketM implements PaketManager {
	private KeyMadnessServer ticTacToeServer;
	private Spieler spieler;

	public KMPaketM(Spieler spieler, KeyMadnessServer ticTacToeServer) {
		this.spieler = spieler;
		this.ticTacToeServer = ticTacToeServer;
	}

	public void verarbeiten(PacketFeldKlick packet){
		ticTacToeServer.feldSetzen(spieler, packet.feld);
	}

}
