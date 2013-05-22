package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class OsmosClient extends ClientSpiel implements PaketManager {
	
	private Map<Integer, BlasenRenderer> blasen = new HashMap<Integer, BlasenRenderer>();
	private BlasenRenderer aktiveBlase = null;
	private int weltRadius = 0;
	
	@Override
	protected void starten() {
		leinwandAktivieren(600, 600);
		setPaketManager(this);
		
		netzwerkMausRegistrieren();
	}
	
	@Override
	public void leinwandRendern(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		final float scale = 0.7f;
		
		if (aktiveBlase != null) {
			g.translate(Math.round(300), Math.round(300));
			g.translate(-(int) (aktiveBlase.position.x * scale), -(int) (aktiveBlase.position.y * scale));
		}
		
		if (scale != 1)
			g.scale(scale, scale);
		
		g.setStroke(new BasicStroke(5.0f / scale));
		g.setColor(Color.gray);
		g.drawOval(-weltRadius, -weltRadius, 2 * weltRadius, 2 * weltRadius);
		
		g.drawLine((int) aktiveBlase.position.x, (int) aktiveBlase.position.y, 0, 0);
		
		for (BlasenRenderer renderer : blasen.values())
			renderer.rendern(g);
	}
	
	public void verarbeiten(PacketSetup packet) {
		weltRadius = (int) packet.weltRadius;
		aktiveBlase = blasen.get(packet.aktivId);
	}
	
	public void verarbeiten(PacketNeueBlase packet) {
		blasen.put(packet.id, new BlasenRenderer(packet.id, packet.radius, packet.position));
	}
	
	public void verarbeiten(PacketBlaseBewegen packet) {
		blasen.get(packet.id).position.kopiere(packet.position);
	}
	
}
