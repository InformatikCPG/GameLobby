package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.IMausListener;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.IMausradListener;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ITastaturListener;

public class SpielInputListener implements KeyListener, MouseListener, MouseWheelListener, FocusListener {
	
	private Map<Integer, ITastaturListener> registrierteTasten = new HashMap<Integer, ITastaturListener>();
	private IMausListener mausListener = null;
	private IMausradListener mausradListener = null;
	
	private Set<Integer> gedrückteTasten = new HashSet<Integer>();
	
	public void tasteRegistrieren(int tastencode, ITastaturListener listener) {
		registrierteTasten.put(tastencode, listener);
	}
	
	public void mausRegistrieren(IMausListener listener) {
		mausListener = listener;
	}
	
	public void mausradRegistrieren(IMausradListener listener) {
		mausradListener = listener;
	}

	private void tasteSetzen(KeyEvent e, boolean zustand) {
		int tastencode = e.getKeyCode();
		
		if (registrierteTasten.containsKey(tastencode)) {
			if (gedrückteTasten.contains(tastencode) == zustand) {
				// es hat sich nicht wirklich was geändert
				return;
			}
			
			if (zustand)
				gedrückteTasten.add(tastencode);
			else
				gedrückteTasten.remove(tastencode);
			
			registrierteTasten.get(tastencode).onTasteGeändert(e, zustand);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		tasteSetzen(e, true);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		tasteSetzen(e, false);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// wird nicht benötigt
	}
	
	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		// wird nicht benötigt
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// wird nicht benötigt
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// wird nicht benötigt
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (mausListener != null)
			mausListener.onMaustasteGeändert(e, true);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (mausListener != null)
			mausListener.onMaustasteGeändert(e, false);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (mausradListener != null)
			mausradListener.onMausGescrollt(e);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		// wird nicht benötigt
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		// TODO tasten entfernen, wenn Fokus verloren wurde
	}
	
}
