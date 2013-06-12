package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Das JPanel in der Server-Lobby, das die verbundenen Spieler anzeigt und die Auswählen/Abwählen - Buttons verwaltet.
 */
public class PanelSpielerEintrag extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private Color colorAktiv = new Color(100, 215, 100);
	private Color colorInaktiv = new Color(200, 200, 200);
	
	
	private BildschirmServerLobby serverLobby;
	private SpielerZustand spielerModel;
	
	private JButton auswahlBtn;
	
	public PanelSpielerEintrag(SpielerZustand spieler, BildschirmServerLobby serverLobby) {
		spielerModel = spieler;
		this.serverLobby = serverLobby;
		
		spielerModel.setAnzeige(this);
		
		auswahlBtn = new JButton();
		auswahlBtn.addActionListener(new SynchronerListener(this, serverLobby.getClient()));
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 4));
		setPreferredSize(new Dimension(1, 32));
		
		add(new JLabel(spielerModel.getName()), BorderLayout.WEST);
		add(auswahlBtn, BorderLayout.EAST);
		
		zustandVerändert();
	}
	
	@Override
	public void run() {
		// beim Klicken des Auswählen/Abwählen-Buttons (auf Hauptthread)
		serverLobby.spielerAuswahlUmschalten(spielerModel);
	}
	
	public void zustandVerändert() {
		if (spielerModel.istAusgewählt()) {
			setBackground(colorAktiv);
			auswahlBtn.setText("Abwählen");
		}
		else {
			setBackground(colorInaktiv);
			auswahlBtn.setText("Auswählen");
		}
	}
	
}
