package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class Connection implements IReadWriteErrorHandler {
	
	private static int instanceCounter = 0;
	
	private PotentialSocket socket;
	private PacketReadThread readThread;
	private PacketWriteThread writeThread;
	private PacketProcessor netProcessor = null;
	
	private IPacketDictionary dictionary;
	
	private volatile boolean closed = false;
	
	private volatile int writec, writes;
	private volatile int readc, reads;
	private long last = 0;
	
	public Connection(PotentialSocket socket, IPacketDictionary dictionary) throws IOException {
		IStatsListener stats = new IStatsListener() {
			@Override
			public void packetWritten(int size) {
				writec++;
				writes += size;
			}
			
			@Override
			public void packetRead(int size) {
				readc++;
				reads += size;
			}
		};

		this.socket = socket;
		this.dictionary = dictionary;
		readThread = new PacketReadThread(socket.getInputStream(), dictionary, stats, this, "NetworkReaderThread-" + ++instanceCounter);
		writeThread = new PacketWriteThread(socket.getOutputStream(), dictionary, stats, this, "NetworkWriterThread-" + instanceCounter);
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
		
		long now = System.currentTimeMillis();
		if (now - last >= 6000) {
			double seconds = (now - last) / 1000.0;
			last = now;
			
			System.out.format("written: %.2f (%s/s); read: %.2f (%s/s)%n", writec / seconds, formatBytes(writes / seconds), readc / seconds, formatBytes(reads / seconds));
			writec = writes = readc = reads = 0;
		}
		
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
	
	private static final NumberFormat decimalFormatter;
	
	static {
		decimalFormatter = DecimalFormat.getInstance(Locale.US);
		decimalFormatter.setMinimumFractionDigits(2);
		decimalFormatter.setMaximumFractionDigits(2);
	}
	
	/**
	 * Formats a given amount of bytes into an applicable bigger unit.
	 * <p />
	 * Everything from 512 bytes will be displayed in KB, everything from 1024 KB in MB. A conversion factor of 1024 is used.
	 * 
	 * @param b the amount of byte
	 * @return a formatted string
	 */
	private static String formatBytes(double b) {
		if (b >= 1024 * 1024)
			return decimalFormatter.format(b / 1024.0 / 1024.0) + " MB";
		if (b >= 512)
			return decimalFormatter.format(b / 1024.0) + " KB";
		
		return decimalFormatter.format(b) + " B";
	}

}
