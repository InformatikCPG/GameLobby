package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.cpg_gilching.informatik11.gamelobby.server.LobbySpieler;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielPunkte;

public class Scoreboard {
	
	/**
	 * Ein Wert, der angibt, dass keine vorbereiteten Punkte angezeigt werden sollen. Standard
	 */
	public static final int NICHTS = Integer.MIN_VALUE;

	private ServerSpiel spiel;
	private Map<Spieler, Integer> punkte = new HashMap<Spieler, Integer>();
	private Map<Spieler, Integer> tempPunkte = new HashMap<Spieler, Integer>();
	
	public Scoreboard(ServerSpiel spiel) {
		this.spiel = spiel;
		
		for (Spieler s : spiel.getTeilnehmer()) {
			punkte.put(s, 0);
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

		punktestandSenden(spieler);
	}
	
	/**
	 * Zählt alle vorbereiteten Punkte zum richtigen Punktestand hinzu und löscht sie.
	 */
	public void punkteAnwenden() {
		for (Entry<Spieler, Integer> entry : new ArrayList<Entry<Spieler, Integer>>(tempPunkte.entrySet())) {
			tempPunkte.remove(entry.getKey());
			punkteÄndern(entry.getKey(), entry.getValue());
		}
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
		
		punktestandSenden(spieler);
	}
	
	/**
	 * Gibt den aktuellen Punktestand des Spielers zurück.
	 */
	public int getPunkte(Spieler spieler) {
		if (!punkte.containsKey(spieler))
			throw new IllegalArgumentException("Spieler nicht Teil des Spiels");
		
		return punkte.get(spieler);
	}
	
	private void punktestandSenden(Spieler spieler) {
		for (Spieler anderer : spiel.getTeilnehmer()) {
			Integer temp = tempPunkte.get(spieler);
			if (temp == null)
				temp = NICHTS;

			((LobbySpieler) anderer).packetSenden(new PacketSpielPunkte(spiel.getSpielId(), spieler.getName(), punkte.get(spieler), temp));
		}
	}

}