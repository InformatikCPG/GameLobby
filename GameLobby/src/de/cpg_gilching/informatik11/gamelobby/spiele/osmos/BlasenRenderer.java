package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

public class BlasenRenderer {
	
	private static final Stroke rahmenStroke = new BasicStroke(3.0f);

	private final OsmosClient client;
	public final int id;
	private final int spielerfarbe;
	public double radius = 0;
	public Vektor position = new Vektor();
	
	private Color farbe = Color.white;
	private Color rahmenFarbe = Color.green;

	public BlasenRenderer(OsmosClient client, int id, int spielerfarbe) {
		this.client = client;
		this.id = id;
		this.spielerfarbe = spielerfarbe;
		
		neueFarbe();
	}
	
	public void neueFarbe() {
		BlasenRenderer aktiv = client.getAktiveBlase();
		if (aktiv == this) {
			farbe = new Color(spielerfarbe);
			rahmenFarbe = Color.blue;
		}
		else if (aktiv != null) {
			float radiusRatio = (float) (radius / aktiv.radius);

			if (spielerfarbe > -1) {
				farbe = new Color(spielerfarbe);
			}
			else {
				float r = Helfer.clamp((+radiusRatio - 0.7f) * 2.0f, 0.4f, 1.0f);
				float g = 0.4f;
				float b = Helfer.clamp((-radiusRatio - 0.7f) * 2.0f, 0.4f, 1.0f);

				farbe = new Color(r, g, b);
			}

			rahmenFarbe = (radiusRatio > 1.0f ? Color.red : Color.blue);
		}
	}
	
	public void rendern(Graphics2D g) {
		g.setColor(farbe);
		g.fillOval((int) (position.x - radius), (int) (position.y - radius), (int) (2.0 * radius), (int) (2.0 * radius));
		
		g.setStroke(rahmenStroke);
		g.setColor(rahmenFarbe);
		g.drawOval((int) (position.x - radius), (int) (position.y - radius), (int) (2.0 * radius), (int) (2.0 * radius));
	}
	
}
