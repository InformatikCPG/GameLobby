package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketEntityBewegen extends SpielPacket {
	
	public int id;
	public int x;
	public int y;
	public int ausrichtung;
	public int health;
	public int mana;
	public int charge;
	
	public PacketEntityBewegen() {
	}
	
	public PacketEntityBewegen(int id, int x, int y, int ausrichtung, int health, int mana, int charge) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.ausrichtung = ausrichtung;
		this.health = health;
		this.mana = mana;
		this.charge = charge;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeInt(x);
		out.writeInt(y);
		out.write(ausrichtung);
		out.write(health);
		out.write(mana);
		out.write(charge);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		x = in.readInt();
		y = in.readInt();
		ausrichtung = in.read();
		health = in.read();
		mana = in.read();
		charge = in.read();
	}
	
}
