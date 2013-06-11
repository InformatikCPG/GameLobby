package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Graphics2D;

public abstract class EntityRenderer {
	
	public int x;
	public int y;
	public int ausrichtung;
	public int health;
	public int mana;

	public void bewegen(int x, int y, int ausrichtung, int health, int mana) {
		this.x = x;
		this.y = y;
		this.ausrichtung = ausrichtung;
		this.health = health;
		this.mana = mana;
	}

	public abstract void rendern(Graphics2D g);
	
}
