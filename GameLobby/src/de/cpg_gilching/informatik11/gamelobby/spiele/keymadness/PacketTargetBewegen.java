package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketTargetBewegen extends SpielPacket {
	
	public int id;
	public int x;
	public int y;
	
	public PacketTargetBewegen() {
	}
	
	public PacketTargetBewegen(int id, double x, double y) {
		this.id = id;
		this.x = (int) Math.round(x);
		this.y = (int) Math.round(y);
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeInt(x);
		out.writeInt(y);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		x = in.readInt();
		y = in.readInt();
	}
	
}
