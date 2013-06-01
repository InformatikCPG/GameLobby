package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielTaste;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class KMPaketM implements PaketManager {
	private KeyMadnessServer server;
	private Spieler spieler;

	public KMPaketM(Spieler spieler, KeyMadnessServer server) {
		this.spieler = spieler;
		this.server = server;
	}

	public void verarbeiten(PacketSpielTaste packet){
		if(packet.zustand){
			server.prüfen(packet.tastencode, spieler);
		}
	}
	
}
