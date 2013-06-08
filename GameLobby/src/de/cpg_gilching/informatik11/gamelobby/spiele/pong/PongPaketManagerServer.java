package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielTaste;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

/**
 * Diese Klasse ist für das Empfangen von Pong-Paketen des Clients zuständig.
 * <p/>
 * Dies ist hier nur das Paket {@link PacketSpielTaste}, mit dem die Tastendrucke von Oben und Unten signalisiert werden.<br>
 * Je nach gedrückter Taste wird die Geschwindigkeit des Schlägers am Server gesetzt.
 */
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
		
		// Spielergeschwindigkeit je nach gedrückten Tasten setzen
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
