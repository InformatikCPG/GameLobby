package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Color;
import java.awt.Graphics2D;

public class RenderDude extends EntityRenderer {
	
	@Override
	public void rendern(Graphics2D g) {
		g.setColor(Color.pink);
		g.drawRect(x-10, y-10, 20, 20);
		g.setColor(Color.green);
		g.fillRect(x-12, y-22, health, 5);
	}
	
}
