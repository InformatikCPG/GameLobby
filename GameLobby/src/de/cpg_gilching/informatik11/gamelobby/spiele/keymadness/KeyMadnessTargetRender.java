package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.Color;
import java.awt.Graphics2D;

public class KeyMadnessTargetRender {
	
	public int tastencode;
	public boolean valid;
	public int x;
	public int y;

	public KeyMadnessTargetRender(int tastencode, boolean valid) {
		this.tastencode = tastencode;
		this.valid = valid;
	}
	
	public void positionieren(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void rendern(Graphics2D g) {
		g.setColor(Color.white);
		g.drawRect(x, y, 10, 10);
	}

}
