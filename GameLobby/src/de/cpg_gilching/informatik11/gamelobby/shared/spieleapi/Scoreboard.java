package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.cpg_gilching.informatik11.gamelobby.server.LobbySpieler;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielTeilnehmerDaten;

/**
 * Eine API für {@link ServerSpiel}e, um einfach den Punktestand der teilnehmenden Spieler zu verwalten.<br>
 * Es wird unterschieden zwischen zwei Arten von Punkten:
 * <p>
 * 1. "Echte" Punkte. Diese Punkte werden regulär in der Liste angezeigt. Wenn diese Punkte das im Spiel eingestellte Punktelimit überschreiten, hat der Spieler das Spiel gewonnen. Diese Punkte können auch negativ werden.
 * </p>
 * <p>
 * 2. "Vorbereitete" Punkte. Diese Punkte werden neben den echten Punkten angezeigt und dienen als Vorschau für Punkte, die der Spieler nach der Runde bekommen wird.<br>
 * Diese werden zum Zeitpunkt gesetzt, wenn sie feststehen (z.B. da der Spieler die Runde an x.ter Stelle verloren hat) und werden in der Regel am Ende jeder Runde angewendet. Dann werden sie zum echten Score hinzugezählt.
 * </p>
 */
public class Scoreboard {
	
	/**
	 * Ein Wert, der angibt, dass keine vorbereiteten Punkte angezeigt werden sollen. Standard-Wert für alle Spieler
	 */
	public static final int NICHTS = Integer.MIN_VALUE;

	private ServerSpiel spiel;
	private Map<Spieler, Integer> punkte = new HashMap<Spieler, Integer>();
	private Map<Spieler, Integer> tempPunkte = new HashMap<Spieler, Integer>();
	private Map<Spieler, Integer> farben = new HashMap<Spieler, Integer>();
	
	public Scoreboard(ServerSpiel spiel) {
		this.spiel = spiel;
		
		for (Spieler s : spiel.getTeilnehmer()) {
			punkte.put(s, 0);
			farben.put(s, -1);
		}
	}
	
	/**
	 * Zeigt zusätzliche Punkte für den Spieler an, die aber noch nicht dazugerechnet werden.<br>
	 * Mit der Methode {@link #punkteAnwenden()} werden diese zum normalen Stand dazugerechnet.
	 * 
	 * @param spieler der betreffende Spieler
	 * @param wert die Anzahl der Punkte, die vorbereitet werden sollen; kann {@link Scoreboard#NICHTS} sein
	 */
	public void punkteVorbereiten(Spieler spieler, int wert) {
		if (wert == NICHTS)
			tempPunkte.remove(spieler);
		else
			tempPunkte.put(spieler, wert);

		datenSenden(spieler);
	}
	
	/**
	 * Zählt alle vorbereiteten Punkte zum richtigen Punktestand hinzu und löscht sie.
	 */
	public void punkteAnwenden() {
		for (Entry<Spieler, Integer> entry : new ArrayList<Entry<Spieler, Integer>>(tempPunkte.entrySet())) {
			tempPunkte.remove(entry.getKey());
			punkteÄndern(entry.getKey(), entry.getValue());
		}
		
		gewinnPrüfen();
	}
	
	/**
	 * Gibt dem Spieler 1 Punkt.
	 */
	public void punktHinzufügen(Spieler spieler) {
		punkteÄndern(spieler, 1);
	}
	
	/**
	 * Ändert die Punktezahl eines Spielers um den gegebenen Wert.
	 * Kann auch negativ sein.
	 */
	public void punkteÄndern(Spieler spieler, int anzahl) {
		if (!punkte.containsKey(spieler))
			throw new IllegalArgumentException("Spieler nicht Teil des Spiels");
		
		int neueAnzahl = punkte.get(spieler) + anzahl;
		punkte.put(spieler, neueAnzahl);
		
		datenSenden(spieler);
		gewinnPrüfen();
	}
	
	/**
	 * Setzt die Anzeigefarbe eines Spielers auf dem Scoreboard.
	 * 
	 * @param spieler der Spieler, dessen Farbe geändert werden soll
	 * @param farbe die Farbe als Java-{@link Color}
	 */
	public void anzeigefarbeSetzen(Spieler spieler, Color farbe) {
		anzeigefarbeSetzen(spieler, farbe.getRGB());
	}
	
	/**
	 * Setzt die Anzeigefarbe eines Spielers auf dem Scoreboard.
	 * 
	 * @param spieler der Spieler, dessen Farbe geändert werden soll
	 * @param farbe die Farbe im RRGGBB-format
	 */
	public void anzeigefarbeSetzen(Spieler spieler, int farbe) {
		if (!farben.containsKey(spieler))
			throw new IllegalArgumentException("Spieler nicht Teil des Spiels");
		
		farben.put(spieler, farbe);
		datenSenden(spieler);
	}
	
	/**
	 * Gibt den aktuellen Punktestand des Spielers zurück.
	 */
	public int getPunkte(Spieler spieler) {
		if (!punkte.containsKey(spieler))
			throw new IllegalArgumentException("Spieler nicht Teil des Spiels");
		
		return punkte.get(spieler);
	}
	
	/**
	 * Gibt die aktuelle Anzeigefarbe des Spielers zurück.
	 */
	public int getAnzeigefarbe(Spieler spieler) {
		if (!farben.containsKey(spieler))
			throw new IllegalArgumentException("Spieler nicht Teil des Spiels");
		
		return farben.get(spieler);
	}


	private void datenSenden(Spieler spieler) {
		for (Spieler anderer : spiel.getTeilnehmer()) {
			Integer temp = tempPunkte.get(spieler);
			if (temp == null)
				temp = NICHTS;

			((LobbySpieler) anderer).packetSenden(new PacketSpielTeilnehmerDaten(spiel.getSpielId(), spieler.getName(), farben.get(spieler), punkte.get(spieler), temp));
		}
	}
	
	private void gewinnPrüfen() {
		for (Entry<Spieler, Integer> entry : punkte.entrySet()) {
			if (entry.getValue() >= spiel.getPunktelimit()) {
				Spieler gewinner = entry.getKey();
				spiel.spielEndeSounds(gewinner);
				spiel.beenden(String.format("%s hat das Spiel mit %d Punkten gewonnen!", gewinner.getName(), entry.getValue()));
				break;
			}
		}
	}

}
