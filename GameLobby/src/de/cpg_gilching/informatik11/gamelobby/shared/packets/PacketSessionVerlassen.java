package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSessionVerlassen extends Packet {
	
	public int sessionId;
	
	public PacketSessionVerlassen() {
	}
	
	public PacketSessionVerlassen(int sessionId) {
		this.sessionId = sessionId;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(sessionId);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		sessionId = in.readInt();
	}
	
}
