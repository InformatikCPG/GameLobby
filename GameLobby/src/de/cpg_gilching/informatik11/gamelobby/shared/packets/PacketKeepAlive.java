package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketKeepAlive extends Packet {
	
	public PacketKeepAlive() {
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
	}
	
}
