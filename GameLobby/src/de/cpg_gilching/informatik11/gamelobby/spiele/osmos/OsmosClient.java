package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.IMausradListener;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class OsmosClient extends ClientSpiel implements PaketManager, IMausradListener {
	
	private Map<Integer, BlasenRenderer> blasen = new HashMap<Integer, BlasenRenderer>();
	private BlasenRenderer aktiveBlase = null;
	private int weltRadius = 0;
	
	private volatile float zielSkalierung = 1.0f;
	private float skalierung = 1.0f;

	private Vektor gesendetePosition = new Vektor();
	
	@Override
	protected void starten() {
		leinwandAktivieren(600, 600);
		setPaketManager(this);
		
		netzwerkMausRegistrieren();
		mausradRegistrieren(this);
	}
	
	@Override
	public void onMausGescrollt(MouseWheelEvent event) {
		int off = event.getWheelRotation();
		zielSkalierung -= off * 0.2f;
		
		if (2 * weltRadius * zielSkalierung < 550.0f) { // gesamte Welt sichtbar
			zielSkalierung = 550.0f / (2 * weltRadius);
		}
	}
	
	@Override
	public void leinwandRendern(Graphics2D g) {
		{
			Point p = getLeinwand().getMousePosition();
			if (p != null && aktiveBlase != null) {
				Vektor maus = new Vektor(p.x, p.y);
				maus.sub(300, 300).add(aktiveBlase.position);
				
				if (!maus.equals(gesendetePosition)) {
					spielPacketSenden(new PacketMausPosition(maus));
					gesendetePosition.kopiere(maus);
				}
			}
			
			// Skalierung interpolieren
			skalierung = (3 * skalierung + zielSkalierung) / 4;
		}
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.translate(Math.round(300), Math.round(300));
		
		if (aktiveBlase != null) {
			g.translate(-(int) (aktiveBlase.position.x * skalierung), -(int) (aktiveBlase.position.y * skalierung));
		}
		
		if (skalierung != 1)
			g.scale(skalierung, skalierung);
		
		g.setStroke(new BasicStroke(5.0f / skalierung));
		g.setColor(Color.gray);
		g.drawOval(-weltRadius, -weltRadius, 2 * weltRadius, 2 * weltRadius);
		
		for (BlasenRenderer renderer : blasen.values())
			renderer.rendern(g);
	}
	
	public void verarbeiten(PacketSetup packet) {
		weltRadius = (int) packet.weltRadius;
		aktiveBlase = blasen.get(packet.aktivId);
	}
	
	public void verarbeiten(PacketNeueBlase packet) {
		if (packet.neu)
			blasen.put(packet.id, new BlasenRenderer(this, packet.id));
		else if (blasen.remove(packet.id) == aktiveBlase)
			aktiveBlase = null;
	}
	
	public void verarbeiten(PacketBlaseBewegen packet) {
		blasen.get(packet.id).position.kopiere(packet.position);
	}
	
	public void verarbeiten(PacketBlaseDaten packet) {
		BlasenRenderer renderer = blasen.get(packet.id);
		renderer.radius = packet.radius;
		renderer.neueFarbe();
		
		// wenn unsere Blase sich verändert hat, alle Farben neu berechnen
		if (renderer == aktiveBlase) {
			for (BlasenRenderer b : blasen.values()) {
				b.neueFarbe();
			}
		}
	}
	
	public BlasenRenderer getAktiveBlase() {
		return aktiveBlase;
	}

}
