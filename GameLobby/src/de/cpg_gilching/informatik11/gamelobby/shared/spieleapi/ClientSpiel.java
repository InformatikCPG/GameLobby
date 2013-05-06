package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.awt.Graphics2D;

import de.cpg_gilching.informatik11.gamelobby.client.SpielOberfläche;

public abstract class ClientSpiel {
	
	public static SpielOberfläche konstruktorSpielView = null;
	
	protected PaketManager paketManager;
	
	private SpielOberfläche spielView;
	
	public final void viewZuweisen(SpielOberfläche spielView) {
		this.spielView = spielView;
	}
	
	protected void leinwandAktivieren(int breite, int höhe) {
		// wenn im Konstruktor aufgerufen, muss das statische Attribut genommen werden, da noch kein Wert zugewiesen werden konnte
		if (konstruktorSpielView != null)
			spielView = konstruktorSpielView;
		
		spielView.canvasHinzufügen(breite, höhe);
	}
	
	public abstract void rendern(Graphics2D g);
	
}
