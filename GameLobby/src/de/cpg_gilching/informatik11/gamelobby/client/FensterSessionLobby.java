package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;


/**
 * Die View-Komponente der "Server-Lobby".
 */
public class FensterSessionLobby {
	
	private BildschirmSessionLobby sessionLobby;
	
	private JFrame fenster;
	private JLabel labelSpielname;
	private JPanel panelSpieler;
	private JButton ablehnenBtn;
	private JButton annehmenBtn;
	
	
	public FensterSessionLobby(BildschirmSessionLobby lobby) {
		this.sessionLobby = lobby;
		
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				JPanel hauptPanel = new JPanel();
				hauptPanel.setLayout(null);
				hauptPanel.setPreferredSize(new Dimension(250, 500));
				
				labelSpielname = new JLabel();
				labelSpielname.setText(sessionLobby.getBeschreibung().getBezeichnung());
				labelSpielname.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
				labelSpielname.setBounds(0, 10, 250, 40);
				labelSpielname.setHorizontalAlignment(JLabel.CENTER);
				hauptPanel.add(labelSpielname);
				
				panelSpieler = new JPanel();
				panelSpieler.setLayout(new BoxLayout(panelSpieler, BoxLayout.Y_AXIS));
				
				JPanel spielerPanelWrapper = new JPanel();
				spielerPanelWrapper.setLayout(new BorderLayout());
				spielerPanelWrapper.setBounds(10, 60, 230, 330);
				spielerPanelWrapper.add(panelSpieler, BorderLayout.NORTH);
				hauptPanel.add(spielerPanelWrapper);
				
				annehmenBtn = new JButton("Annehmen");
				annehmenBtn.setBounds(10, 400, 230, 40);
				annehmenBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						annehmen();
					}
				});
				hauptPanel.add(annehmenBtn);
				
				ablehnenBtn = new JButton("Lobby Verlassen");
				ablehnenBtn.setBounds(10, 450, 230, 40);
				ablehnenBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sessionLobby.lobbyVerlassen();
					}
				});
				hauptPanel.add(ablehnenBtn);
				
				
				fenster = new JFrame("Session-Lobby");
				fenster.setResizable(false);
				fenster.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				fenster.setLayout(new BorderLayout());
				fenster.add(hauptPanel, BorderLayout.NORTH);
				fenster.pack();
				fenster.setLocationRelativeTo(null);
				fenster.setVisible(true);
				
				fenster.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						sessionLobby.lobbyVerlassen();
					}
				});
			}
		});
	}
	
	private void annehmen() {
		// schon auf Event-Thread
		annehmenBtn.setEnabled(false);
		sessionLobby.lobbyAnnehmen();
	}
	
	public void spielerListeAktualisieren(Collection<String> spieler, Collection<String> fertig) {
		// unabhängige Listen erstellen
		final Collection<String> alleSpieler = new ArrayList<String>(spieler);
		final Set<String> fertigeSpieler = new HashSet<String>(fertig);
		
		// auf Event-Thread ausführen
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				panelSpieler.removeAll();
				
				panelSpieler.add(Box.createVerticalStrut(10));
				
				for (String spielerName : alleSpieler) {
					JLabel nameLabel = new JLabel(spielerName);
					nameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
					
					Box box = Box.createHorizontalBox();
					box.add(Box.createHorizontalStrut(10));
					box.add(nameLabel);
					box.add(Box.createHorizontalGlue());
					
					if (fertigeSpieler.contains(spielerName)) {
						JLabel bereitLabel = new JLabel("Bereit");
						bereitLabel.setForeground(Color.green);
						
						box.add(bereitLabel);
						box.add(Box.createHorizontalStrut(10));
					}
					
					panelSpieler.add(box);
					panelSpieler.add(Box.createVerticalStrut(10));
				}
				
				panelSpieler.revalidate();
				panelSpieler.repaint();
			}
		});
	}
	
	public void fensterSchliessen() {
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				fenster.dispose();
			}
		});
	}
	
}
