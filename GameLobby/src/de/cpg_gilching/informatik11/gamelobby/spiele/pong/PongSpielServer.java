package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.server.Spieler;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielTaste;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;


public class PongSpielServer extends ServerSpiel implements PaketManager {
	
	private Spieler links;
	private Spieler rechts;
	
	private int richtungLinks = 0;
	private int richtungRechts = 0;
	
	private int posLinks = 300;
	private int posRechts = 300;
	
	@Override
	public void starten() {
		links = teilnehmer.get(0);
		rechts = teilnehmer.get(1);
		
		setPaketManager(this);
	}
	
	@Override
	public void tick() {
		posLinks += 5 * richtungLinks;
		posRechts += 5 * richtungRechts;
		
		if (posLinks > 500)
			posLinks = 500;
		else if (posLinks < 100)
			posLinks = 100;
		if (posRechts > 500)
			posRechts = 500;
		else if (posRechts < 100)
			posRechts = 100;
		
		// wenn sich etwas bewegt hat
		if (richtungLinks != 0 || richtungRechts != 0) {
			packetAnAlle(new PacketSchlägerBewegen(posLinks, posRechts));
		}
	}
	
	public void verarbeiten(Spieler spieler, PacketSpielTaste packet) {
		int änderung;
		if (packet.tastencode == KeyEvent.VK_UP) {
			änderung = -1;
		}
		else {
			änderung = 1;
		}
		
		if (!packet.zustand) {
			änderung *= -1;
		}
		
		
		if (spieler == links) {
			richtungLinks += änderung;
		}
		else if (spieler == rechts) {
			richtungRechts += änderung;
		}
	}
	
}
