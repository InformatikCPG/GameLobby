package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class EntityTracker {
	
	public static interface Trackbar {
		
		/**
		 * Erstellt und gibt ein Spawn-Packet zurück, das vom Tracker verwendet werden kann.
		 * 
		 * @param id die Entity Id für das Packet
		 * @return ein Spawn-Packet
		 */
		public SpielPacket spawnPacketErstellen(int id);
		
		/**
		 * Erstellt und gibt ein Despawn-Packet zurück, das vom Tracker verwendet werden kann.
		 * 
		 * @param id die Entity Id für das Packet
		 * @return ein Despawn-Packet
		 */
		public SpielPacket despawnPacketErstellen(int id);
		
		/**
		 * Erstellt und gibt ein Bewegungs-Packet zurück, das vom Tracker verwendet werden kann.<br>
		 * Es kann null zurückgegeben werden, wenn seit dem letzten Aufruf keine Bewegung erfolgt ist.
		 * 
		 * @param id die Entity Id für das Packet
		 * @return ein Spawn-Packet, oder null
		 */
		public SpielPacket bewegungsPacketErstellen(int id);
		
	}

	private int idCounter = 1;
	private ServerSpiel server;
	private Map<Integer, Trackbar> targets = new HashMap<Integer, Trackbar>();
	
	public EntityTracker(ServerSpiel server) {
		this.server = server;
	}
	
	/**
	 * Fügt etwas {@link Trackbar}es dem Tracker hinzu, sodass es an Clients gesendet wird.
	 */
	public void trackTarget(Trackbar target) {
		int id = idCounter++;
		targets.put(id, target);
		
		server.packetAnAlle(target.spawnPacketErstellen(id));
	}
	
	/**
	 * Entfernt etwas {@link Trackbar}es vom Tracker, sodass Clients es nicht mehr anzeigen.
	 */
	public void untrackTarget(Trackbar target) {
		for (Entry<Integer, Trackbar> e : targets.entrySet()) {
			if (e.getValue() == target) {
				server.packetAnAlle(target.despawnPacketErstellen(e.getKey()));
				
				targets.remove(e.getKey());
				break;
			}
		}
	}
	
	public void untrackAll() {
		for (Entry<Integer, Trackbar> e : targets.entrySet()) {
			server.packetAnAlle(e.getValue().despawnPacketErstellen(e.getKey()));
		}
	}

	/**
	 * Kümmert sich um Positions-Updates, die an Clients gesendet werden müssen.
	 */
	public void tick() {
		for (Entry<Integer, Trackbar> e : targets.entrySet()) {
			SpielPacket p = e.getValue().bewegungsPacketErstellen(e.getKey());
			if (p != null)
				server.packetAnAlle(p);
		}
	}

}
