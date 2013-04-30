package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSpielerListe extends Packet {
	
	public String username;
	public boolean neu;
	
	public PacketSpielerListe() {
	}
	
	public PacketSpielerListe(String username, boolean neu) {
		this.username = username;
		this.neu = neu;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		out.writeBoolean(neu);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		username = in.readUTF();
		neu = in.readBoolean();
	}
	
}
