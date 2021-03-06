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
		
		// Tasten werden festgelegt:
		netzwerkTasteRegistrieren(KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_LEFT);
		netzwerkTasteRegistrieren(KeyEvent.VK_RIGHT);
		// Alias-Tasten:
		netzwerkTasteRegistrieren(KeyEvent.VK_W, KeyEvent.VK_UP);
		netzwerkTasteRegistrieren(KeyEvent.VK_S, KeyEvent.VK_DOWN);
		netzwerkTasteRegistrieren(KeyEvent.VK_A, KeyEvent.VK_LEFT);
		netzwerkTasteRegistrieren(KeyEvent.VK_D, KeyEvent.VK_RIGHT);
		
		// Grafiken werden geladen:
		Hintergrund = Helfer.bildLaden("keymadness/background.png");
		checkpoint = Helfer.bildLaden("keymadness/checkpoint.png");
	}

	@Override
	public void leinwandRendern(Graphics2D g) {
		// Hintergrund wird gerendert:
		g.drawImage(Hintergrund, 0, 0, null);
		// Zweidimensionales Array der Punktedaten Pfade wird durchgegangen mit i(Spalte) und j(Zeile):
		for(int i = 0; i <= daten.pfade.length - 1; i++){
			for(int j = 0; j <= daten.pfade[i].length - 2; j++){
				g.setColor(Color.white);
				g.setStroke(new BasicStroke(5));
				// Linien werden zwischen jedem Punkt und dem nächsten in einer Spalte gezeichnet:
				g.drawLine(daten.pfade[i][j].x, daten.pfade[i][j].y, daten.pfade[i][j+1].x, daten.pfade[i][j+1].y);
			}
		}
		// Targets werden gerendert:
		targets.targetsRendern(g);
		// Checkpoints werden gerendert:
		for(int i = 0; i <= daten.checkpoints.length - 1; i++){
			g.drawImage(checkpoint, daten.checkpoints[i].x - 35, daten.checkpoints[i].y - 35, null);
			g.setStroke(new BasicStroke(10));
			// Je nach Stelle im Checkpoint Array werden die Farben verteilt:
			// (Die Farbe ist festgelegt durch den Checkpoint der für den jeweiligen Spieler überprüft wird:
			// Die Stelle des Spielers (in der Teilnehmerliste) und die Stelle des Checkpoints (im Checkpoint Array)
			// werden im Server beim Prüfen gleichgesetzt! Daher stimmt die Farbe überein!)
			switch (i) {
			case 0:
				g.setColor(new Color(1, 0, 0, 0.5f));
				g.drawRect(daten.checkpoints[i].x - 30, daten.checkpoints[i].y - 30, 60, 60);
				break;
			case 1:
				g.setColor(new Color(0, 1, 0, 0.5f));
				g.drawRect(daten.checkpoints[i].x - 30, daten.checkpoints[i].y - 30, 60, 60);
				break;
			case 2:
				g.setColor(new Color(0, 0, 1, 0.5f));
				g.drawRect(daten.checkpoints[i].x - 30, daten.checkpoints[i].y - 30, 60, 60);
				break;
			case 3:
				g.setColor(new Color(0, 1, 1, 0.5f));
				g.drawRect(daten.checkpoints[i].x - 30, daten.checkpoints[i].y - 30, 60, 60);
				break;
			}
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
