package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.EntityTracker.Trackbar;
import de.cpg_gilching.informatik11.gamelobby.spiele.osmos.Vektor;

public class KeyMadnessTarget implements Trackbar {
	
	boolean richtung;
	int tastencode;
	boolean valid;
	int ziel;
	double x;
	double y;
	KeyMadnessServer server;
	double geschwindigkeit;
	boolean tot;
	
	public KeyMadnessTarget(KeyMadnessServer server){
		this.server = server;
		richtung = Helfer.zufallsZahl(2) == 0;
		tastencode = Helfer.zufallsElement(server.daten.tastencodes, false);
		valid = Helfer.zufallsZahl(3) != 0;
		if(richtung){
			ziel = 1;
			x=server.daten.punkte[0].x;
			y=server.daten.punkte[0].y;
		}
		else{
			ziel = server.daten.punkte.length - 2;
			x=server.daten.punkte[server.daten.punkte.length - 1].x;
			y=server.daten.punkte[server.daten.punkte.length - 1].y;
		}
		geschwindigkeit = 1.0;
	}
	
	public void tick(){
		int zielx = server.daten.punkte[ziel].x;
		int ziely = server.daten.punkte[ziel].y;
		Vektor v = new Vektor(zielx, ziely).sub(x, y);
		if (v.längeQuadrat() > 0) {
			v.einheit().mul(geschwindigkeit);
			x = x + v.x;
			y = y + v.y;
		}
		

		double abstand = v.kopiere(x, y).sub(zielx, ziely).länge();
		if(abstand <= geschwindigkeit){
			if(richtung){
				ziel = ziel + 1;
				if(ziel >= server.daten.punkte.length){
					tot = true;
				}
			}
			else{
				ziel = ziel - 1;
				if(ziel <= -1){
					tot = true;
				}
				
			}
		}
	}
	
	// Implementierung des Trackbar-Interface
	@Override
	public SpielPacket spawnPacketErstellen(int id) {
		return new PacketTargetNeu(id, this);
	}
	
	@Override
	public SpielPacket despawnPacketErstellen(int id) {
		return new PacketTargetTot(id);
	}
	
	@Override
	public SpielPacket bewegungsPacketErstellen(int id) {
		return new PacketTargetBewegen(id, x, y);
	}
}
