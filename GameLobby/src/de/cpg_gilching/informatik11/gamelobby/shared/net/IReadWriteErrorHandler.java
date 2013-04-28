package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.IOException;

public interface IReadWriteErrorHandler {
	/**
	 * WARNING: CALLED ASYNCHRONOUSLY!
	 */
	public void handleRWError(IOException exception);
}
