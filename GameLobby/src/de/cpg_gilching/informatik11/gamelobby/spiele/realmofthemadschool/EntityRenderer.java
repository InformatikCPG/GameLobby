package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Graphics2D;

public abstract class EntityRenderer {
	
	public int x;
	public int y;
	
	public EntityRenderer(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void positionieren(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public abstract void rendern(Graphics2D g);
	
}
