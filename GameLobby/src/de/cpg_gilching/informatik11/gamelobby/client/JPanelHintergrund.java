package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

/**
 * Ein simples {@link JPanel}, das ein Bild im Hintergrund anzeigt.
 */
public class JPanelHintergrund extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private boolean listenerAdded = false;
	private Image bgBild;
	
	public JPanelHintergrund(String bgName) {
		bgBild = Helfer.bildLaden(bgName);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int yoffset = 0;
		if (this.getParent() instanceof JViewport) {
			JViewport viewport = (JViewport) this.getParent();
			yoffset = viewport.getViewPosition().y;
			
			if (!listenerAdded) {
				viewport.addChangeListener(this);
				listenerAdded = true;
			}
		}

		g.drawImage(bgBild, 0, yoffset, null);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		this.repaint();
	}

}
