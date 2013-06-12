package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;


import java.awt.Graphics2D;
import java.awt.Image;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

public class RenderSuper extends EntityRenderer {
	
	Image projectileSL = Helfer.bildLaden("realm/Superbullet.png");
	
	public void rendern(Graphics2D g) {
		Image pbild=null;
		switch (ausrichtung) {
		case 0:
			pbild=projectileSL;
			break;
		case 1:
			pbild=projectileSL;
			break;
		case 2:
			pbild=projectileSL;
			break;
		case 3:
			pbild=projectileSL;
			break;
		}

		g.drawImage (pbild,x-3, y-3,25,25, null);
	}

}