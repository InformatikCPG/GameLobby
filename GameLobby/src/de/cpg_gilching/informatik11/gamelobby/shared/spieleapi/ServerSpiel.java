package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.server.Spieler;

public abstract class ServerSpiel extends Spiel {
	
	private int spielId;
	protected List<Spieler> teilnehmer = null;
	
	public final void _init(int id, Collection<Spieler> teilnehmer) {
		this.spielId = id;
		this.teilnehmer = new ArrayList<Spieler>(teilnehmer);
		starten();
	}
	
	public final void packetAnAlle(SpielPacket packet) {
		// TOOD spielid aufm Server richtig zuweisen: direkte Packets an die spieler vom implementierenden spiel?!
		packet.spielId = spielId;
		for (Spieler spieler : teilnehmer)
			spieler.packetSenden(packet);
	}
	
	protected abstract void starten();
	
	public void tick() {
	}
	
}
