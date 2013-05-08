package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ITastaturListener;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;


public class PongSpielClient extends ClientSpiel implements PaketManager, ITastaturListener {
	
	private int links;
	private int rechts;
	
	@Override
	public void starten() {
		leinwandAktivieren(600, 600);
		setPaketManager(this);
		
		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		tasteRegistrieren(KeyEvent.VK_H, this);
	}
	
	@Override
	public void onTasteGeändert(KeyEvent event, boolean zustand) {
		// die H-Taste, die oben registriert wurde
		System.out.format("Pong: H wurde %s!%n", (zustand ? "gedrückt" : "losgelassen"));
	}
	
	@Override
	public void leinwandRendern(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(50, links, 20, 200);
		g.fillRect(530, rechts, 20, 200);
	}
	
	public void verarbeiten(PacketSchlägerBewegen packet) {
		links = packet.linkePosition;
		rechts = packet.rechtePosition;
	}
	
}
