package de.cpg_gilching.informatik11.gamelobby.shared;

import java.awt.Frame;

// Watchdog-Klasse, auf Deutsch ;)
public class Wachhund extends Thread {
	
	private Frame fenster;
	
	/**
	 * Erstellt einen Wachhund, der auf das Programmende prüft.
	 */
	public Wachhund() {
		this(null);
	}
	
	/**
	 * Erstellt einen Wachhund, der auf das Schließen eines Fensters prüft.
	 */
	public Wachhund(Frame fenster) {
		this.fenster = fenster;
		setDaemon(true);
	}
	
	@Override
	public void run() {
		if (fenster == null) {
			// das Programm sollte innerhalb von 3 Sekunden beendet sein (wir selbst sind ein Daemon-Thread)
			Helfer.warten(3000L);
			
			// wenn wir immer noch laufen, trat ein Fehler auf und wir müssen mit Gewalt beendet werden
			System.err.println("Programm konnte nicht richtig beendet werden!");
			System.exit(1);
		}
		else {
			// das zu überwachende Fenster sollte innerhalb von 2 Sekunden geschlossen werden
			for (int i = 0; i < 10; i++) {
				Helfer.warten(200L);
				if (!fenster.isDisplayable())
					return; // Wachhund abbrechen --> es wurde geschlossen
			}
			
			// wenn es hier immer noch offen ist, wird es zwangsweise beendet
			if (fenster.isDisplayable()) {
				System.err.println("Fenster \"" + fenster.getTitle() + "\" konnte nicht richtig geschlossen werden!");
				fenster.dispose();
			}
		}
	}
	
}
