package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

public class Bullet extends Entity {
	
	
	public Bullet(Dude d) {
		this.x = d.x;
		this.y = d.y;
		this.Ausrichtung = d.Ausrichtung;
	}

	public void tick() {
		if(Ausrichtung == 0) {
			y-=5;
		}
		if(Ausrichtung == 3) {
			x-=5;
		}
		if(Ausrichtung == 2) {
			y+=5;
		}
		if(Ausrichtung == 1) {
			x+=5;
		}
		
	}
}
