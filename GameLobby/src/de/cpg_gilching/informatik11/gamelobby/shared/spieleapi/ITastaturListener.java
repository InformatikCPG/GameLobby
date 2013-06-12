package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.awt.event.KeyEvent;

/**
 * Interface, um das Drücken und Loslassen von Tasten der Tastatur empfangen zu können.
 */
public interface ITastaturListener {
	
	/**
	 * Wird aufgerufen, wenn eine Taste gedrückt oder losgelassen wurde.
	 * 
	 * @param event das Java-Event
	 * @param zustand true, wenn die Taste gedrückt wurde, ansonsten false
	 */
	public void onTasteGeändert(KeyEvent event, boolean zustand);
	
}
