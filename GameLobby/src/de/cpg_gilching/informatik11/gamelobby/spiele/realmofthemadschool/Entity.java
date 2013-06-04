package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.EntityTracker.Trackbar;

public abstract class Entity implements Trackbar {
	
	int x;
	int y;
	int Ausrichtung;
	
	private int gesendetX, gesendetY, gesendetAusrichtung;
	
	@Override
	public SpielPacket spawnPacketErstellen(int id) {
		gesendetX = x;
		gesendetY = y;
		return new PacketEntityNeu(id, PacketEntityNeu.TYP_DUDE, x, y);
	}
	
	@Override
	public SpielPacket despawnPacketErstellen(int id) {
		return new PacketEntityTot(id);
	}
	
	@Override
	public SpielPacket bewegungsPacketErstellen(int id) {
		if (gesendetX == x && gesendetY == y && gesendetAusrichtung == Ausrichtung) {
			return null;
		}
		else {
			gesendetX = x;
			gesendetY = y;
			gesendetAusrichtung = Ausrichtung;
			
			return new PacketEntityBewegen(id, x, y, Ausrichtung, 0);
		}
	}

}
