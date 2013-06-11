package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.EntityTracker.Trackbar;

public abstract class Entity implements Trackbar {
	
	boolean dead = false;
	boolean nomana = false;
	int health=20;
	int mana=15;
	int x;
	int y;
	int Ausrichtung;
	private int Typ;
	
	public Entity(int Typ) {
		this.Typ = Typ;
	}
	
	public abstract void tick(); 
	
	private int gesendetX, gesendetY, gesendetAusrichtung, gesendetHealth, gesendetMana;
	
	@Override
	public SpielPacket spawnPacketErstellen(int id) {
		gesendetX = x;
		gesendetY = y;
		return new PacketEntityNeu(id,Typ, x, y, health, mana);
	}
	
	@Override
	public SpielPacket despawnPacketErstellen(int id) {
		return new PacketEntityTot(id);
	}
	
	@Override
	public SpielPacket bewegungsPacketErstellen(int id) {
		if (gesendetX == x && gesendetY == y && gesendetAusrichtung == Ausrichtung && gesendetHealth == health && gesendetMana == mana) {
			return null;
		}
		else {
			gesendetX = x;
			gesendetY = y;
			gesendetAusrichtung = Ausrichtung;
			gesendetHealth = health;
			gesendetMana = mana;
			
			return new PacketEntityBewegen(id, x, y, Ausrichtung, health, mana);
		}
	}

}
