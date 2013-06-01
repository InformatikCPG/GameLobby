package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.IMausListener;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class KeyMadnessClient extends ClientSpiel implements PaketManager{

	public int[] felder;
	Image Hintergrund;
	
	@Override
	protected void starten() {
		leinwandAktivieren(600, 600);
		setPaketManager(this);
		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_LEFT);
		netzwerkTasteRegistrieren(KeyEvent.VK_RIGHT);
		felder = new int[9];
		Hintergrund = Helfer.bildLaden("tictactoe/Hintergrund.png");
	}

	@Override
	public void leinwandRendern(Graphics2D g) {
		g.drawImage(Hintergrund, 0, 0, null);
		
	}

	private void feldRendern(Graphics2D g, int feld, int x, int y) {
		if (felder[feld] == 1) {
			g.drawImage(X, x, y, null);
		} else if (felder[feld] == 2) {
			g.drawImage(O, x, y, null);
		}
	}
	public void verarbeiten(PacketFeldSetzen packet){
		felder[packet.feld] = packet.wert;
		}

}
