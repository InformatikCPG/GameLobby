package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;


/**
 * Die View-Komponente der "Server-Lobby".
 */
public class FensterSessionLobby {
	
	private JFrame fenster;
	
	private JLabel labelSpielname;
	private JPanel panelSpieler;
	private JButton ablehnenBtn;
	private JButton annehmenBtn;
	
	public FensterSessionLobby() {
		
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				JPanel hauptPanel = new JPanel();
				hauptPanel.setLayout(null);
				hauptPanel.setPreferredSize(new Dimension(250, 500));
				
				panelSpieler = new JPanel();
				panelSpieler.setLayout(null);
				
				labelSpielname = new JLabel();
				labelSpielname.setText("Spiel Nr. 1337");
				labelSpielname.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
				labelSpielname.setBounds(0, 0, 250, 50);
				labelSpielname.setHorizontalAlignment(JLabel.CENTER);
				hauptPanel.add(labelSpielname);
				
				ablehnenBtn = new JButton("Lobby Verlassen");
				ablehnenBtn.setBounds(10, 450, 230, 40);
				ablehnenBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("abgelehnt");
					}
				});
				hauptPanel.add(ablehnenBtn);
				
				annehmenBtn = new JButton("Annehmen");
				annehmenBtn.setBounds(10, 400, 230, 40);
				annehmenBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("angenommen");
					}
				});
				hauptPanel.add(annehmenBtn);
				
				
				fenster = new JFrame("Server-Lobby");
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
						fenster.dispose();
					}
				});
			}
		});
	}
	
	public void fensterSchliessen() {
		fenster.dispose();
	}
	
}
