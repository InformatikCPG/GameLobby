package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Color;
import java.awt.Graphics2D;

public class RenderKreis extends EntityRenderer {
	
	@Override
	public void rendern(Graphics2D g) {
		g.setColor(Color.white);
		g.fillOval(x - 3, y - 3, 6, 6);
	}

}
