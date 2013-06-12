package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketReset extends SpielPacket {
	
	public PacketReset() {
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
	}
	
}
