package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielTaste;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class SnakePaketManagerServer implements PaketManager {
	
	private SnakeSpielServer snakeServer;
	private Spieler spieler;	
	
	//Konstruktor
	public SnakePaketManagerServer(SnakeSpielServer snake, Spieler spieler) {
		this.snakeServer = snake;
		this.spieler = spieler;
	}
	
	//was beim Aktivieren, der in SnakeBeschreibung definierten Tasten passieren soll
	public void verarbeiten(PacketSpielTaste packet) {
		if(packet.zustand) {
			Snake snake = snakeServer.sucheSnake(spieler);
			switch (packet.tastencode) {
			case KeyEvent.VK_UP:
				snake.richtungSpeichern(3);
				break;
			case KeyEvent.VK_RIGHT:
				snake.richtungSpeichern(0);
				break;
			case KeyEvent.VK_DOWN:
				snake.richtungSpeichern(1);
				break;
			case KeyEvent.VK_LEFT:
				snake.richtungSpeichern(2);
				break;
			}
		}
	}
}
