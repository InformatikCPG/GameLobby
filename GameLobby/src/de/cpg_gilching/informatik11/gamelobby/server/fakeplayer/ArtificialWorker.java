package de.cpg_gilching.informatik11.gamelobby.server.fakeplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.net.IPacketDictionary;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionAnnehmen;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionStarten;

/**
 * Der Arbeiter der Bots, der auf eingehende Packets entsprechend antwortet. Hier liegt das Herzstück der "künstlichen Intelligenz" (die gar nicht so intelligent ist, wie sie sich anhört ...).
 */
class ArtificialWorker implements Runnable {
	
	/**
	 * Ein Array mit allen möglichen Nachrichten, die ein Bot auf seinen Namen erwidert.
	 */
	private static final String[] nachrichten = { "hey :)", "was gibts? ;)", "hallo welt ...", "ich mag züge", "das bin ich!", "danke!", "warum nicht?", "die zahl " + Helfer.zufallsZahl(100000) + " ist toll!" };

	private final IPacketDictionary dictionary;
	private final DataInputStream in;
	private final DataOutputStream out;
	
	public ArtificialWorker(InputStream in, OutputStream out, IPacketDictionary dictionary) {
		this.dictionary = dictionary;
		this.in = new DataInputStream(in);
		this.out = new DataOutputStream(out);
	}
	
	private void process(Packet p) throws IOException {
		if (p instanceof PacketSessionStarten) {
			PacketSessionStarten psess = (PacketSessionStarten) p;
			
			pout(new PacketSessionAnnehmen(psess.sessionId));
		}
		else if (p instanceof PacketChatNachricht) {
			String msg = ((PacketChatNachricht) p).nachricht.toLowerCase();
			String me = Thread.currentThread().getName().toLowerCase();
			
			if (msg.contains(me) && !msg.startsWith("<" + me) && msg.contains("<")) {
				Helfer.warten(100L);
				pout(new PacketChatNachricht(((PacketChatNachricht) p).spielId, nachrichten[Helfer.zufallsZahl(nachrichten.length)]));
			}
		}
	}
	
	@Override
	public void run() {
		try {
			out.writeLong(Packet.MAGIC_NUMBER);
			in.readLong();
			pout(new PacketHallo(Thread.currentThread().getName()));
			
			while (true) {
				int packetId = in.readUnsignedShort() & 0xFFFF;
				Class<? extends Packet> clazz = dictionary.getPacketById(packetId);
				
				Packet p = clazz.newInstance();
				p.read(in);
				
				process(p);
			}
		} catch (EOFException e) {
			System.out.println(Thread.currentThread().getName() + ": EOF");
		} catch (Exception e) {
			System.err.println(Thread.currentThread().getName() + " :: ");
			e.printStackTrace();
		}
	}
	
	private void pout(Packet p) throws IOException {
		out.writeShort(dictionary.getPacketId(p));
		p.write(out);
	}
	
}
