package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

public class Dude extends Entity {

	//0=Norden 1=Osten 2=Süden 3=Westen
	boolean Wgedrückt;
	boolean Agedrückt;
	boolean Sgedrückt;
	boolean Dgedrückt;
	RealmofthemadschoolServer server;
	
	public Dude(RealmofthemadschoolServer ROMSS) {
		server = ROMSS;
		
	}
	public void tick() {
		if(Wgedrückt){
			y--;
			Ausrichtung = 0;
		}
		if(Agedrückt){
			x--;
			Ausrichtung = 3;
		}
		if(Sgedrückt){
			y++;
			Ausrichtung = 2;
		}
		if(Dgedrückt){
			x++;
			Ausrichtung = 1;
		}
	}
	
	public void attack() {
		server.einfügen(new Bullet(this));
		
	}
	
}
