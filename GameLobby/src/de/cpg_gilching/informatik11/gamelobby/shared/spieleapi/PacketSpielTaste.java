package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketSpielTaste extends SpielPacket {
	
	public int tastencode;
	public boolean zustand;
	
	public PacketSpielTaste() {
	}
	
	public PacketSpielTaste(int tastencode, boolean zustand) {
		this.tastencode = tastencode;
		this.zustand = zustand;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(tastencode);
		out.writeBoolean(zustand);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		tastencode = in.readInt();
		zustand = in.readBoolean();
	}
	
}
