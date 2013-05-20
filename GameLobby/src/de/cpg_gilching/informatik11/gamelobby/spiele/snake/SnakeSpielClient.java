package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.*;

public class SnakeSpielClient extends ClientSpiel implements PaketManager {

	private Color[][] feldMatrix;

	@Override
	protected void starten() {
		leinwandAktivieren(700, 700);
		setPaketManager(this);
		feldMatrix = new Color[70][70];

		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_LEFT);
		netzwerkTasteRegistrieren(KeyEvent.VK_RIGHT);
	}

	@Override
	public void leinwandRendern(Graphics2D g) {
		for (int i = 0; i < 70; i++) {
			for (int j = 0; j < 70; j++) {
				if (feldMatrix[j][i] != null) {
					g.setColor(feldMatrix[j][i]);
					g.fillRect(j * 10, i * 10, 10, 10);
				}
			}
		}
	}

	public void verarbeiten(PacketFeldSetzen packet) {
		if (packet.x >= 0 && packet.x < 70 && packet.y >= 0 && packet.y < 70) {
			if (packet.farbe != -1) {
				feldMatrix[packet.x][packet.y] = new Color(packet.farbe);
			} else {
				feldMatrix[packet.x][packet.y] = null;
			}
		}

	}
}
