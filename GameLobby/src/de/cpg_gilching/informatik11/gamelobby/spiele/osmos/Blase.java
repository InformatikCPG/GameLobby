package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

public class Blase {
	
	private static int idCounter = 0;
	
	public final int id;
	private SpielerController controller = null;
	private Vektor position = new Vektor();
	
	private double radius;
	
	public Blase(double radius) {
		this.id = ++idCounter;
		this.radius = radius;
	}
	
	public void tick() {
		if (controller != null) {
			controller.tick();
			return;
		}
		
		// TODO blasen KI
	}
	
	public void setController(SpielerController controller) {
		this.controller = controller;
		if (controller != null)
			controller.setBlase(this);
	}
	
	public void teleport(Vektor ziel) {
		position.kopiere(ziel);
	}
	
	public double getRadius() {
		return radius;
	}
	
	public Vektor getPosition() {
		return position;
	}
	
}
