package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketDisconnect extends Packet {
	
	public String grund;
	
	public PacketDisconnect() {
	}
	
	public PacketDisconnect(String grund) {
		this.grund = grund;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(grund);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		grund = in.readUTF();
	}
	
}
