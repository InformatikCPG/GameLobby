package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;


public class PongSpielServer extends ServerSpiel {
	
	private Spieler links;
	private Spieler rechts;
	
	private int geschwindigkeitLinks = 0;
	private int geschwindigkeitRechts = 0;
	
	private int posLinks = 300;
	private int posRechts = 300;
	
	@Override
	public void starten() {
		links = teilnehmer.get(0);
		rechts = teilnehmer.get(1);
		
		packetAnAlle(new PacketSchlägerBewegen(posLinks, posRechts));
	}
	
	@Override
	public PaketManager paketManagerErstellen(Spieler spieler) {
		return new PongPaketManagerServer(this, spieler);
	}
	
	@Override
	public void tick() {
		posLinks += 10 * geschwindigkeitLinks;
		posRechts += 10 * geschwindigkeitRechts;
		
		if (posLinks > 500)
			posLinks = 500;
		else if (posLinks < 100)
			posLinks = 100;
		if (posRechts > 500)
			posRechts = 500;
		else if (posRechts < 100)
			posRechts = 100;
		
		// wenn sich etwas bewegt hat
		if (geschwindigkeitLinks != 0 || geschwindigkeitRechts != 0) {
			packetAnAlle(new PacketSchlägerBewegen(posLinks, posRechts));
		}
	}
	
	public void setSpielerGeschwindigkeit(Spieler spieler, int geschwindigkeit) {
		if (spieler == links) {
			geschwindigkeitLinks = geschwindigkeit;
		}
		else if (spieler == rechts) {
			geschwindigkeitRechts = geschwindigkeit;
		}
	}
	
}
