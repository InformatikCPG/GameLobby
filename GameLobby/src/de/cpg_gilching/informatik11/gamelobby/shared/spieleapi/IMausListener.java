package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.awt.event.MouseEvent;

/**
 * Interface, um das Drücken und Loslassen von Maustasten empfangen zu können.
 */
public interface IMausListener {
	
	/**
	 * Wird aufgerufen, wenn eine Maustaste gedrückt oder losgelassen wurde.
	 * 
	 * @param event das Java-Event
	 * @param zustand true, wenn die Taste gedrückt wurde, ansonsten false
	 */
	public void onMaustasteGeändert(MouseEvent event, boolean zustand);
	
}
