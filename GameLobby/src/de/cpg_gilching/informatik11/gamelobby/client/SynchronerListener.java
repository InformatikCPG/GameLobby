package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Hilfsklasse, um AWT-Events auf dem Hauptthread entgegennehmen zu können.
 * 
 * Kann als unterschiedliche Listener-Typen verwendet werden und muss bei Bedarf erweitert werden.
 */
public class SynchronerListener implements ActionListener {
	
	private Runnable task;
	private ControllerClient clientController;
	
	public SynchronerListener(Runnable task, ControllerClient clientController) {
		this.task = task;
		this.clientController = clientController;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		clientController.getScheduler().taskHinzufügen(task);
	}
	
}
