package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class DudeRenderVerwaltung {
	
	private Map<Integer, EntityRenderer> targetsMap = new HashMap<Integer, EntityRenderer>();

	public DudeRenderVerwaltung() {
	}
	
	public void neueEntity(PacketEntityNeu packet) {
		EntityRenderer renderer;
		
		switch (packet.typ) {
		case PacketEntityNeu.TYP_DUDE:
			renderer = new RenderDude(packet.x, packet.y);
			break;
		case PacketEntityNeu.TYP_BULLET:
			renderer = new RenderKreis(packet.x, packet.y);
			break;
		default:
			renderer = null;
			break;
		}

		targetsMap.put(packet.id, renderer);
	}
	
	public void entityBewegen(PacketEntityBewegen packet) {
		EntityRenderer renderer = targetsMap.get(packet.id);
		renderer.positionieren(packet.x, packet.y);
	}
	
	public void entityEntfernen(PacketEntityTot packet) {
		targetsMap.remove(packet.id);
	}
	
	public void entitiesRendern(Graphics2D g) {
		for (EntityRenderer renderer : targetsMap.values()) {
			renderer.rendern(g);
		}
	}

}
