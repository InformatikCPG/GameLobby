package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class KeyMadnessClient extends ClientSpiel implements PaketManager{

	TargetRenderVerwaltung targets;
	Image Hintergrund;
	
	@Override
	protected void starten() {
		targets = new TargetRenderVerwaltung(this);
		leinwandAktivieren(600, 600);
		setPaketManager(this);
		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_LEFT);
		netzwerkTasteRegistrieren(KeyEvent.VK_RIGHT);
		Hintergrund = Helfer.bildLaden("keymadness/Hintergrund.png");
	}

	@Override
	public void leinwandRendern(Graphics2D g) {
		g.drawImage(Hintergrund, 0, 0, null);
		targets.targetsRendern(g);
	}
	
	public void verarbeiten(PacketTargetNeu packet) {
		targets.neuesTarget(packet);
	}
	
	public void verarbeiten(PacketTargetBewegen packet) {
		targets.targetBewegen(packet);
	}
	
	public void verarbeiten(PacketTargetTot packet) {
		targets.targetEntfernen(packet);
	}

}
