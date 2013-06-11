package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;


import java.awt.Graphics2D;
import java.awt.Image;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

public class RenderKreis extends EntityRenderer {
	
	Image projectileT = Helfer.bildLaden("realm/splashtop.png");
	Image projectileB = Helfer.bildLaden("realm/splashbottom.png");
	Image projectileL = Helfer.bildLaden("realm/splashlinks.png");
	Image projectileR = Helfer.bildLaden("realm/splashrechts.png");
	
	public void rendern(Graphics2D g) {
		Image pbild=null;
		switch (ausrichtung) {
		case 0:
			pbild=projectileT;
			break;
		case 1:
			pbild=projectileR;
			break;
		case 2:
			pbild=projectileB;
			break;
		case 3:
			pbild=projectileL;
			break;
		}

		g.drawImage (pbild,x-3, y-3,15,15, null);
	}

}
