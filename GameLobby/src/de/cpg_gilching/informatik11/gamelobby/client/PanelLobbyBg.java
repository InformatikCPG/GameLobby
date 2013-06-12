package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

/**
 * Ein simples {@link JPanel}, das ein Bild im Hintergrund anzeigt.
 */
public class PanelLobbyBg extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Image bgBild = Helfer.bildLaden("lobby_hintergrund.png");
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(bgBild, 0, 0, null);
	}
	
}
