package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class that wraps around an input stream and counts the amount of bytes that are read from it.
 * <p/>
 * Optionally, an observer can be associated that is notified at given intervals of read bytes.
 */
public class CountingInputStream extends FilterInputStream {
	
	public static interface SizeObserver {
		public void onSizeUpdate(int newSize);
	}
	
	private int readCounter = 0;
	
	private int lastUpdate = 0;
	private SizeObserver sizeObserver = null;
	private int interval = -1;
	
	public CountingInputStream(InputStream in) {
		super(in);
	}
	
	
	public void setSizeObserver(int interval, SizeObserver sizeObserver) {
		this.interval = interval;
		this.sizeObserver = sizeObserver;
	}
	
	private void checkObservers() {
		if (sizeObserver != null && readCounter - lastUpdate >= interval) {
			lastUpdate = readCounter;
			sizeObserver.onSizeUpdate(readCounter);
		}
	}
	
	@Override
	public int read() throws IOException {
		int result = super.read();
		if (result > -1) {
			readCounter++;
			checkObservers();
		}
		
		return result;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int read = super.read(b, off, len);
		if (read > -1) {
			readCounter += read;
			checkObservers();
		}
		
		return read;
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		int read = super.read(b);
		if (read > -1) {
			readCounter += read;
			checkObservers();
		}
		
		return read;
	}
	
	@Override
	public long skip(long n) throws IOException {
		long skipped = super.skip(n);
		if (skipped > 0) {
			readCounter += skipped;
			checkObservers();
		}
		
		return skipped;
	}
	
	@Override
	public boolean markSupported() {
		return false;
	}
	
	/**
	 * Returns the amount of bytes read by this stream.
	 * NOTE: Not thread-safe, calling thread should be the one that reads from this stream.
	 * 
	 * @return the value of the internal counter holding the amount of bytes read
	 */
	public int size() {
		return readCounter;
	}
	
}
