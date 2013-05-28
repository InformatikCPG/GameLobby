package de.cpg_gilching.informatik11.gamelobby.spiele.tictactoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketFeldSetzen extends SpielPacket {
	
	public int feld;
	public int wert;
	
	public PacketFeldSetzen() {
	}
	
	public PacketFeldSetzen(int feld, int wert) {
		this.feld = feld;
		this.wert = wert;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(feld);
		out.write(wert);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		feld = in.readInt();
		wert = in.read();
	}
	
}
