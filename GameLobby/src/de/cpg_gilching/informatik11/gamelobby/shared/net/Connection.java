package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.DataInputStream;
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
	
	public void startThreads() {
		readThread.start();
		writeThread.start();
	}
	
	/**
	 * Writes the {@link Packet#MAGIC_NUMBER} to the output stream of the connection, ignoring possible exceptions.<br>
	 * If successful, it also receives and validates the returned number, which should be {@link Packet#MAGIC_NUMBER_ACK}.
	 * <p/>
	 * Note that this happens synchronously. It shouldn't be called after the connection is fully established and the packet flow is already in progress.<br>
	 * (that means it should be called before {@link #startThreads()} has been called)
	 * 
	 * @return true if the validation was successful, false if a wrong number was returned or an I/O error occurred
	 */
	public boolean sendMagicNumber() {
		try {
			new DataOutputStream(socket.getOutputStream()).writeLong(Packet.MAGIC_NUMBER);
			long magicNumber = new DataInputStream(socket.getInputStream()).readLong();
			return magicNumber == Packet.MAGIC_NUMBER_ACK;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
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
				Method m = netProcessor.getClass().getMethod("handle", p.getClass());
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
