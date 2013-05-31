package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielMaus;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class SpielerController implements PaketManager {
	
	private final OsmosServer server;
	private final Spieler spieler;
	private Blase blase;
	
	private Vektor mausPosition = new Vektor();
	private boolean gedrückt = false;
	private int beschleunigtTicks = 0;
	
	public SpielerController(OsmosServer server, Spieler spieler) {
		this.server = server;
		this.spieler = spieler;
	}
	
	public void tick() {
		//		System.out.println(gedrückt);
		if (gedrückt) {
			beschleunigtTicks++;
			double faktor = Math.max(0.1, 2.0 - 0.1 * beschleunigtTicks);

			Vektor richtung = new Vektor();
			richtung.add(blase.getPosition()).sub(mausPosition).einheit().mul(faktor);

			blase.beschleunigen(richtung);
		}
		else {
			beschleunigtTicks = 0;
		}
	}
	
	public void verarbeiten(PacketSpielMaus packet) {
		if (packet.maustaste == 1)
			gedrückt = packet.zustand;
		else if (packet.maustaste == 3)
			blase.vergrößern(10);
	}
	
	public void verarbeiten(PacketMausPosition packet) {
		mausPosition.kopiere(packet.position);
	}
	
	public void setBlase(Blase blase) {
		this.blase = blase;
	}
}
