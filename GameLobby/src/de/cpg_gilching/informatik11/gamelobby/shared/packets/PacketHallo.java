package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketHallo extends Packet {
	
	public String username;
	
	public PacketHallo() {
	}
	
	public PacketHallo(String username) {
		this.username = username;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(username);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		username = in.readUTF();
	}
	
}
