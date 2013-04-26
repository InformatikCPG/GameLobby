package de.cpg_gilching.informatik11.gamelobby;

public abstract class SpielBeschreibungServer extends SpielBeschreibung {
	
	public int minimalspielerGeben() {
		return 2;
	}
	
	public int maximalspielerGeben() {
		return -1;
	}
	
	public abstract ServerSpiel instanzErstellen();
	
}
