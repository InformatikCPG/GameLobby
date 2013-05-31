package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

public class Blase {
	
	private static int idCounter = 0;
	
	public final int id;
	private final OsmosServer server;
	private SpielerController controller = null;
	private Vektor position = new Vektor();
	private Vektor geschwindigkeit = new Vektor();
	private boolean tot = false;
	
	private double radius;

	
	public Blase(OsmosServer server, double radius) {
		this.id = ++idCounter;
		this.server = server;
		this.radius = radius;
	}
	
	public void tick() {
		if (controller != null) {
			controller.tick();
		}
		
		position.add(geschwindigkeit);
		
		double abstand = position.länge() + radius;
		double zuWeit = abstand - server.getWeltRadius();
		if (zuWeit > 0) {
			position.mul(1.0 - (zuWeit / abstand));
			
			double kollisionsWinkel = Math.toDegrees(Math.atan2(position.y, position.x));
			geschwindigkeit.drehen(-kollisionsWinkel);
			geschwindigkeit.x *= -1;
			geschwindigkeit.drehen(kollisionsWinkel);
		}

	}
	
	public void setController(SpielerController controller) {
		this.controller = controller;
		if (controller != null)
			controller.setBlase(this);
	}
	
	public void beschleunigen(Vektor a) {
		geschwindigkeit.add(a);
		
		double verloren = radius * a.länge() * 0.05;

		radius -= Math.sqrt(verloren);

		Blase neu = new Blase(server, verloren);
		neu.getPosition().kopiere(a).einheit().mul(-radius - verloren - 5).add(position);
		neu.getGeschwindigkeit().kopiere(a).mul(-6).add(geschwindigkeit);

		server.blaseHinzufügen(neu);
	}
	
	/**
	 * Gibt den Wert zurück, der tatsächlich geändert wurde.
	 */
	public double vergrößern(double wert) {
		if (radius < -wert) {
			tot = true;
			double geändert = -radius;
			radius = 0;
			return geändert;
		}

		radius += wert;
		return wert;
	}

	public void teleport(Vektor ziel) {
		position.kopiere(ziel);
	}
	
	public boolean istTot() {
		return tot;
	}

	public double getRadius() {
		return radius;
	}
	
	public Vektor getPosition() {
		return position;
	}
	
	public Vektor getGeschwindigkeit() {
		return geschwindigkeit;
	}
	
	
	public static boolean kollidiert(Blase b1, Blase b2) {
		return kollidiert(b1, b2, new Vektor());
	}
	
	public static boolean kollidiert(Blase b1, Blase b2, Vektor v) {
		v.kopiere(b1.getPosition()).sub(b2.getPosition());

		double radien = b1.getRadius() + b2.getRadius();
		
		return v.längeQuadrat() < (radien * radien);
	}

}
