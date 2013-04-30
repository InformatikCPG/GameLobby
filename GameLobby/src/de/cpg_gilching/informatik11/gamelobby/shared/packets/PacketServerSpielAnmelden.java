package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketServerSpielAnmelden extends Packet {
	
	public int spielId;
	public String spielBezeichner;
	
	public PacketServerSpielAnmelden() {
	}
	
	public PacketServerSpielAnmelden(int id, String bezeichner) {
		this.spielId = id;
		this.spielBezeichner = bezeichner;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(spielId);
		out.writeUTF(spielBezeichner);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		spielId = in.readInt();
		spielBezeichner = in.readUTF();
	}
	
}
