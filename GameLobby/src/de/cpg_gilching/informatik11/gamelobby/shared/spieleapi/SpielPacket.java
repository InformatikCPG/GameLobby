package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public abstract class SpielPacket extends Packet {
	
	public int spielId; // muss vom Sender des Packets manuell gesetzt werden
	
	@Override
	public final void write(DataOutputStream out) throws IOException {
		out.write(spielId); // 1-byte Wert, um Overhead zu verringern
		ausgeben(out);
	}
	
	@Override
	public final void read(DataInputStream in) throws IOException {
		spielId = in.read();
		einlesen(in);
	}
	
	public abstract void ausgeben(DataOutputStream out) throws IOException;
	
	public abstract void einlesen(DataInputStream in) throws IOException;
	
}
