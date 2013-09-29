package de.cpg_gilching.informatik11.gamelobby.spiele.tictactoe;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class TicTacToeServer extends ServerSpiel {
	
	public int[] felder;
	private Spieler spieler1;
	private Spieler spieler2;
	private Spieler spielerAmZug;
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new TTTPaketM(spieler, this);
	}
	
	@Override
	protected void starten() {
		felder = new int[9];
		spieler1 = teilnehmer.get(0);
		spieler2 = teilnehmer.get(1);
		spielerAmZug = spieler1;
		
		chat.nachrichtAnSpieler(spieler1, "Du bist X!");
		chat.nachrichtAnSpieler(spieler2, "Du bist O!");
		chat.nachrichtAnAlleTeilnehmer(spieler1.getName() + " beginnt!");
	}

	public void feldSetzen(Spieler spieler, int feld) {
		if (felder[feld] == 0) {
			if (spieler == spieler1 && spielerAmZug == spieler1) {
				felder[feld] = 1;
				spielerAmZug = spieler2;
				packetAnAlle(new PacketFeldSetzen(feld, 1));
				soundAnAlle("tictactoeClick");
				SiegÜberprüfenNachZug(1);
			}
			else if (spieler == spieler2 && spielerAmZug == spieler2) {
				felder[feld] = 2;
				spielerAmZug = spieler1;
				packetAnAlle(new PacketFeldSetzen(feld, 2));
				soundAnAlle("tictactoeClick");
				SiegÜberprüfenNachZug(2);
			}
		}
	}
	
	public void SiegÜberprüfenNachZug(int spieler) {
		if (felder[0] == spieler && felder[1] == spieler && felder[2] == spieler) {
			gewinnenLassen(spieler);
		}
		else if (felder[3] == spieler && felder[4] == spieler && felder[5] == spieler) {
			gewinnenLassen(spieler);
		}
		else if (felder[6] == spieler && felder[7] == spieler && felder[8] == spieler) {
			gewinnenLassen(spieler);
		}
		else if (felder[0] == spieler && felder[3] == spieler && felder[6] == spieler) {
			gewinnenLassen(spieler);
		}
		else if (felder[1] == spieler && felder[4] == spieler && felder[7] == spieler) {
			gewinnenLassen(spieler);
		}
		else if (felder[2] == spieler && felder[5] == spieler && felder[8] == spieler) {
			gewinnenLassen(spieler);
		}
		else if (felder[0] == spieler && felder[4] == spieler && felder[8] == spieler) {
			gewinnenLassen(spieler);
		}
		else if (felder[2] == spieler && felder[4] == spieler && felder[6] == spieler) {
			gewinnenLassen(spieler);
		}
		else {
			int leer = 0;
			for (int i = 0; i < felder.length; i++) {
				if (felder[i] == 0) {
					leer++;
				}
			}
			
			if (leer == 0) {
				// unentschieden
				gewinnenLassen(0);
			}
		}
	}

	private void gewinnenLassen(int spieler) {
		for (int i = 0; i < felder.length; i++) {
			packetAnAlle(new PacketFeldSetzen(i, 0));
			felder[i] = 0;
		}
		
		if (spieler == 1) {
			scoreboard.punktHinzufügen(spieler1);
			chat.nachrichtAnAlleTeilnehmer("X hat gewonnen!");
			soundAnSpieler(spieler1, "tictactoeWin");
			soundAnSpieler(spieler2, "tictactoeLose");
		}
		else if (spieler == 2) {
			scoreboard.punktHinzufügen(spieler2);
			chat.nachrichtAnAlleTeilnehmer("O hat gewonnen!");
			soundAnSpieler(spieler2, "tictactoeWin");
			soundAnSpieler(spieler1, "tictactoeLose");
		}
		else {
			chat.nachrichtAnAlleTeilnehmer("Unentschieden!");
		}
	}
}
