package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

/**
 * Ein Packet, das innerhalb eines Spiels versendet wird. Es enthält die ID des Spiels, welche automatisch gesetzt wird.
 * <p>
 * Implementierungen müssen aus technischen Gründen in jedem Fall einen leeren Konstruktor (ohne Parameter) bereitstellen, da sonst das Paket nicht instanziiert werden kann.<br>
 * Dazu muss in der Regel ein Konstruktor angelegt werden, der die Attribute mit richtigen Werten füllt.
 * </p>
 * <p>
 * Die Methoden {@link #ausgeben(DataOutputStream)} und {@link #einlesen(DataInputStream)} müssen zueinander symmetrisch arbeiten, also müssen in jedem Fall die Daten, die erstere Methode ausgibt, in exakt dieser Form von letzterer Methode wieder eingelesen werden.
 * </p>
 */
public abstract class SpielPacket extends Packet {
	
	public int spielId; // muss vom Sender des Packets manuell gesetzt werden
	
	@Override
	public final void write(DataOutputStream out) throws IOException {
		out.write(spielId); // 1-byte Wert, um Overhead zu verringern
		ausgeben(out);
	}
	
	@Override
	public final void read(DataInputStream in) throws IOException {
		spielId = in.read();
		einlesen(in);
	}
	
	/**
	 * Schreibt alle relevanten Daten dieses Packets in den gegebenen Stream.
	 * 
	 * @param out der Output-Stream, in den geschrieben werden soll
	 * @throws IOException wenn das Schreiben in den Stream nicht funktioniert hat
	 */
	public abstract void ausgeben(DataOutputStream out) throws IOException;
	
	/**
	 * Liest alle relevanten Daten dieses Packets aus dem gegebenen Stream und speichert sie in den Attributen dieses Packets.
	 * 
	 * @param in der Input-Stream, aus dem gelesen werden soll
	 * @throws IOException wenn das Lesen aus dem Stream nicht funktioniert hat
	 */
	public abstract void einlesen(DataInputStream in) throws IOException;
	
}
