package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

public class RenderDude extends EntityRenderer {

	Image phinten = Helfer.bildLaden("realm/playerhinten.png");
	Image pvorne = Helfer.bildLaden("realm/playervorne.png");
	Image plinks = Helfer.bildLaden("realm/playerlinks.png");
	Image prechts = Helfer.bildLaden("realm/playerrechts.png");
	
	@Override
	public void rendern(Graphics2D g) {
		Image bild=null;
		switch (ausrichtung) {
		case 0:
			bild=phinten;
			break;
		case 1:
			bild=prechts;
			break;
		case 2:
			bild=pvorne;
			break;
		case 3:
			bild=plinks;
			break;
		}
		g.drawImage (bild,x-10, y-10, null);
		g.setColor(Color.green);
		g.fillRect(x-12, y-22, health, 5);
	}
	
}
