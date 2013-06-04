package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

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
		if(valid == true) {
			g.setColor(Color.white);
		}
		else{
			g.setColor(Color.red);
		}
		g.drawRect(x - 25, y - 25, 50, 50);
		if(tastencode == KeyEvent.VK_UP){
			g.drawString("up", x, y);
		}
		else if(tastencode == KeyEvent.VK_DOWN){
			g.drawString("down", x, y);
		}
		else if(tastencode == KeyEvent.VK_RIGHT){
			g.drawString("right", x, y);
		}
		else if(tastencode == KeyEvent.VK_LEFT){
			g.drawString("left", x, y);
		}
	}

}
