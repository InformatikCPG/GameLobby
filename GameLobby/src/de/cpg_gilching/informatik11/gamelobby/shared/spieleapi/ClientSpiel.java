package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.awt.Graphics2D;

public abstract class ClientSpiel {
	
	protected PaketManager paketManager;
	
	public abstract void rendern(Graphics2D g, int breite, int höhe);
	
}
