package de.cpg_gilching.informatik11.gamelobby.spiele.tictactoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketFeldDrücken extends SpielPacket {
	
	public int feld;
	
	public PacketFeldDrücken() {
	}
	
	public PacketFeldDrücken(int feld) {
		this.feld = feld;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(feld);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		feld = in.readInt();
	}
	
}
