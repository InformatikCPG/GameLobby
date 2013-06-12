package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;


import java.awt.Graphics2D;
import java.awt.Image;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

public class RenderSuper extends EntityRenderer {
	
	Image projectileSL = Helfer.bildLaden("realm/Superbullet.png");
	
	public void rendern(Graphics2D g) {
		g.drawImage(projectileSL, x - 3, y - 3, 25, 25, null);
	}

}