package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketFeldSetzen extends SpielPacket {
	
	public int x;
	public int y;
	public int farbe; //-1=leer, ansonsten Farbwert
	
	public PacketFeldSetzen() {
	}
	
	public PacketFeldSetzen(int x, int y, int farbe) {
		this.x = x;
		this.y = y;
		this.farbe = farbe;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeShort(x);
		out.writeShort(y);
		out.writeInt(farbe);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		x = in.readUnsignedShort();
		y = in.readUnsignedShort();
		farbe = in.readInt();
	}
	
}
