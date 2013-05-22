package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketNachrichtSenden extends SpielPacket {
	
	public String msg;
	
	public PacketNachrichtSenden() {
	}
	
	public PacketNachrichtSenden(String msg) {
		this.msg = msg;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeUTF(msg);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		msg = in.readUTF();
	}
	
}
