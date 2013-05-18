package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSpielVerlassen extends Packet {
	
	public int spielId;
	
	public PacketSpielVerlassen() {
	}
	
	public PacketSpielVerlassen(int spielId) {
		this.spielId = spielId;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(spielId);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		spielId = in.readInt();
	}
	
}
