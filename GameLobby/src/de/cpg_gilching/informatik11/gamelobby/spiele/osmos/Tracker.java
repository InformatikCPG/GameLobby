package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.util.HashMap;
import java.util.Map;

public class Tracker {
	
	private OsmosServer server;
	private Map<Integer, BlasenTracker> blasen = new HashMap<Integer, BlasenTracker>();
	
	public Tracker(OsmosServer server) {
		this.server = server;
	}
	
	public void track(Blase b) {
		blasen.put(b.id, new BlasenTracker(b));
	}
	
	public void untrack(Blase b) {
		blasen.remove(b.id).entfernen();
	}
	
	public void untrackAll() {
		for (BlasenTracker t : blasen.values()) {
			t.entfernen();
		}
		blasen.clear();
	}
	
	public void tick() {
		for (BlasenTracker tracker : blasen.values())
			tracker.tick();
	}
	
	private class BlasenTracker {
		
		private Blase blase;
		private Vektor gesendetPosition = new Vektor();
		private double gesendetRadius = 0;
		
		public BlasenTracker(Blase blase) {
			this.blase = blase;
			
			server.packetAnAlle(new PacketNeueBlase(blase.id, true, blase.getFarbe(), blase.getLabel()));
			server.packetAnAlle(new PacketBlaseBewegen(blase.id, blase.getPosition()));
			server.packetAnAlle(new PacketBlaseDaten(blase.id, blase.getRadius()));
		}
		
		public void entfernen() {
			server.packetAnAlle(new PacketNeueBlase(blase.id, false, -1, ""));
		}
		
		public void tick() {
			if (!gesendetPosition.equals(blase.getPosition())) {
				server.packetAnAlle(new PacketBlaseBewegen(blase.id, blase.getPosition()));
				gesendetPosition.kopiere(blase.getPosition());
			}
			
			if (gesendetRadius != blase.getRadius()) {
				server.packetAnAlle(new PacketBlaseDaten(blase.id, blase.getRadius()));
				gesendetRadius = blase.getRadius();
			}
		}
	}
}
