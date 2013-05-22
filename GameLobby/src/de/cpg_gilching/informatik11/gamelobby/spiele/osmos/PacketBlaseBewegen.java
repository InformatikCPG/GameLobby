package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketBlaseBewegen extends SpielPacket {
	
	public int id;
	public Vektor position;
	
	public PacketBlaseBewegen() {
	}
	
	public PacketBlaseBewegen(int id, Vektor position) {
		this.id = id;
		this.position = position;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeDouble(position.x);
		out.writeDouble(position.y);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		position = new Vektor(in.readDouble(), in.readDouble());
	}
	
}
