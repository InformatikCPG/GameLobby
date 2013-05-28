package de.cpg_gilching.informatik11.gamelobby.spiele.tictactoe;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.IMausListener;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class TicTacToeClient extends ClientSpiel implements PaketManager,
		IMausListener {

	public int[] felder;
	Image Hintergrund;
	Image X;
	Image O;

	@Override
	protected void starten() {
		// TODO Auto-generated method stub
		leinwandAktivieren(600, 600);
		mausRegistrieren(this);
		felder = new int[9];
		Hintergrund = Helfer.bildLaden("Hintergrund.png");
		X = Helfer.bildLaden("X.png");
		O = Helfer.bildLaden("O.png");

	}

	@Override
	public void onMaustasteGeändert(MouseEvent event, boolean zustand) {
		// TODO Auto-generated method stub
		int feld=42;
		if (event.getX() <= 200 && event.getY() <= 200) {
			feld = 0;
		} else if (event.getX() <= 400 && event.getY() <= 200) {
			feld = 1;
		} else if (event.getX() <= 600 && event.getY() <= 200) {
			feld = 2;
		} else if (event.getX() <= 200 && event.getY() <= 400) {
			feld = 3;
		} else if (event.getX() <= 400 && event.getY() <= 400) {
			feld = 4;
		} else if (event.getX() <= 600 && event.getY() <= 400) {
			feld = 5;
		} else if (event.getX() <= 200 && event.getY() <= 600) {
			feld = 6;
		} else if (event.getX() <= 400 && event.getY() <= 600) {
			feld = 7;
		} else if (event.getX() <= 600 && event.getY() <= 600) {
			feld = 8;
		}
		spielPacketSenden(new PacketFeldKlick(feld));
	}
	

	@Override
	public void leinwandRendern(Graphics2D g) {
		g.drawImage(Hintergrund, 0, 0, null);
		feldRendern(g, 0, 0, 0);
		feldRendern(g, 1, 200, 0);
		feldRendern(g, 2, 400, 0);
		feldRendern(g, 3, 0, 200);
		feldRendern(g, 4, 200, 200);
		feldRendern(g, 5, 400, 200);
		feldRendern(g, 6, 0, 400);
		feldRendern(g, 7, 200, 400);
		feldRendern(g, 8, 400, 400);
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
