package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Connection implements IReadWriteErrorHandler {
	
	private static int instanceCounter = 0;
	
	private PotentialSocket socket;
	private PacketReadThread readThread;
	private PacketWriteThread writeThread;
	private PacketProcessor netProcessor = null;
	
	private IPacketDictionary dictionary;
	
	private volatile boolean closed = false;
	
	
	public Connection(PotentialSocket socket, IPacketDictionary dictionary) throws IOException {
		this.socket = socket;
		this.dictionary = dictionary;
		readThread = new PacketReadThread(socket.getInputStream(), dictionary, null, this, "NetworkReaderThread-" + ++instanceCounter);
		writeThread = new PacketWriteThread(socket.getOutputStream(), dictionary, null, this, "NetworkWriterThread-" + instanceCounter);
	}
	
	/**
	 * Writes the {@link Packet#MAGIC_NUMBER} to the output stream of the connection, ignoring possible exceptions.
	 * <p/>
	 * Note that this happens synchronously. It shouldn't be called after the connection is fully established and the packet flow is already in progress.
	 */
	public void sendMagicNumber() {
		try {
			new DataOutputStream(socket.getOutputStream()).writeLong(Packet.MAGIC_NUMBER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setPacketProcessor(PacketProcessor packetProcessor) {
		this.netProcessor = packetProcessor;
	}
	
	public void sendPacket(Packet p) {
		writeThread.addPacket(p);
	}
	
	@Override
	public void handleRWError(IOException exception) {
		exception.printStackTrace();
		close();
	}
	
	public void close() {
		if (!closed) {
			closed = true;
			readThread.shutdown();
			writeThread.shutdown();
			
			// actually no need to close the socket, it will be closed automatically when the underlying streams are closed by their threads
			//			try {
			//				socket.close();
			//			} catch (IOException e) {
			//				e.printStackTrace();
			//			}
		}
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void processPackets() {
		if (netProcessor == null)
			return;
		
		Packet p;
		while ((p = readThread.peekPacket()) != null) {
			try {
				Method m = netProcessor.getClass().getDeclaredMethod("handle", p.getClass());
				m.invoke(netProcessor, p);
			} catch (NoSuchMethodException e) {
				netProcessor.onUnhandledPacket(p);
			} catch (InvocationTargetException e) {
				netProcessor.onException(e, p);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public PotentialSocket getSocket() {
		return socket;
	}
	
	public IPacketDictionary getDictionary() {
		return dictionary;
	}
	
}
