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
	
	public void tick() {
		for (BlasenTracker tracker : blasen.values())
			tracker.tick();
	}
	
	private class BlasenTracker {
		
		private Blase blase;
		private Vektor gesendetePosition = new Vektor();
		
		public BlasenTracker(Blase blase) {
			this.blase = blase;
			
			server.packetAnAlle(new PacketNeueBlase(blase.id, blase.getRadius(), blase.getPosition()));
		}
		
		public void tick() {
			if (!gesendetePosition.equals(blase.getPosition())) {
				server.packetAnAlle(new PacketBlaseBewegen(blase.id, blase.getPosition()));
				gesendetePosition.kopiere(blase.getPosition());
			}
		}
	}
}
