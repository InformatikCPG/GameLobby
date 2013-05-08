package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketSpielTaste extends SpielPacket {
	
	public int keyCode;
	public boolean zustand;
	
	public PacketSpielTaste() {
	}
	
	public PacketSpielTaste(int keyCode, boolean zustand) {
		this.keyCode = keyCode;
		this.zustand = zustand;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(keyCode);
		out.writeBoolean(zustand);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		keyCode = in.readInt();
		zustand = in.readBoolean();
	}
	
}
