package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.awt.event.KeyEvent;

public interface ITastaturListener {
	
	public void onTasteGeändert(KeyEvent event, boolean zustand);
	
}
