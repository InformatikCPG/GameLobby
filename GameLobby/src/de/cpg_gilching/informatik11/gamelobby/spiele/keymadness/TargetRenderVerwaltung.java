package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class TargetRenderVerwaltung {
	
	private KeyMadnessClient client;
	private Map<Integer, KeyMadnessTargetRender> targetsMap = new HashMap<Integer, KeyMadnessTargetRender>();

	public TargetRenderVerwaltung(KeyMadnessClient client) {
		this.client = client;
	}
	
	public void neuesTarget(PacketTargetNeu packet) {
		KeyMadnessTargetRender renderer = new KeyMadnessTargetRender(packet.tastencode, packet.valid);
		targetsMap.put(packet.id, renderer);
	}
	
	public void targetBewegen(PacketTargetBewegen packet) {
		KeyMadnessTargetRender renderer = targetsMap.get(packet.id);
		renderer.positionieren(packet.x, packet.y);
	}
	
	public void targetEntfernen(PacketTargetTot packet) {
		targetsMap.remove(packet.id);
	}
	
	public void targetsRendern(Graphics2D g) {
		for (KeyMadnessTargetRender renderer : targetsMap.values()) {
			renderer.rendern(g);
		}
	}
	
	public KeyMadnessClient getClient() {
		return client;
	}

}
