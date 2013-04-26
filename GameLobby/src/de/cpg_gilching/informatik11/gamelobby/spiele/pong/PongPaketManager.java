package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import de.cpg_gilching.informatik11.gamelobby.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.PaketLexikon.Paket;

public class PongPaketManager extends PaketManager {
	
	public static class PaketSchlägerBewegen extends Paket {
	}
	
	private PongSpielServer spiel;
	
	public PongPaketManager(PongSpielServer spiel) {
		this.spiel = spiel;
	}
	
	public void empfangen(PaketSchlägerBewegen p) {
		spiel.linkenSchlägerBewegen(1);
		spiel.rechtenSchlägerBewegen(1);
	}
	
}
