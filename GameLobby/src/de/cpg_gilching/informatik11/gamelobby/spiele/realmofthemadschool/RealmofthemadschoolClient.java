package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class RealmofthemadschoolClient extends ClientSpiel implements PaketManager {

	Image Hintergrund;
	EntityRenderVerwaltung entities;

	@Override
	protected void starten() {
		leinwandAktivieren(600, 600);
		setPaketManager(this);
		netzwerkTasteRegistrieren(KeyEvent.VK_W);
		netzwerkTasteRegistrieren(KeyEvent.VK_A);
		netzwerkTasteRegistrieren(KeyEvent.VK_S);
		netzwerkTasteRegistrieren(KeyEvent.VK_D);
		netzwerkTasteRegistrieren(KeyEvent.VK_SPACE);
		
		// Alias-Tasten
		netzwerkTasteRegistrieren(KeyEvent.VK_UP, KeyEvent.VK_W);
		netzwerkTasteRegistrieren(KeyEvent.VK_LEFT, KeyEvent.VK_A);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN, KeyEvent.VK_S);
		netzwerkTasteRegistrieren(KeyEvent.VK_RIGHT, KeyEvent.VK_D);

		Hintergrund = Helfer.bildLaden("realm/realmbackground.png");
		
		entities = new EntityRenderVerwaltung();
	}
	

	@Override
	public void leinwandRendern(Graphics2D g) {
		g.drawImage(Hintergrund, 0, 0, null);
		entities.entitiesRendern(g);
	}
	
	public void verarbeiten(PacketEntityNeu packet) {
		entities.neueEntity(packet);
	}
	
	public void verarbeiten(PacketEntityBewegen packet) {
		entities.entityBewegen(packet);
	}
	
	public void verarbeiten(PacketEntityTot packet) {
		entities.entityEntfernen(packet);
	}

}
