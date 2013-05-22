package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketNeueBlase extends SpielPacket {
	
	public int id;
	public double radius;
	public Vektor position;
	
	public PacketNeueBlase() {
	}
	
	public PacketNeueBlase(int id, double radius, Vektor position) {
		this.id = id;
		this.radius = radius;
		this.position = position;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeDouble(radius);
		out.writeDouble(position.x);
		out.writeDouble(position.y);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		radius = in.readDouble();
		position = new Vektor(in.readDouble(), in.readDouble());
	}
	
}
