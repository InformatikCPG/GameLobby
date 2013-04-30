package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.shared.AdapterPaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;


public class PaketLexikon {
	
	private AdapterPaketLexikon internesLexikon;
	private Map<Class<? extends Packet>, PaketManager> managerNachKlasse = new HashMap<Class<? extends Packet>, PaketManager>();
	
	public PaketLexikon(AdapterPaketLexikon internesLexikon) {
		this.internesLexikon = internesLexikon;
	}
	
	/**
	 * Meldet einen Paket-Typ beim Lexikon an, sodass dieses später vom gegebenen {@link PaketManager} entgegengenommen werden kann.
	 * 
	 * @param klasse die Klasse des Pakets
	 * @param manager der {@link PaketManager}, der solche Pakete verarbeiten soll
	 */
	public void spielPaketAnmelden(Class<? extends Packet> klasse, PaketManager manager) {
		internesLexikon.anmelden(klasse);
		managerNachKlasse.put(klasse, manager);
	}
	
	/**
	 * Verarbeitet ein {@link Packet}, das einem Spiel zugeordnet ist.
	 * 
	 * @param packet das {@link Packet}, das verarbeitet werden soll
	 * 
	 * @return true, wenn tatsächlich ein Spiel für das Paket gefunden wurde, ansonsten falsch
	 */
	public boolean spielPaketVerarbeiten(Packet packet) {
		PaketManager manager = managerNachKlasse.get(packet.getClass());
		
		if (manager == null) {
			return false;
		}
		else {
			try {
				Method m = manager.getClass().getDeclaredMethod("verarbeiten", packet.getClass());
				m.invoke(manager, packet);
			} catch (NoSuchMethodException e) {
				System.err.println("Spiel-Paket wurde vom PaketManager nicht entgegengenommen: " + packet.getClass().getSimpleName());
			} catch (InvocationTargetException e) {
				System.err.println("Exception beim Verarbeiten des Spiel-Pakets: " + packet.getClass().getSimpleName());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return true;
		}
	}
	
	public AdapterPaketLexikon getInternesLexikon() {
		return internesLexikon;
	}
	
}
