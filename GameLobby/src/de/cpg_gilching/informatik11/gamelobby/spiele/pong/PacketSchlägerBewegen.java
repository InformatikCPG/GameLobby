package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketSchlägerBewegen extends SpielPacket {
	
	public int linkePosition;
	public int rechtePosition;
	
	public PacketSchlägerBewegen() {
	}
	
	public PacketSchlägerBewegen(int links, int rechts) {
		linkePosition = links;
		rechtePosition = rechts;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(linkePosition);
		out.writeInt(rechtePosition);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		linkePosition = in.readInt();
		rechtePosition = in.readInt();
	}
	
}
