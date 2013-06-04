package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Color;
import java.awt.Graphics2D;

public class RenderKreis extends EntityRenderer {
	
	public RenderKreis(int x, int y) {
		super(x, y);
	}

	@Override
	public void rendern(Graphics2D g) {
		g.setColor(Color.white);
		g.fillOval(x - 5, y - 5, 10, 10);
	}

}
