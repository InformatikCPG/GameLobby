package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketChatNachricht extends Packet {
	
	public String nachricht;
	
	public PacketChatNachricht() {
	}
	
	public PacketChatNachricht(String nachricht) {
		this.nachricht = nachricht;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(nachricht);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		nachricht = in.readUTF();
	}
	
}
