package vue;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import static constantes.Constantes.*;
import static  main.Coeur.CONTROLEUR;

public class Fenetre extends JFrame implements KeyListener{
	
	public Fenetre() {
		this.setTitle(TITRE_FENETRE);
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.addKeyListener(this);
		this.setVisible(false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		CONTROLEUR.keyTyped(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		CONTROLEUR.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		CONTROLEUR.keyReleased(e);
	}
}