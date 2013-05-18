package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ITastaturListener;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;


public class PongSpielClient extends ClientSpiel implements PaketManager, ITastaturListener {
	
	private int links;
	private int rechts;
	
	private int ballX, ballY;
	
	@Override
	public void starten() {
		leinwandAktivieren(PongBeschreibung.BILDSCHIRM_BREITE, PongBeschreibung.BILDSCHIRM_HÖHE);
		setPaketManager(this);
		
		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		tasteRegistrieren(KeyEvent.VK_H, this);
	}
	
	@Override
	public void onTasteGeändert(KeyEvent event, boolean zustand) {
		// die H-Taste, die oben registriert wurde
		System.out.format("Pong: H wurde %s!%n", (zustand ? "gedrückt" : "losgelassen"));
	}
	
	@Override
	public void leinwandRendern(Graphics2D g) {
		// ===========================
		// ====== Ball rendern =======
		// ===========================
		int r = PongBeschreibung.BALL_RADIUS;
		g.setColor(Color.blue);
		g.fillOval(ballX - r, ballY - r, 2 * r, 2 * r);
		
		
		// ===========================
		// ==== Schläger rendern =====
		// ===========================
		int linksX = PongBeschreibung.GRENZE_LINKS - PongBeschreibung.SCHLÄGER_BREITE;
		int rechtsX = PongBeschreibung.GRENZE_RECHTS;
		int linksY = links - PongBeschreibung.SCHLÄGER_HÖHE / 2;
		int rechtsY = rechts - PongBeschreibung.SCHLÄGER_HÖHE / 2;
		
		g.setColor(Color.white);
		g.fillRect(linksX, linksY, PongBeschreibung.SCHLÄGER_BREITE, PongBeschreibung.SCHLÄGER_HÖHE);
		g.fillRect(rechtsX, rechtsY, PongBeschreibung.SCHLÄGER_BREITE, PongBeschreibung.SCHLÄGER_HÖHE);
	}
	
	public void verarbeiten(PacketSchlägerBewegen packet) {
		links = packet.linkePosition;
		rechts = packet.rechtePosition;
	}
	
	public void verarbeiten(PacketBallBewegen packet) {
		ballX = packet.x;
		ballY = packet.y;
	}
	
}
