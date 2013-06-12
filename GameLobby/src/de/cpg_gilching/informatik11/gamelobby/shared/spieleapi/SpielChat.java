package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.server.LobbySpieler;
import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;

/**
 * Eine API, um {@link ServerSpiel}en einfachen Zugriff auf den Ingame-Chat zu geben.
 * <p>
 * Es ist möglich, Nachrichten auszugeben sowie Befehle zu registrieren, die von Spielern ausgeführt werden können.
 * </p>
 */
public class SpielChat {
	
	/**
	 * Darstellung eines Befehls, der von einem {@link Spieler} ausgeführt werden kann.
	 */
	public static interface ChatBefehl {
		/**
		 * Die Methode, die aufgerufen wird, wenn ein {@link Spieler} diesen Befehl ausführt.
		 * 
		 * @param sender der Spieler, der den Befehl gesendet hat
		 * @param argumente ein beliebig langes Array mit Argumenten, die der Spieler dem Befehl gegeben hat (Argumente werden an Leerzeichen getrennt)
		 */
		public void ausführen(Spieler sender, String[] argumente);
	}
	
	private ServerSpiel spiel;
	private Map<String, ChatBefehl> befehle = new HashMap<String, ChatBefehl>();
	
	public SpielChat(ServerSpiel spiel) {
		this.spiel = spiel;
	}
	
	/**
	 * Wird aufgerufen, wenn ein Spieler eine Chat-Nachricht sendet. Es wird auch auf einen möglichen Befehl geprüft.<br>
	 * Ein implementierendes Spiel muss diese Methode nur selten aufrufen.
	 * 
	 * @param von der Spieler, von dem die Nachricht kommt
	 * @param nachricht die Nachricht, die gesendet wurde
	 */
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

	/**
	 * Sendet eine Chat-Nachricht an alle teilnehmenden Spieler.
	 * 
	 * @param nachricht die zu sendende Nachricht
	 */
	public void nachrichtAnAlleTeilnehmer(String nachricht) {
		for (Spieler spieler : spiel.getTeilnehmer()) {
			nachrichtAnSpieler(spieler, nachricht);
		}
	}
	
	/**
	 * Sendet eine Chat-Nachricht an einen bestimmten Spieler.
	 * 
	 * @param spieler der Empfänger der Nachricht
	 * @param nachricht die zu sendende Nachricht
	 */
	public void nachrichtAnSpieler(Spieler spieler, String nachricht) {
		((LobbySpieler) spieler).packetSenden(new PacketChatNachricht(spiel.getSpielId(), nachricht));
	}
	
	public void befehlRegistrieren(String name, ChatBefehl befehl) {
		befehle.put(name, befehl);
	}

}
