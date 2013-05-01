package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

public abstract class SpielBeschreibung {
	
	private int spielId = -1;
	
	public final void setSpielId(int spielId) {
		this.spielId = spielId;
	}
	
	public final int getSpielId() {
		return spielId;
	}
	
	public int minimalspielerGeben() {
		return 2;
	}
	
	public int maximalspielerGeben() {
		return -1;
	}
	
	public abstract String getBezeichnung();
	
	public abstract int tickrateGeben();
	
	public void paketeAnmelden(PaketLexikon lexikon) {
	}
	
	public abstract ClientSpiel clientInstanzErstellen();
	
	public abstract ServerSpiel serverInstanzErstellen();
	
}
