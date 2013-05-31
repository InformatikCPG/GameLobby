package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketMausPosition extends SpielPacket {
	
	public Vektor position;
	
	public PacketMausPosition() {
	}
	
	public PacketMausPosition(Vektor position) {
		this.position = position;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeDouble(position.x);
		out.writeDouble(position.y);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		position = new Vektor(in.readDouble(), in.readDouble());
	}
	
}
