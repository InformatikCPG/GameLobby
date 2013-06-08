package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Graphics2D;

public abstract class EntityRenderer {
	
	public int x;
	public int y;
	public int ausrichtung;
	public int health;

	public void bewegen(int x, int y, int ausrichtung, int health) {
		this.x = x;
		this.y = y;
		this.ausrichtung = ausrichtung;
		this.health = health;
	}

	public abstract void rendern(Graphics2D g);
	
}
