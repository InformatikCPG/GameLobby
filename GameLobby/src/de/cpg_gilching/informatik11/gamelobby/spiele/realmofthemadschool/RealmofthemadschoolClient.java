package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.IMausListener;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class RealmofthemadSchoolClient extends ClientSpiel implements PaketManager,
		IMausListener {

	Image Hintergrund;
	Image Player;

	@Override
	protected void starten() {
		leinwandAktivieren(600, 600);
		setPaketManager(this);
		netzwerkTasteRegistrieren(KeyEvent.VK_W);
		netzwerkTasteRegistrieren(KeyEvent.VK_A);
		netzwerkTasteRegistrieren(KeyEvent.VK_S);
		netzwerkTasteRegistrieren(KeyEvent.VK_D);
		Hintergrund = Helfer.bildLaden("Realmofthemadschool/Hintergrund.png");
		Player = Helfer.bildLaden("Realmofthemadschool/Player.png");

	}

	@Override
	
	
	

	@Override
	public void leinwandRendern(Graphics2D g) {
		g.drawImage(Hintergrund, 0, 0, null);
		g.drawImage(Player, x, y, null);
	}

	public void verarbeiten(PacketFeldSetzen packet){
		felder[packet.feld] = packet.wert;
		}

}
