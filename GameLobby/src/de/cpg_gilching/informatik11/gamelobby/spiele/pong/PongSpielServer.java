package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielChat.ChatBefehl;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;


public class PongSpielServer extends ServerSpiel {
	
	// die beiden Spieler
	private Spieler links;
	private Spieler rechts;
	
	// die "Geschwindigkeit" der Schläger (-1, 0 oder 1; je nach gedrückter Taste des Clients
	private int geschwindigkeitLinks = 0;
	private int geschwindigkeitRechts = 0;
	// die Y-Position der beiden Schläger
	private int schlägerLinks = 300;
	private int schlägerRechts = 300;
	
	// die Anzahl der Ticks, die vor Neustart noch gewartet wird; am Anfang 90, um Spielstart etwas zu verzögern
	private int pauseTicks = 90;
	
	// die Position des Balls
	private int ballX = -100;
	private int ballY = -100;
	// die momentane X- und Y-Geschwindigkeit des Balls
	private int ballGeschwindigkeitX = -1;
	private int ballGeschwindigkeitY = -1;
	
	// per Command änderbare Geschwindigkeits-Faktoren für Ball und Schläger
	private int ballCustomSpeed = 10;
	private int schlägerCustomSpeed = 8;
	
	@Override
	public void starten() {
		links = teilnehmer.get(0);
		rechts = teilnehmer.get(1);
		
		packetAnAlle(new PacketSchlägerBewegen(schlägerLinks, schlägerRechts));
		
		// die Chat-Befehle registrieren

		chat.befehlRegistrieren("ballspeed", new ChatBefehl() {
			@Override
			public void ausführen(Spieler sender, String[] argumente) {
				if (argumente.length >= 1) {
					try {
						ballCustomSpeed = Integer.parseInt(argumente[0]);
					} catch (NumberFormatException e) {
						chat.nachrichtAnSpieler(sender, "Du musst eine Zahl eingeben!");
					}
				}
			}
		});
		
		chat.befehlRegistrieren("schlägerspeed", new ChatBefehl() {
			@Override
			public void ausführen(Spieler sender, String[] argumente) {
				if (argumente.length >= 1) {
					try {
						schlägerCustomSpeed = Integer.parseInt(argumente[0]);
					} catch (NumberFormatException e) {
						chat.nachrichtAnSpieler(sender, "Du musst eine Zahl eingeben!");
					}
				}
			}
		});
	}
	
	@Override
	public PaketManager paketManagerErstellen(Spieler spieler) {
		return new PongPaketManagerServer(this, spieler);
	}
	
	private void ballZurücksetzen() {
		// Ball zurück in die Mitte bewegen
		ballX = 300;
		ballY = 300;
		
		// zufällige neue Geschwindigkeit auswählen
		if (Helfer.zufallsZahl(2) == 0)
			ballGeschwindigkeitX = 1;
		else
			ballGeschwindigkeitX = -1;
		
		ballGeschwindigkeitY = Helfer.zufallsZahl(-5, 5);
	}
	
	@Override
	public void tick() {
		// ===========================
		// ==== Schläger-Position ====
		// ===========================
		schlägerLinks += schlägerCustomSpeed * geschwindigkeitLinks;
		schlägerRechts += schlägerCustomSpeed * geschwindigkeitRechts;
		
		// Schläger nicht aus dem Bildschirm bewegen lassen
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
		// ===== Gewonnen-Timer ======
		// ===========================
		if (pauseTicks >= 0) {
			pauseTicks--;
		}
		if (pauseTicks == 0) {
			ballZurücksetzen();
			pauseTicks = -1;
		}
		
		// ===========================
		// ====== Ball-Position ======
		// ===========================
		ballX += ballCustomSpeed * ballGeschwindigkeitX;
		ballY += ballGeschwindigkeitY;
		
		// Ball an oberem und unterem Rand abprallen lassen
		if (ballY < 0 || ballY > PongBeschreibung.BILDSCHIRM_HÖHE) {
			ballGeschwindigkeitY *= -1;
		}
		
		// auf Schläger-Kollision testen
		if (pauseTicks <= 0) {
			if (ballX > PongBeschreibung.GRENZE_RECHTS) {
				if (schlägerBerührtBall(schlägerRechts)) {
					// --> rechter Schläger hat getroffen
					ballX = PongBeschreibung.GRENZE_RECHTS;
					ballGeschwindigkeitX = -1;
					
					int yDiff = ballY - schlägerRechts;
					ballGeschwindigkeitY = (20 * yDiff) / PongBeschreibung.SCHLÄGER_HÖHE;
				}
				else {
					// --> rechter Schläger hat verfehlt
					scoreboard.punktHinzufügen(links);
					pauseTicks = 20;
				}
			}
			
			else if (ballX < PongBeschreibung.GRENZE_LINKS) {
				if (schlägerBerührtBall(schlägerLinks)) {
					// --> linker Schläger hat getroffen
					ballX = PongBeschreibung.GRENZE_LINKS;
					ballGeschwindigkeitX = 1;
					
					int yDiff = ballY - schlägerLinks;
					ballGeschwindigkeitY = (20 * yDiff) / PongBeschreibung.SCHLÄGER_HÖHE;
				}
				else {
					// --> linker Schläger hat verfehlt
					scoreboard.punktHinzufügen(rechts);
					pauseTicks = 20;
				}
			}
		}
		
		// Clients über Ballposition informieren
		packetAnAlle(new PacketBallBewegen(ballX, ballY));
	}
	
	/**
	 * Prüft anhand der Y-Position, ob ein Schläger den Ball getroffen hat
	 * 
	 * @param schläger die Y-Position des Mittelpunkts des Schlägers
	 * @return true, wenn getroffen, ansonsten false
	 */
	private boolean schlägerBerührtBall(int schläger) {
		return (ballY >= schläger - (PongBeschreibung.SCHLÄGER_HÖHE / 2) && ballY <= schläger + (PongBeschreibung.SCHLÄGER_HÖHE / 2));
	}
	
	/**
	 * Setzt die aktuelle Geschwindigkeit eines Spielers, wenn dessen gedrückte Tasten sich verändert haben.
	 * 
	 * @param spieler der veränderte Spieler
	 * @param geschwindigkeit die neue Geschwindigkeit: -1 für nach oben bewegend, 0 für stehend, 1 für nach unten bewegend
	 */
	public void setSpielerGeschwindigkeit(Spieler spieler, int geschwindigkeit) {
		if (spieler == links) {
			geschwindigkeitLinks = geschwindigkeit;
		}
		else if (spieler == rechts) {
			geschwindigkeitRechts = geschwindigkeit;
		}
	}
	
}
