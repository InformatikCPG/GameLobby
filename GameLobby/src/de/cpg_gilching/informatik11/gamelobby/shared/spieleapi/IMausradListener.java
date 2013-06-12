package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.awt.event.MouseWheelEvent;

/**
 * Interface, um das Scrollen des Mausrads empfangen zu können.
 */
public interface IMausradListener {
	
	/**
	 * Wird aufgerufen, wenn das Mausrad gescrollt wurde.
	 * 
	 * @param event das Java-Event
	 */
	public void onMausGescrollt(MouseWheelEvent event);
	
}
