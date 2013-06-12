package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class SnakeSpielClient extends ClientSpiel implements PaketManager {

	private Color[][] feldMatrix;
	private String msg;

	@Override
	protected void starten() {
		leinwandAktivieren(600, 600);
		setPaketManager(this);
		feldMatrix = new Color[60][60];
		msg = null;

		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_LEFT);
		netzwerkTasteRegistrieren(KeyEvent.VK_RIGHT);
	}

	@Override
	public void leinwandRendern(Graphics2D g) {
		for (int i = 0; i < 60; i++) {
			for (int j = 0; j < 60; j++) {
				if (feldMatrix[j][i] != null) {
					g.setColor(feldMatrix[j][i]);
					g.fillRect(j * 10, i * 10, 10, 10);
				}
			}
		}
		
		if(msg != null && !msg.isEmpty()) {			
			g.setColor(Color.GREEN);
			g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,30));
			int schriftBreite = g.getFontMetrics().stringWidth(msg);
			g.drawString(msg, (600-schriftBreite) / 2, 335);
		}
	}

	public void verarbeiten(PacketFeldSetzen packet) {
		if (packet.x >= 0 && packet.x < 60 && packet.y >= 0 && packet.y < 60) {
			if (packet.farbe != -1) {
				feldMatrix[packet.x][packet.y] = new Color(packet.farbe);
			} else {
				feldMatrix[packet.x][packet.y] = null;
			}
		}
	}
	
	public void verarbeiten(PacketNachrichtSenden packet) {
		msg = packet.msg;
	}
	
	public void verarbeiten(PacketReset packet) {
		//alle Felder zurücksetzen
		for (int i = 0; i < 60; i++) {
			for (int j = 0; j < 60; j++) {
				feldMatrix[i][j] = null;
			}
		}
		
		//Nachricht zurücksetzen
		msg = "";
	}
}
