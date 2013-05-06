package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import java.awt.Color;
import java.awt.Graphics2D;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;


public class PongSpielClient extends ClientSpiel {
	
	public PongSpielClient() {
		leinwandAktivieren(600, 600);
	}
	
	@Override
	public void rendern(Graphics2D g) {
		g.setColor(Color.white);
		g.drawRect(10, 20, 20, 200);
		g.drawRect(110, 20, 20, 200);
	}
	
}
