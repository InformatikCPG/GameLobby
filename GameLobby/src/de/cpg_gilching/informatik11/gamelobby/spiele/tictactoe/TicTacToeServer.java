package de.cpg_gilching.informatik11.gamelobby.spiele.tictactoe;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class TicTacToeServer extends ServerSpiel {
	
	public int[] felder;
	private Spieler spieler1;
	private Spieler spieler2;
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new TTTPaketM(spieler, this);
	}
	
	@Override
	protected void starten() {
		felder = new int[9];
		spieler1 = teilnehmer.get(0);
		spieler2 = teilnehmer.get(1);
	}
	public void feldSetzen(Spieler spieler, int feld) {
		if(spieler == spieler1) {
			felder[feld]=1;
			packetAnAlle(new PacketFeldSetzen(feld,1));
			SiegÜberprüfenNachZug(1);
		}
		else if(spieler == spieler2) {
			felder[feld]=2;
			packetAnAlle(new PacketFeldSetzen(feld,2));
			SiegÜberprüfenNachZug(2);
		}
	}
	public void SiegÜberprüfenNachZug(int spieler){
		if(felder[0]==spieler && felder[1]==spieler && felder[2]==spieler){
			gewinnenLassen(spieler);
		}
		else if(felder[3]==spieler && felder[4]==spieler && felder[5]==spieler){
			gewinnenLassen(spieler);
		}
		else if(felder[6]==spieler && felder[7]==spieler && felder[8]==spieler){
			gewinnenLassen(spieler);
		}
		else if(felder[0]==spieler && felder[3]==spieler && felder[6]==spieler){
			gewinnenLassen(spieler);
		}
		else if(felder[1]==spieler && felder[4]==spieler && felder[7]==spieler){
			gewinnenLassen(spieler);
		}
		else if(felder[2]==spieler && felder[5]==spieler && felder[8]==spieler){
			gewinnenLassen(spieler);
		}
		else if(felder[0]==spieler && felder[4]==spieler && felder[8]==spieler){
			gewinnenLassen(spieler);
		}
		else if(felder[2]==spieler && felder[4]==spieler && felder[6]==spieler){
			gewinnenLassen(spieler);
		}
		
	}

	private void gewinnenLassen(int spieler) {
		for(int i =0; i<felder.length; i++){
		packetAnAlle(new PacketFeldSetzen(i,0));
		felder[i]=0;
	}
	
	}
}
