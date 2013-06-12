package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class Dude extends Entity {

	// 0=Norden 1=Osten 2=Süden 3=Westen
	boolean Wgedrückt;
	boolean Agedrückt;
	boolean Sgedrückt;
	boolean Dgedrückt;
	boolean SPACEgedrückt;
	RealmofthemadschoolServer server;
	Spieler spieler;
	int time;
	int charge;
	int chargetime;

	public Dude(RealmofthemadschoolServer ROMSS, Spieler spieler) {
		super(PacketEntityNeu.TYP_DUDE);
		server = ROMSS;
		this.spieler = spieler;

	}

	public void tick() {
		int speed = 3;
		if (Wgedrückt) {
			y -= speed;
			Ausrichtung = 0;
		}
		if (Agedrückt) {
			x -= speed;
			Ausrichtung = 3;
		}
		if (Sgedrückt) {
			y += speed;
			Ausrichtung = 2;
		}
		if (Dgedrückt) {
			x += speed;
			Ausrichtung = 1;
		}

		if (x <= 20) {
			x = 20;
		}
		if (x >= 580) {
			x = 580;
		}
		if (y <= 20) {
			y = 20;
		}
		if (y >= 580) {
			y = 580;
		}
	    charging();
		time++;
		if (SPACEgedrückt) {
		chargetime++;
		}
		else {
		chargetime = 0;
		}
		
		regeneratemana();
		dowehavesomemanaleft();
	}

	public void dowehavesomemanaleft() {
		if(mana >=1) {
			nomana = false;
		}
	}
	public void supderduperwaschbär() {
		if (SPACEgedrückt == false) {
			if(charge >= 5) {
				superattack();
			}
			else {
				attack();
			}
		}
	}
	public void charging() {
		if (chargetime == 15) {
			charge++;
		}
		if (chargetime == 30) {
			charge++;
		}
		if (chargetime == 45) {
			charge++;
		}
		if (chargetime == 60) {
			charge++;
		}
		if (chargetime == 75) {
			charge++;
		}
	}
	
	public void regeneratemana() {
		if(mana != 15 && time >= 30) {
		 mana++;
		 time = 0;
		}
	}
	
	public void attack() {
		if (dead == false && nomana == false) {
			mana--;
			server.einfügen(new Bullet(this));
			if (mana <= 0) {
				nomana = true;
			}
		}
	}
			
			public void superattack() {
				if (dead == false && nomana == false && mana >= 5) {
					mana --;
					mana --;
					mana --;
					mana --;
					mana --;
					server.einfügen(new SuperBullet(this));
					charge = 0;
					if (mana <= 0) {
						nomana = true;
					}
		}

	}

	public void damage() {
		health--;
		if (health <= 0) {
			dead = true;
		}
	}

	public void superdamage() {
		health--;
		health--;
		health--;
		health--;
		health--;
		health--;
		health--;
		health--;
		health--;
		health--;
		if (health <= 0) {
			dead = true;
		}
	}

}
