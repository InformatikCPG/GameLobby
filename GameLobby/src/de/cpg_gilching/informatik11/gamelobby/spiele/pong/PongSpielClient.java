package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ITastaturListener;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;


public class PongSpielClient extends ClientSpiel implements PaketManager, ITastaturListener {
	
	private Image imgHintergrund = Helfer.bildLaden("pong/hintergrund.png");
	private Image imgSchläger = Helfer.bildLaden("pong/schlaeger.png");
	
	private int links;
	private int rechts;
	
	private int ballX = -100, ballY = -100;
	
	@Override
	public void starten() {
		leinwandAktivieren(PongBeschreibung.BILDSCHIRM_BREITE, PongBeschreibung.BILDSCHIRM_HÖHE);
		setPaketManager(this);
		
		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		
		// Alias-Tasten
		netzwerkTasteRegistrieren(KeyEvent.VK_W, KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_S, KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_PAGE_UP, KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_DOWN);
		
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
		// === Hintergrund rendern ===
		// ===========================
		g.drawImage(imgHintergrund, 0, 0, null);
		
		// ===========================
		// ====== Ball rendern =======
		// ===========================
		int r = PongBeschreibung.BALL_RADIUS;
		g.setColor(Color.blue);
		g.fillOval(ballX - r, ballY - r, 2 * r, 2 * r);
		
		
		// ===========================
		// ==== Schläger rendern =====
		// ===========================
		int linksX = PongBeschreibung.GRENZE_LINKS - PongBeschreibung.SCHLÄGER_BREITE - PongBeschreibung.BALL_RADIUS;
		int rechtsX = PongBeschreibung.GRENZE_RECHTS + PongBeschreibung.SCHLÄGER_BREITE + PongBeschreibung.BALL_RADIUS;
		int linksY = links - imgSchläger.getHeight(null) / 2;
		int rechtsY = rechts - imgSchläger.getHeight(null) / 2;
		
		g.setColor(Color.white);
		g.drawImage(imgSchläger, linksX, linksY, null);
		g.drawImage(imgSchläger, rechtsX, rechtsY, -imgSchläger.getWidth(null), imgSchläger.getHeight(null), null);
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
