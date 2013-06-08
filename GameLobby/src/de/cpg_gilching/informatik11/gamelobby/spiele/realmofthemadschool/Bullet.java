package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

public class Bullet extends Entity {
	
	
	public Dude dude;

	public Bullet(Dude d) {
		super(PacketEntityNeu.TYP_BULLET);
		this.dude = d;
		this.x = d.x;
		this.y = d.y;
		this.Ausrichtung = d.Ausrichtung;
	}

	public void tick() {
		if(Ausrichtung == 0) {
			y -= 10;
		}
		if(Ausrichtung == 3) {
			x -= 10;
		}
		if(Ausrichtung == 2) {
			y += 10;
		}
		if(Ausrichtung == 1) {
			x += 10;
		}
		dude.server.collisiondetecting(this);
	}
}
