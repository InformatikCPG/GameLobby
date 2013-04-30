package de.cpg_gilching.informatik11.gamelobby.client;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.net.PacketProcessor;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketServerSpielAnmelden;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielerListe;

public class AllgemeinerPacketProcessorClient extends PacketProcessor {
	
	private ControllerClient client;
	
	public AllgemeinerPacketProcessorClient(ControllerClient client) {
		this.client = client;
	}
	
	@Override
	public void onUnhandledPacket(Packet p) {
	}
	
	@Override
	public void onException(Exception exception, Packet packet) {
		System.err.println("Fehler beim Verarbeiten von " + packet);
		exception.printStackTrace();
	}
	
	public void handle(PacketDisconnect packet) {
		System.out.println("Disconnect von Server: " + packet.grund);
		client.getVerbindung().close();
	}
	
	public void handle(PacketSpielerListe packet) {
		if (packet.neu) {
			client.getServerLobby().spielerAnlegen(packet.username);
		}
		else {
			client.getServerLobby().spielerEntfernen(packet.username);
		}
	}
	
	public void handle(PacketChatNachricht packet) {
		client.getServerLobby().chatNachrichtAnzeigen(packet.nachricht);
	}
	
	public void handle(PacketServerSpielAnmelden packet) {
		client.spielAnmelden(packet.spielId, packet.spielBezeichner);
	}
	
}
