package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSpielStarten extends Packet {
	
	public int spielId;
	public int beschreibungId;
	public int punktelimit;
	
	public PacketSpielStarten() {
	}
	
	public PacketSpielStarten(int spielId, int beschreibungId, int punktelimit) {
		this.spielId = spielId;
		this.beschreibungId = beschreibungId;
		this.punktelimit = punktelimit;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(spielId);
		out.writeInt(beschreibungId);
		out.writeInt(punktelimit);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		spielId = in.readInt();
		beschreibungId = in.readInt();
		punktelimit = in.readInt();
	}
	
}
