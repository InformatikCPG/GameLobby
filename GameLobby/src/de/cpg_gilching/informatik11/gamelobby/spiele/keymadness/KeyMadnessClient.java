package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class KeyMadnessClient extends ClientSpiel implements PaketManager{

	TargetRenderVerwaltung targets;
	Image Hintergrund;
	KeyMadnessPunkteDaten daten;
	
	
	@Override
	protected void starten() {
		targets = new TargetRenderVerwaltung(this);
		leinwandAktivieren(600, 600);
		setPaketManager(this);
		daten = new KeyMadnessPunkteDaten(2);
		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_LEFT);
		netzwerkTasteRegistrieren(KeyEvent.VK_RIGHT);
		Hintergrund = Helfer.bildLaden("keymadness/Hintergrund.png");
	}

	@Override
	public void leinwandRendern(Graphics2D g) {
		g.drawImage(Hintergrund, 0, 0, null);
		for(int i = 0; i <= daten.punkte.length - 2; i++){
			g.setColor(Color.white);
			g.setStroke(new BasicStroke(5));
			g.drawLine(daten.punkte[i].x, daten.punkte[i].y, daten.punkte[i+1].x, daten.punkte[i+1].y);
		}
		targets.targetsRendern(g);
		for(int i = 0; i <= daten.checkpoints.length - 1; i++){
			g.setColor(Color.white);
			g.drawRect(daten.checkpoints[i].x - 30, daten.checkpoints[i].y - 30, 60, 60);
		}
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
