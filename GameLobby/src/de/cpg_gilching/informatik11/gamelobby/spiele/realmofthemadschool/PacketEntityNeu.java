package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketEntityNeu extends SpielPacket {
	
	public static final int TYP_DUDE = 1;
	public static final int TYP_BULLET = 2;
	
	public int id;
	public int typ;
	public int x;
	public int y;
	
	public PacketEntityNeu() {
	}
	
	public PacketEntityNeu(int id, int typ, int x, int y) {
		this.id = id;
		this.typ = typ;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.write(typ);
		out.writeShort(x);
		out.writeShort(y);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		typ = in.read();
		x = in.readShort();
		y = in.readShort();
	}
	
}
