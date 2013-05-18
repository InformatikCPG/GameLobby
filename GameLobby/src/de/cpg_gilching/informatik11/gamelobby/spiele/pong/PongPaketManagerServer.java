package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielTaste;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class PongPaketManagerServer implements PaketManager {
	
	private PongSpielServer pong;
	private Spieler spieler;
	
	private boolean obenGedrückt = false;
	private boolean untenGedrückt = false;
	
	
	public PongPaketManagerServer(PongSpielServer pong, Spieler spieler) {
		this.pong = pong;
		this.spieler = spieler;
	}
	
	public void verarbeiten(PacketSpielTaste packet) {
		if (packet.tastencode == KeyEvent.VK_UP) {
			obenGedrückt = packet.zustand;
		}
		else if (packet.tastencode == KeyEvent.VK_DOWN) {
			untenGedrückt = packet.zustand;
		}
		
		if (obenGedrückt && !untenGedrückt) {
			pong.setSpielerGeschwindigkeit(spieler, -1);
		}
		else if (!obenGedrückt && untenGedrückt) {
			pong.setSpielerGeschwindigkeit(spieler, 1);
		}
		else {
			pong.setSpielerGeschwindigkeit(spieler, 0);
		}
	}
	
}
