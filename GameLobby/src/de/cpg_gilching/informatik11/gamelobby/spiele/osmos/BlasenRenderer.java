package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.awt.Color;
import java.awt.Graphics2D;

public class BlasenRenderer {
	
	public final int id;
	private double radius;
	public Vektor position;
	
	public BlasenRenderer(int id, double radius, Vektor position) {
		this.id = id;
		this.radius = radius;
		this.position = position.klonen();
	}
	
	public void rendern(Graphics2D g) {
		g.setColor(Color.white);
		g.fillOval((int) (position.x - radius), (int) (position.y - radius), (int) (2.0 * radius), (int) (2.0 * radius));
		g.setColor(Color.blue);
		g.drawString("ID: " + id, (float) position.x, (float) position.y);
		g.drawString("x: " + position.x, (float) position.x - 10, (float) position.y + 10);
		g.drawString("y: " + position.y, (float) position.x - 10, (float) position.y + 20);
	}
	
}
