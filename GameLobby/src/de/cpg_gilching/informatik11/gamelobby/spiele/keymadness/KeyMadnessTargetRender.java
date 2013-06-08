package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

public class KeyMadnessTargetRender {
	
	public int tastencode;
	public boolean valid;
	public int x;
	public int y;
	static Image graphik = Helfer.bildLaden("keymadness/target.png");
	static Image graphikinvalid = Helfer.bildLaden("keymadness/target_invalid.png");
	static Image arrowup = Helfer.bildLaden("keymadness/arrow_up.png");
	static Image arrowdown = Helfer.bildLaden("keymadness/arrow_down.png");
	static Image arrowleft = Helfer.bildLaden("keymadness/arrow_left.png");
	static Image arrowright = Helfer.bildLaden("keymadness/arrow_right.png");

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
			g.drawImage(graphik, x - 25, y - 25, null);
		}
		else{
			g.drawImage(graphikinvalid, x - 25, y - 25, null);
		}
		if(tastencode == KeyEvent.VK_UP){
			g.drawImage(arrowup, x - 25, y - 25, null);
		}
		else if(tastencode == KeyEvent.VK_DOWN){
			g.drawImage(arrowdown, x - 25, y - 25, null);
		}
		else if(tastencode == KeyEvent.VK_RIGHT){
			g.drawImage(arrowright, x - 25, y - 25, null);
		}
		else if(tastencode == KeyEvent.VK_LEFT){
			g.drawImage(arrowleft, x - 25, y - 25, null);
		}
	}

}
