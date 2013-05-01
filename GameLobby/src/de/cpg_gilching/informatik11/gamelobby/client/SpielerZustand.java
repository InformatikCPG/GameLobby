package de.cpg_gilching.informatik11.gamelobby.client;

public class SpielerZustand {
	
	private final String name;
	private boolean ausgewählt = false;
	private PanelSpielerEintrag anzeige = null;
	
	public SpielerZustand(String name) {
		this.name = name;
	}
	
	public synchronized void setAusgewählt(boolean ausgewählt) {
		this.ausgewählt = ausgewählt;
		
		if (anzeige != null) {
			anzeige.zustandVerändert();
		}
	}
	
	public synchronized boolean istAusgewählt() {
		return ausgewählt;
	}
	
	public String getName() {
		return name;
	}
	
	public synchronized void setAnzeige(PanelSpielerEintrag anzeige) {
		this.anzeige = anzeige;
	}
	
	public synchronized PanelSpielerEintrag getAnzeige() {
		return anzeige;
	}
	
}
