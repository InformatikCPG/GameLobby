package de.cpg_gilching.informatik11.gamelobby.shared;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

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
	 * Zeigt eine Meldung in einem einzelnen Fenster an.
	 * <p/>
	 * Diese Methode blockiert so lange, bis das Fenster bestätigt wurde.
	 * 
	 * @param meldung der Text, der angezeigt werden soll
	 * @param istFehler true, wenn der Text eine Fehlermeldung ist, ansonsten false
	 */
	public static void meldungAnzeigen(String meldung, boolean istFehler) {
		if (istFehler) {
			JOptionPane.showMessageDialog(null, meldung, "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(null, meldung);
		}
	}
	
	/**
	 * Joins all elements of an Iterable with a given delimiter between them.
	 * 
	 * @param collection The collection of elements to join
	 * @param delimiter The delimiter to use between elements
	 * @return The joined string as a CharSequence
	 */
	public static CharSequence verketten(Iterable<?> collection, String delimiter) {
		StringBuilder output = new StringBuilder();
		
		if (collection == null) {
			output.append("null");
		}
		else {
			for (Object s : collection) {
				if (output.length() > 0)
					output.append(delimiter);
				output.append(s);
			}
		}
		return output;
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
	 * Gibt ein zufälliges Element aus einer Liste zurück. Das Element kann optional automatisch aus der Liste entfernt werden.
	 * 
	 * @param liste die Liste, aus der das Element genommen werden soll
	 * @param autoLöschen true, wenn das gegebene Element automatisch entfernt werden soll
	 * @return ein zufälliges Element
	 * @throws IllegalArgumentException wenn die übergebene Liste leer oder null ist
	 */
	public static <T> T zufallsElement(List<T> liste, boolean autoLöschen) {
		if (liste == null || liste.isEmpty())
			throw new IllegalArgumentException("leere Liste");
		
		int index = Helfer.zufallsZahl(liste.size());
		
		if (autoLöschen) {
			return liste.remove(index);
		}
		else {
			return liste.get(index);
		}
	}
	
	/**
	 * Wartet für die gegebene Anzahl an Millisekunden.
	 */
	public static void warten(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
	
	public static void alsSwingTask(Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
	}
	
	public static BufferedImage bildLaden(String name) {
		try {
			return ImageIO.read(Helfer.class.getResourceAsStream("/" + name));
		} catch (IOException e) {
			System.err.println("Fehler beim Laden von Bild " + name);
			e.printStackTrace();
			
			return new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		}
	}
	
	public static double clamp(double val, double min, double max) {
		return Math.max(Math.min(val, max), min);
	}
	
	public static float clamp(float val, float min, float max) {
		return Math.max(Math.min(val, max), min);
	}
}
