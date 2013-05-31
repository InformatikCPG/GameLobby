package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;


public class PongSpielServer extends ServerSpiel {
	
	private Spieler links;
	private Spieler rechts;
	
	private int geschwindigkeitLinks = 0;
	private int geschwindigkeitRechts = 0;
	private int schlägerLinks = 300;
	private int schlägerRechts = 300;
	
	private int pauseTicks;
	
	private int ballX;
	private int ballY;
	private int ballGeschwindigkeitX;
	private int ballGeschwindigkeitY;
	
	@Override
	public void starten() {
		links = teilnehmer.get(0);
		rechts = teilnehmer.get(1);
		
		packetAnAlle(new PacketSchlägerBewegen(schlägerLinks, schlägerRechts));
		ballZurücksetzen();
	}
	
	@Override
	public PaketManager paketManagerErstellen(Spieler spieler) {
		return new PongPaketManagerServer(this, spieler);
	}
	
	private void ballZurücksetzen() {
		ballX = 300;
		ballY = 300;
		
		if (Helfer.zufallsZahl(2) == 0)
			ballGeschwindigkeitX = 1;
		else
			ballGeschwindigkeitX = -1;
		
		ballGeschwindigkeitY = Helfer.zufallsZahl(-5, 5);
		
		pauseTicks = 20;
	}
	
	@Override
	public void tick() {
		// ===========================
		// ==== Schläger-Position ====
		// ===========================
		schlägerLinks += 10 * geschwindigkeitLinks;
		schlägerRechts += 10 * geschwindigkeitRechts;
		
		int maxSchläger = PongBeschreibung.BILDSCHIRM_HÖHE - (PongBeschreibung.SCHLÄGER_HÖHE / 2);
		int minSchläger = PongBeschreibung.SCHLÄGER_HÖHE / 2;
		
		if (schlägerLinks > maxSchläger)
			schlägerLinks = maxSchläger;
		else if (schlägerLinks < minSchläger)
			schlägerLinks = minSchläger;
		if (schlägerRechts > maxSchläger)
			schlägerRechts = maxSchläger;
		else if (schlägerRechts < minSchläger)
			schlägerRechts = minSchläger;
		
		// wenn sich etwas bewegt hat
		if (geschwindigkeitLinks != 0 || geschwindigkeitRechts != 0) {
			packetAnAlle(new PacketSchlägerBewegen(schlägerLinks, schlägerRechts));
		}
		
		
		// ===========================
		// ====== Ball-Position ======
		// ===========================
		if (pauseTicks > 0) {
			pauseTicks--;
		}
		else {
			ballX += 10 * ballGeschwindigkeitX;
			ballY += ballGeschwindigkeitY;
			
			if (ballY < 0 || ballY > PongBeschreibung.BILDSCHIRM_HÖHE) {
				ballGeschwindigkeitY *= -1;
			}
			
			if (ballX > PongBeschreibung.GRENZE_RECHTS) {
				if (schlägerBerührtBall(schlägerRechts)) {
					ballX = PongBeschreibung.GRENZE_RECHTS;
					ballGeschwindigkeitX = -1;
					
					int yDiff = ballY - schlägerRechts;
					ballGeschwindigkeitY = (20 * yDiff) / PongBeschreibung.SCHLÄGER_HÖHE;
				}
				else {
					scoreboard.punktHinzufügen(links);
					ballZurücksetzen();
				}
			}
			
			else if (ballX < PongBeschreibung.GRENZE_LINKS) {
				if (schlägerBerührtBall(schlägerLinks)) {
					ballX = PongBeschreibung.GRENZE_LINKS;
					ballGeschwindigkeitX = 1;
					
					int yDiff = ballY - schlägerLinks;
					ballGeschwindigkeitY = (20 * yDiff) / PongBeschreibung.SCHLÄGER_HÖHE;
				}
				else {
					scoreboard.punktHinzufügen(rechts);
					ballZurücksetzen();
				}
			}
			
			packetAnAlle(new PacketBallBewegen(ballX, ballY));
		}
	}
	
	private boolean schlägerBerührtBall(int schläger) {
		return (ballY >= schläger - (PongBeschreibung.SCHLÄGER_HÖHE / 2) && ballY <= schläger + (PongBeschreibung.SCHLÄGER_HÖHE / 2));
	}
	
	public void setSpielerGeschwindigkeit(Spieler spieler, int geschwindigkeit) {
		if (spieler == links) {
			geschwindigkeitLinks = geschwindigkeit;
			//			geschwindigkeitRechts = geschwindigkeit;
		}
		else if (spieler == rechts) {
			geschwindigkeitRechts = geschwindigkeit;
			//			geschwindigkeitLinks = geschwindigkeit;
		}
	}
	
}
