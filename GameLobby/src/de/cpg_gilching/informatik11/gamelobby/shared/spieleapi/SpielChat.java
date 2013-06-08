package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.server.LobbySpieler;
import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;

public class SpielChat {
	
	public static interface ChatBefehl {
		public void ausführen(Spieler sender, String[] argumente);
	}
	
	private ServerSpiel spiel;
	private Map<String, ChatBefehl> befehle = new HashMap<String, ChatBefehl>();
	
	public SpielChat(ServerSpiel spiel) {
		this.spiel = spiel;
	}
	
	public void spielerChatAusführen(Spieler von, String nachricht) {
		nachricht = nachricht.trim();

		if (nachricht.startsWith("!")) {
			List<String> argumente = Helfer.tokenize(nachricht.substring(1), " ", false);
			String befehlName = argumente.remove(0).toLowerCase(); // den Befehl an sich nicht als Argument übergeben und abspeichern
			
			if (befehle.containsKey(befehlName)) {
				// ein registrierter Befehl
				befehle.get(befehlName).ausführen(von, argumente.toArray(new String[argumente.size()]));
				return;
			}
		}
		
		// andernfalls normale Chat-Nachricht
		nachrichtAnAlleTeilnehmer("<" + von.getName() + "> " + nachricht);
	}

	public void nachrichtAnAlleTeilnehmer(String nachricht) {
		for (Spieler spieler : spiel.getTeilnehmer()) {
			nachrichtAnSpieler(spieler, nachricht);
		}
	}
	
	public void nachrichtAnSpieler(Spieler spieler, String nachricht) {
		((LobbySpieler) spieler).packetSenden(new PacketChatNachricht(spiel.getSpielId(), nachricht));
	}
	
	public void befehlRegistrieren(String name, ChatBefehl befehl) {
		befehle.put(name, befehl);
	}

}
