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
		
		//Registrieren der Tasten
		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_LEFT);
		netzwerkTasteRegistrieren(KeyEvent.VK_RIGHT);
		
		// Alias-Tasten
		netzwerkTasteRegistrieren(KeyEvent.VK_W, KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_S, KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_A, KeyEvent.VK_LEFT);
		netzwerkTasteRegistrieren(KeyEvent.VK_D, KeyEvent.VK_RIGHT);
	}
	
	//Rendern der Oberfläche
	@Override
	public void leinwandRendern(Graphics2D g) {
		//Teilen der Oberfläche in 10*10 große Bereiche -> wenn "feldMatrix[j][i]" nicht "null" ist, dann mit der gespeicherten Farbe füllen
		for (int i = 0; i < 60; i++) {
			for (int j = 0; j < 60; j++) {
				if (feldMatrix[j][i] != null) {
					g.setColor(feldMatrix[j][i]);
					g.fillRect(j * 10, i * 10, 10, 10);
				}
			}
		}
		
		//Anzeigen einer Nachricht, wenn "msg" nicht "null" und nicht "leer" ist
		if(msg != null && !msg.isEmpty()) {			
			g.setColor(Color.GREEN);
			g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,30));
			int schriftBreite = g.getFontMetrics().stringWidth(msg);
			g.drawString(msg, (600-schriftBreite) / 2, 335);
		}
	}

	//Wenn ein Packet "PacketFeldSetzen" ankommt den x- und den y-Wert auslesen und die Farbe bei "feldMatrix[x][y]" speichern
	public void verarbeiten(PacketFeldSetzen packet) {
		if (packet.x >= 0 && packet.x < 60 && packet.y >= 0 && packet.y < 60) {
			if (packet.farbe != -1) {
				feldMatrix[packet.x][packet.y] = new Color(packet.farbe);
			} else {
				feldMatrix[packet.x][packet.y] = null;
			}
		}
	}
	
	//Wenn ein Packet "PacketReset" ankommt die Oberfläche komplett zurücksetzen
	public void verarbeiten(PacketNachrichtSenden packet) {
		msg = packet.msg;
	}
	
	//Wenn ein Packet "PacketNachrichtSenden" ankommt den Wert von "msg" zu "msg" übergeben
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
