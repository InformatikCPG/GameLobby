package de.cpg_gilching.informatik11.gamelobby.shared;

// Watchdog-Klasse, auf Deutsch ;)
public class Wachhund extends Thread {
	
	public Wachhund() {
		setDaemon(true);
	}
	
	@Override
	public void run() {
		Helfer.warten(3000L);
		System.err.println("Programm konnte nicht richtig beendet werden!");
		System.exit(1);
	}
	
}
