package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class OsmosServer extends ServerSpiel {
	
	private double weltRadius;
	private List<Blase> blasen = new ArrayList<Blase>();
	private Tracker tracker = new Tracker(this);
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new SpielerController(this, spieler);
	}
	
	@Override
	protected void starten() {
		weltRadius = 600.0 * Math.log(teilnehmer.size() + Math.E);
		
		double winkelSchritt = 2 * Math.PI / teilnehmer.size();
		double winkel = 0;
		
		for (Spieler spieler : teilnehmer) {
			Blase b = new Blase(50.0);
			b.setController((SpielerController) getPaketManagerFür(spieler));
			b.teleport(new Vektor(Math.sin(winkel) * weltRadius / 2, Math.cos(winkel) * weltRadius / 2));
			blaseHinzufügen(b);
			
			packetAnSpieler(spieler, new PacketSetup(weltRadius, b.id));
			
			winkel += winkelSchritt;
		}
		
		blaseHinzufügen(new Blase(20));
	}
	
	@Override
	public void tick() {
		for (Blase b : blasen)
			b.tick();
		
		tracker.tick();
	}
	
	public void blaseHinzufügen(Blase b) {
		blasen.add(b);
		tracker.track(b);
	}
	
}
