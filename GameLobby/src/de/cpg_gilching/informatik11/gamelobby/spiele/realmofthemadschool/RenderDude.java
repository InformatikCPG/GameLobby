package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Color;
import java.awt.Graphics2D;

public class RenderDude extends EntityRenderer {
	
	public RenderDude(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void rendern(Graphics2D g) {
		g.setColor(Color.blue);
		g.drawRect(x, y, 20, 20);
	}
	
}
