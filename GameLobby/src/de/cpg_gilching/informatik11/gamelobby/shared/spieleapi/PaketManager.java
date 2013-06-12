package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

/**
 * Markierendes Interface, das von Klassen implementiert wird, die Spiel-Pakete verarbeiten.<br>
 * Es müssen keine Methoden überschrieben werden. Für die Lobby relevante Methoden erfüllen die folgenden Kriterien:
 * <ul>
 * <li>sie sind public</li>
 * <li>sie heißen <b>verarbeiten</b></li>
 * <li>sie nehmen genau einen Parameter vom Typ des Packets, das sie verarbeiten</li>
 * </ul>
 * Wenn ein {@link SpielPacket} empfangen wird, wird die entsprechende Methode des PaketManagers aufgerufen.
 */
public interface PaketManager {
}
