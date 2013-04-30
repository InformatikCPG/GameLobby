package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;

public class PongPaketManager extends PaketManager {
	
	public static class PaketSchlägerBewegen extends Packet {
		@Override
		public void read(DataInputStream in) throws IOException {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void write(DataOutputStream out) throws IOException {
			// TODO Auto-generated method stub
		}
	}
	
	private PongSpielServer spiel;
	
	public PongPaketManager(PongSpielServer spiel) {
		this.spiel = spiel;
	}
	
	public void empfangen(PaketSchlägerBewegen p) {
		spiel.linkenSchlägerBewegen(1);
		spiel.rechtenSchlägerBewegen(1);
	}
	
}
