package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSpielStarten extends Packet {
	
	public int spielId;
	public int beschreibungId;
	
	public PacketSpielStarten() {
	}
	
	public PacketSpielStarten(int spielId, int beschreibungId) {
		this.spielId = spielId;
		this.beschreibungId = beschreibungId;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(spielId);
		out.writeInt(beschreibungId);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		spielId = in.readInt();
		beschreibungId = in.readInt();
	}
	
}
