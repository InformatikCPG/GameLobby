package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielMaus;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class SpielerController implements PaketManager {
	
	private final OsmosServer server;
	private final Spieler spieler;
	private Blase blase;
	
	private boolean taste = false;
	
	public SpielerController(OsmosServer server, Spieler spieler) {
		this.server = server;
		this.spieler = spieler;
	}
	
	public void tick() {
		if (taste)
			blase.getPosition().add(-3, 1);
	}
	
	public void verarbeiten(PacketSpielMaus packet) {
		taste = packet.zustand;
	}
	
	public void setBlase(Blase blase) {
		this.blase = blase;
	}
}
