package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

public abstract class SpielBeschreibung {
	
	public abstract String bezeichnungGeben();
	
	public abstract int tickrateGeben();
	
	public void paketeAnmelden(PaketLexikon lexikon) {
	}
	
}