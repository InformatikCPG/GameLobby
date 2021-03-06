package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketChatNachricht extends Packet {
	
	public int spielId;
	public String nachricht;
	
	public PacketChatNachricht() {
	}
	
	public PacketChatNachricht(int spielId, String nachricht) {
		this.spielId = spielId;
		this.nachricht = nachricht;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(spielId);
		out.writeUTF(nachricht);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		spielId = in.readInt();
		nachricht = in.readUTF();
	}
	
}
