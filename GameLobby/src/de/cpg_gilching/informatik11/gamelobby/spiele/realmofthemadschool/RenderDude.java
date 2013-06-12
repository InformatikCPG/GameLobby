package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

public class RenderDude extends EntityRenderer {

	Image phinten = Helfer.bildLaden("realm/playerhinten.png");
	Image pvorne = Helfer.bildLaden("realm/playervorne.png");
	Image plinks = Helfer.bildLaden("realm/playerlinks.png");
	Image prechts = Helfer.bildLaden("realm/playerrechts.png");
	Image Health = Helfer.bildLaden("realm/healthbarfull.png");
	Image Health1 = Helfer.bildLaden("realm/healthbar-1.png");
	Image Health2 = Helfer.bildLaden("realm/healthbar-2.png");
	Image Health3 = Helfer.bildLaden("realm/healthbar-3.png");
	Image Health4 = Helfer.bildLaden("realm/healthbar-4.png");
	Image Health5 = Helfer.bildLaden("realm/healthbar-5.png");
	Image Health6 = Helfer.bildLaden("realm/healthbar-6.png");
	Image Health7 = Helfer.bildLaden("realm/healthbar-7.png");
	Image Health8 = Helfer.bildLaden("realm/healthbar-8.png");
	Image Health9 = Helfer.bildLaden("realm/healthbar-9.png");
	Image Health10 = Helfer.bildLaden("realm/healthbar-10.png");
	Image Health11 = Helfer.bildLaden("realm/healthbar-11.png");
	Image Health12 = Helfer.bildLaden("realm/healthbar-12.png");
	Image Health13 = Helfer.bildLaden("realm/healthbar-13.png");
	Image Health14 = Helfer.bildLaden("realm/healthbar-14.png");
	Image Health15 = Helfer.bildLaden("realm/healthbar-15.png");
	Image Health16 = Helfer.bildLaden("realm/healthbar-16.png");
	Image Health17 = Helfer.bildLaden("realm/healthbar-17.png");
	Image Health18 = Helfer.bildLaden("realm/healthbar-18.png");
	Image Health19 = Helfer.bildLaden("realm/healthbar-19.png");
	Image Health20 = Helfer.bildLaden("realm/healthbar-20.png");
	
	@Override
	public void rendern(Graphics2D g) {
		Image bild=null;
		switch (ausrichtung) {
		case 0:
			bild=phinten;
			break;
		case 1:
			bild=prechts;
			break;
		case 2:
			bild=pvorne;
			break;
		case 3:
			bild=plinks;
			break;
		}
			
			Image Hbild=null;
			switch (health) {
			case 20:
				Hbild=Health;
				break;
			case 19:
				Hbild=Health1;
				break;
			case 18:
				Hbild=Health2;
				break;
			case 17:
				Hbild=Health3;
				break;
			case 16:
				Hbild=Health4;
				break;
			case 15:
				Hbild=Health5;
				break;
			case 14:
				Hbild=Health6;
				break;
			case 13:
				Hbild=Health7;
				break;
			case 12:
				Hbild=Health8;
				break;
			case 11:
				Hbild=Health9;
				break;
			case 10:
				Hbild=Health10;
				break;
			case 9:
				Hbild=Health11;
				break;
			case 8:
				Hbild=Health12;
				break;
			case 7:
				Hbild=Health13;
				break;
			case 6:
				Hbild=Health14;
				break;
			case 5:
				Hbild=Health15;
				break;
			case 4:
				Hbild=Health16;
				break;
			case 3:
				Hbild=Health17;
				break;
			case 2:
				Hbild=Health18;
				break;
			case 1:
				Hbild=Health19;
				break;
			case 0:
				Hbild=Health20;
				break;

		}
				
		g.drawImage (bild,x-20, y-20, null);		
	    g.drawImage (Hbild,x-20, y-30, null);
	    g.setColor(Color.orange);	
	    g.fillRect(x+45,y-15, 5, charge); 
	    g.setColor(Color.blue);	
	    g.fillRect(x-25,y-15, 5, mana);
	    
	}
	
}
