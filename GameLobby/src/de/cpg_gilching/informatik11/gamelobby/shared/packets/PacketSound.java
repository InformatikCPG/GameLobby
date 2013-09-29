package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSound extends Packet {
	
	public String soundName;
	
	public PacketSound() {
	}
	
	public PacketSound(String soundName) {
		this.soundName = soundName;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(soundName);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		soundName = in.readUTF();
	}
	
}
