package de.cpg_gilching.informatik11.gamelobby.shared.net;

public interface IStatsListener {
	void packetWritten(int size);
	
	void packetRead(int size);
}
