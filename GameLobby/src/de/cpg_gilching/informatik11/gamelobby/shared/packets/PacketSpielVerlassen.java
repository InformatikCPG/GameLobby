package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSpielVerlassen extends Packet {

	public int spielId;
	public String grund;
	
	public PacketSpielVerlassen() {
	}
	
	public PacketSpielVerlassen(int spielId, String grund) {
		this.spielId = spielId;
		this.grund = grund;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(spielId);
		
		if (grund == null) {
			out.writeBoolean(false);
		}
		else {
			out.writeBoolean(true);
			out.writeUTF(grund);
		}
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		spielId = in.readInt();
		if (in.readBoolean()) {
			grund = in.readUTF();
		}
		else {
			grund = null;
		}
	}
	
}
