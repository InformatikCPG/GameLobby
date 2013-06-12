package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class EntityRenderVerwaltung {
	
	private Map<Integer, EntityRenderer> targetsMap = new HashMap<Integer, EntityRenderer>();

	public EntityRenderVerwaltung() {
	}
	
	public void neueEntity(PacketEntityNeu packet) {
		EntityRenderer renderer;
		
		switch (packet.typ) {
		case PacketEntityNeu.TYP_DUDE:
			renderer = new RenderDude();
			break;
		case PacketEntityNeu.TYP_BULLET:
			renderer = new RenderKreis();
			break;
		case PacketEntityNeu.TYP_SuperBULLET:
			renderer = new RenderSuper();
			break;
		default:
			renderer = null;
			break;
		}

		renderer.x = packet.x;
		renderer.y = packet.y;
		renderer.health = packet.health;
		renderer.mana = packet.mana;
		renderer.charge = packet.charge;

		targetsMap.put(packet.id, renderer);
	}
	
	public void entityBewegen(PacketEntityBewegen packet) {
		EntityRenderer renderer = targetsMap.get(packet.id);
		renderer.bewegen(packet.x, packet.y, packet.ausrichtung, packet.health, packet.mana, packet.charge);
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
