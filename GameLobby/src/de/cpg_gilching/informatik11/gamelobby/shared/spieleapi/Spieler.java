package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

/**
 * Stellt einen Spieler auf dem Server dar.<br>
 * Die Hashing-Methoden des Spielers müssen von Implementierungen richtig überschrieben werden und zwei verschiedene Spieler-Objekte geben nie den selben Namen zurück.
 */
public interface Spieler {
	
	/**
	 * Gibt den Namen dieses Spielers zurück. Dieser ist auf dem ganzen Server einzigartig und kann nicht doppelt existieren.
	 */
	public String getName();
	
}
