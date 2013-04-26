package de.cpg_gilching.informatik11.gamelobby.shared;

import java.util.Random;

/**
 * Allgemeine Helfer-Klasse, die nützliche Methoden bereitstellt, um bestimmte Dinge zu vereinfachen.
 * <p/>
 * Nutzungsbeispiel:
 * 
 * <pre>
 * Helfer.warten(100); // wartet 100 ms
 * </pre>
 */
public class Helfer {
	
	/**
	 * Eine {@link Random}-Instanz zum internen Erzeugen von Zufallszahlen.
	 */
	private static final Random rand = new Random();
	
	private Helfer() {
	}
	
	/**
	 * Gibt eine zufällige Ganzzahl zwischen zwei Werten zurück.
	 * 
	 * @param min die kleinste mögliche Zahl (eingeschlossen)
	 * @param max die größte mögliche Zahl (ausgeschlossen)
	 */
	public static int zufallsZahl(int min, int max) {
		return rand.nextInt(max - min) + min;
	}
	
	/**
	 * Gibt eine zufällige Ganzzahl zwischen 0 und max zurück.
	 * 
	 * @param max die größte mögliche Zahl (ausgeschlossen)
	 */
	public static int zufallsZahl(int max) {
		return rand.nextInt(max);
	}
	
	/**
	 * Wartet für die gegebene Anzahl an Millisekunden.
	 */
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
}
