package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketBlaseDaten extends SpielPacket {
	
	public int id;
	public double radius;
	
	public PacketBlaseDaten() {
	}
	
	public PacketBlaseDaten(int id, double radius) {
		this.id = id;
		this.radius = radius;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeDouble(radius);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		radius = in.readDouble();
	}
	
}
