package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielerAnzahl;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class KeyMadnessClient extends ClientSpiel implements PaketManager{

	TargetRenderVerwaltung targets;
	Image Hintergrund;
	KeyMadnessPunkteDaten daten; // wird beim Empfang von PacketSpielerAnzahl initialisiert
	Image checkpoint;
	
	
	
	@Override
	protected void starten() {
		targets = new TargetRenderVerwaltung(this);
		leinwandAktivieren(600, 600);
		setPaketManager(this);
		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_LEFT);
		netzwerkTasteRegistrieren(KeyEvent.VK_RIGHT);
		Hintergrund = Helfer.bildLaden("keymadness/background.png");
		checkpoint = Helfer.bildLaden("keymadness/checkpoint.png");
	}

	@Override
	public void leinwandRendern(Graphics2D g) {
		g.drawImage(Hintergrund, 0, 0, null);
		for(int i = 0; i <= daten.pfade.length - 1; i++){
			for(int j = 0; j <= daten.pfade[i].length -2; j++){
				g.setColor(Color.white);
				g.setStroke(new BasicStroke(5));
				g.drawLine(daten.pfade[i][j].x, daten.pfade[i][j].y, daten.pfade[i][j+1].x, daten.pfade[i][j+1].y);
			}
		}
		targets.targetsRendern(g);
		for(int i = 0; i <= daten.checkpoints.length - 1; i++){
			g.drawImage(checkpoint, daten.checkpoints[i].x - 35, daten.checkpoints[i].y - 35, null);
			g.setColor(new Color(1, 0, 0, 0.5f));
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
	
	public void verarbeiten(PacketSpielerAnzahl packet) {
		daten = new KeyMadnessPunkteDaten(packet.anzahl);
	}

}
