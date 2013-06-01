package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TargetTracker {
	
	private int idCounter = 1;
	private KeyMadnessServer server;
	private Map<Integer, KeyMadnessTarget> targets = new HashMap<Integer, KeyMadnessTarget>();
	
	public TargetTracker(KeyMadnessServer server) {
		this.server = server;
	}
	
	/**
	 * Fügt ein {@link KeyMadnessTarget} dem Tracker hinzu, sodass es an Clients gesendet wird.
	 */
	public void trackTarget(KeyMadnessTarget target) {
		int id = idCounter++;
		targets.put(id, target);
		
		server.packetAnAlle(new PacketTargetNeu(id, target));
	}
	
	/**
	 * Entfernt ein {@link KeyMadnessTarget} vom Tracker, sodass Clients es nicht mehr anzeigen.
	 */
	public void untrackTarget(KeyMadnessTarget target) {
		for (Entry<Integer, KeyMadnessTarget> e : targets.entrySet()) {
			if (e.getValue() == target) {
				server.packetAnAlle(new PacketTargetTot(e.getKey()));
				
				targets.remove(e.getKey());
				break;
			}
		}
	}
	
	/**
	 * Kümmert sich um Positions-Updates, die an Clients gesendet werden müssen.
	 */
	public void tick() {
		for (Entry<Integer, KeyMadnessTarget> e : targets.entrySet()) {
			server.packetAnAlle(new PacketTargetBewegen(e.getKey(), e.getValue().x, e.getValue().y));
		}
	}

}
