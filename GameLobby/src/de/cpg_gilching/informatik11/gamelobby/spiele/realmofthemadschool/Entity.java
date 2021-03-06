package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.EntityTracker.Trackbar;

public abstract class Entity implements Trackbar {
	
	boolean dead = false;
	int health=20;
	int mana=15;
	int charge=0;
	int x;
	int y;
	int Ausrichtung;
	private int Typ;
	
	public Entity(int Typ) {
		this.Typ = Typ;
	}
	
	public abstract void tick(); 
	
	private int gesendetX, gesendetY, gesendetAusrichtung, gesendetHealth, gesendetMana, gesendetcharge;
	
	@Override
	public SpielPacket spawnPacketErstellen(int id) {
		gesendetX = x;
		gesendetY = y;
		return new PacketEntityNeu(id,Typ, x, y, health, mana, charge);
	}
	
	@Override
	public SpielPacket despawnPacketErstellen(int id) {
		return new PacketEntityTot(id);
	}
	
	@Override
	public SpielPacket bewegungsPacketErstellen(int id) {
		if (gesendetX == x && gesendetY == y && gesendetAusrichtung == Ausrichtung && gesendetHealth == health && gesendetMana == mana && gesendetcharge == charge) {
			return null;
		}
		else {
			gesendetX = x;
			gesendetY = y;
			gesendetAusrichtung = Ausrichtung;
			gesendetHealth = health;
			gesendetMana = mana;
			gesendetcharge = charge;
			
			return new PacketEntityBewegen(id, x, y, Ausrichtung, health, mana, charge);
		}
	}

}
