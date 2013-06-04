package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class Dude extends Entity {

	//0=Norden 1=Osten 2=Süden 3=Westen
	boolean Wgedrückt;
	boolean Agedrückt;
	boolean Sgedrückt;
	boolean Dgedrückt;
	RealmofthemadschoolServer server;
	Spieler spieler;
	
	public Dude(RealmofthemadschoolServer ROMSS, Spieler spieler) {
		super(PacketEntityNeu.TYP_DUDE);
		server = ROMSS;
		this.spieler = spieler;
		
	}
	public void tick() {
		int speed = 3;
		if(Wgedrückt){
			y -= speed;
			Ausrichtung = 0;
		}
		if(Agedrückt){
			x -= speed;
			Ausrichtung = 3;
		}
		if(Sgedrückt){
			y += speed;
			Ausrichtung = 2;
		}
		if(Dgedrückt){
			x += speed;
			Ausrichtung = 1;
		}
	}
	
	public void attack() {
		server.einfügen(new Bullet(this));
		
	}
	
	public void damage() {
		health--;
		if(health <= 0) {
			dead = true;
		}
	}
	
}
