package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketBallBewegen extends SpielPacket {
	
	public int x;
	public int y;
	
	public PacketBallBewegen() {
	}
	
	public PacketBallBewegen(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeShort(x);
		out.writeShort(y);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		x = in.readUnsignedShort();
		y = in.readUnsignedShort();
	}
	
}
