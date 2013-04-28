package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;


public class PacketReadThread extends Thread {
	
	private final DataInputStream inStream;
	private final CountingInputStream inStreamCounting;
	private final IStatsListener statsObserver;
	private final IReadWriteErrorHandler errorHandler;
	private LinkedList<Packet> readPackets = new LinkedList<Packet>();
	private volatile boolean running = true;
	
	public PacketReadThread(InputStream inStream, IStatsListener observer, IReadWriteErrorHandler errorHandler, String name) {
		super(name);
		this.inStreamCounting = new CountingInputStream(inStream);
		this.inStream = new DataInputStream(inStreamCounting);
		this.statsObserver = observer;
		this.errorHandler = errorHandler;
		this.setDaemon(true);
		this.start();
	}
	
	public Packet peekPacket() {
		Packet ret = null;
		synchronized (readPackets) {
			ret = readPackets.poll();
		}
		return ret;
	}
	
	public void shutdown() {
		running = false;
	}
	
	@Override
	public void run() {
		try {
			while (running) {
				int oldSize = inStreamCounting.size();
				
				int packetId = inStream.readUnsignedShort() & 0xFFFF;
				Class<? extends Packet> clazz = Packet.byId.get(packetId);
				if (clazz == null)
					System.out.println("#" + Thread.currentThread().getName() + "# invalid packet: " + packetId);
				else {
					try {
						Packet p = clazz.newInstance();
						p.read(inStream);
						synchronized (readPackets) {
							readPackets.add(p);
						}
					} catch (InstantiationException e) {
						System.err.println("packet violates contract");
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						System.err.println("packet violates contract");
						e.printStackTrace();
					}
				}
				
				if (statsObserver != null) {
					int newSize = inStreamCounting.size();
					statsObserver.packetRead(newSize - oldSize);
				}
			}
		} catch (EOFException e) {
			System.out.println("PacketReadThread: stream has ended (EOF)");
		} catch (IOException e) {
			if (running) { // the error wasn't because we were ended
				if (errorHandler == null) {
					System.err.println("PacketReadThread exited disgracefully!");
					e.printStackTrace();
				}
				else {
					errorHandler.handleRWError(e);
				}
			}
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				System.err.println("Error closing connection input stream!");
				e.printStackTrace();
			}
		}
	}
}
