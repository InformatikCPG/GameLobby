package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.Point;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.EntityTracker.Trackbar;
import de.cpg_gilching.informatik11.gamelobby.spiele.osmos.Vektor;

public class KeyMadnessTarget implements Trackbar {

	KeyMadnessServer server;
	int tastencode;
	boolean valid;
	// Richtung, in die sich die Target auf dem Pfad bewegt:
	boolean richtung;
	// Ziel, auf das sich die Target mit bestimmter Geschwindigkeit zubewegt:
	int ziel;
	// Aktuelle Position:
	double x;
	double y;
	boolean tot;
	// Array aus Punkten/Pfad auf dem sich die Target bewegt:
	public Point[] punkte;
	
	// neue Target wird von Server aus erstellt und bekommt den Verweis auf den Server und einen Pfad aus dem
	// zweidimensionalen Array pfade, der aus Punkten besteht:
	public KeyMadnessTarget(KeyMadnessServer server, Point[] punkte){
		// die Daten werden in Variablen eingespeichert:
		this.server = server;
		this.punkte = punkte;
		// Zufällig generierte Richtung ( 1 oder 0 -> true oder false):
		richtung = Helfer.zufallsZahl(2) == 0;
		// Tastencode wird zufällig zugewiesen aus der Liste der Tastencodes in den Daten:
		tastencode = Helfer.zufallsElement(server.daten.tastencodes, false);
		// Zuweisung ob Target valid oder nicht ist:
		valid = Helfer.zufallsZahl(8) != 0;
		
		// Je nach Richtung wird das jeweilige erste Ziel und die aktuelle Position festgelegt:
		if(richtung){
			ziel = 1;
			x=punkte[0].x;
			y=punkte[0].y;
		}
		else{
			ziel = punkte.length - 2;
			x=punkte[punkte.length - 1].x;
			y=punkte[punkte.length - 1].y;
		}
	}
	
	public void tick(){
		// Koordinaten für Ziel werden ausgelesen:
		int zielx = punkte[ziel].x;
		int ziely = punkte[ziel].y;
		// Vektor von aktueller Position zu Ziel wird erstellt:
		Vektor v = new Vektor(zielx, ziely).sub(x, y);
		if (v.längeQuadrat() > 0) {
			// Einheitsvektor * Geschwindigkeit, mit der sich Target bewegen soll:
			v.einheit().mul(server.geschwindigkeit[server.level]);
			// Neue Koordinaten werden berechnet:
			x = x + v.x;
			y = y + v.y;
		}
		
		// Neues Ziel wird bestimmt wenn Target mit alter Geschwindigkeit die Target erreichen oder überspringen würde:
		double abstand = v.kopiere(x, y).sub(zielx, ziely).länge();
		if(abstand <= server.geschwindigkeit[server.level]){
			// Je nach Richtung natürlich:
			if(richtung){
				ziel = ziel + 1;
				if(ziel >= punkte.length){
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
