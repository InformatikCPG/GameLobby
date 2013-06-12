package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

public class SuperBullet extends Entity {
	
	
	public Dude dude;

	public SuperBullet(Dude d) {
		super(PacketEntityNeu.TYP_SuperBULLET);
		this.dude = d;
		this.x = d.x;
		this.y = d.y;
		this.Ausrichtung = d.Ausrichtung;
	}

	public void tick() {
		if(Ausrichtung == 0) {
			y -= 4;
		}
		if(Ausrichtung == 3) {
			x -= 4;
		}
		if(Ausrichtung == 2) {
			y += 4;
		}
		if(Ausrichtung == 1) {
			x += 4;
		}
		dude.server.spcollisiondetecting(this);
	}
}
