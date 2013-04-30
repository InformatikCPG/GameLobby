package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;


public class PacketWriteThread extends Thread {
	
	private final DataOutputStream outStream;
	private final IPacketDictionary packetDictionary;
	private final IStatsListener statsObserver;
	private final IReadWriteErrorHandler errorHandler;
	private LinkedList<Packet> outPackets = new LinkedList<Packet>();
	private volatile boolean running = true;
	
	public PacketWriteThread(OutputStream outStream, IPacketDictionary packetDictionary, IStatsListener observer, IReadWriteErrorHandler errorHandler, String name) {
		super(name);
		this.outStream = new DataOutputStream(outStream);
		this.packetDictionary = packetDictionary;
		this.statsObserver = observer;
		this.errorHandler = errorHandler;
		this.setDaemon(true);
		this.start();
	}
	
	public void addPacket(Packet p) {
		synchronized (outPackets) {
			outPackets.add(p);
		}
		
		this.interrupt();
	}
	
	public synchronized boolean isFlush() {
		if (!running)
			return true;
		
		synchronized (outPackets) {
			return outPackets.isEmpty();
		}
	}
	
	public void shutdown() {
		running = false;
		this.interrupt();
	}
	
	@Override
	public void run() {
		try {
			while (running) {
				writePackets();
				if (!Thread.interrupted())
					try {
						Thread.sleep(10000L);
					} catch (InterruptedException e) {
					} // we will be interrupted when new packets are ready to be written
			}
			
			// write remaining packets one last time before we shut down
			writePackets();
		} catch (IOException e) {
			if (errorHandler == null) {
				System.err.println("error in writer thread!");
				e.printStackTrace();
			}
			else {
				errorHandler.handleRWError(e);
			}
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				System.err.println("error closing outgoing connection!");
				e.printStackTrace();
			}
		}
	}
	
	private void writePackets() throws IOException {
		while (true) {
			Packet p;
			synchronized (outPackets) {
				p = outPackets.poll();
			}
			if (p == null)
				break;
			
			
			if (p.shouldLog())
				System.out.println("sending " + p);
			
			int sizeOld = outStream.size();
			
			outStream.writeShort(packetDictionary.getPacketId(p));
			p.write(outStream);
			outStream.flush();
			
			if (statsObserver != null) {
				int sizeNew = outStream.size();
				statsObserver.packetWritten(sizeNew - sizeOld);
			}
		}
		
	}
	
}
