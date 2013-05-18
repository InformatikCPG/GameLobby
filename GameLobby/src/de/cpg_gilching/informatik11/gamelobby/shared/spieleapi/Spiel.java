package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;



public abstract class Spiel {
	
	private PaketManager paketManager = null;
	
	public Spiel() {
	}
	
	public final void setPaketManager(PaketManager manager) {
		paketManager = manager;
	}
	
	public final PaketManager getPaketManager() {
		return paketManager;
	}
	
}
