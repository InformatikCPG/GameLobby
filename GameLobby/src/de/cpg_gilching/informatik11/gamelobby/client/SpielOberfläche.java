package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JPanel;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;

public class SpielOberfläche extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Canvas leinwand = null;
	
	public SpielOberfläche() {
		setBounds(0, 0, 600, 600);
		setLayout(null);
	}
	
	public void canvasHinzufügen(int breite, int höhe) {
		int xoffset = (600 - breite) / 2;
		int yoffset = (600 - höhe) / 2;
		
		leinwand = new Canvas();
		leinwand.setBounds(xoffset, yoffset, breite, höhe);
		add(leinwand);
	}
	
	public void canvasRendern(ClientSpiel spiel) {
		BufferStrategy bs = leinwand.getBufferStrategy();
		if (bs == null) {
			leinwand.createBufferStrategy(2);
			leinwand.requestFocus();
			return;
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, leinwand.getWidth(), leinwand.getHeight());
		
		spiel.rendern(g);
		
		g.dispose();
		bs.show();
	}
	
	public boolean hatCanvas() {
		return leinwand != null;
	}
	
}
