package de.cpg_gilching.informatik11.gamelobby.shared;

import java.util.HashMap;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.shared.net.IPacketDictionary;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class AdapterPaketLexikon implements IPacketDictionary {
	
	private Map<Integer, Class<? extends Packet>> nachId = new HashMap<Integer, Class<? extends Packet>>();
	private Map<Class<? extends Packet>, Integer> nachKlasse = new HashMap<Class<? extends Packet>, Integer>();
	
	public AdapterPaketLexikon() {
	}
	
	@Override
	public Class<? extends Packet> getPacketById(int id) {
		return nachId.get(id);
	}
	
	@Override
	public int getPacketId(Packet p) {
		return nachKlasse.get(p.getClass());
	}
	
	public void anmelden(Class<? extends Packet> klasse) {
		int id = klasse.getCanonicalName().hashCode() & 0xFFFF;
		
		if (nachId.containsKey(id) || nachKlasse.containsKey(klasse))
			throw new Error("Doppeltes Paket: " + id + " für Paket " + klasse.getCanonicalName());
		
		nachId.put(id, klasse);
		nachKlasse.put(klasse, id);
	}
	
}
