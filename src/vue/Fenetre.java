package vue;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import main.Constantes;

import static main.Coeur.CONTROLEUR;
import static main.Constantes.*;

/**
 * La classe Fen�tre h�rite de JFrame et sert � g�rer la fen�tre du mode
 * graphique du jeu.
 * 
 * Elle impl�mente la classe {@link KeyListener} afin de pouvoir r�cup�rer les
 * entr�es clavier.
 * 
 * @see JFrame
 * 
 * @author Murloc
 *
 */
public class Fenetre extends JFrame implements KeyListener {

	/**
	 * Constructeur Fenetre.
	 * 
	 * Elle ne prend pas de param�tres mais trouve les diff�rentes variables
	 * dans la classe {@link main.Constantes}.
	 */
	public Fenetre() {
		this.setTitle(Constantes.TITRE_FENETRE);
		this.setSize(WIDTH_FENETRE, HEIGHT_FENETRE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.addKeyListener(this);
		this.setVisible(false);
	}

	/**
	 * M�thode servant � rafraichir le titre de la fen�tre afin d'y inscrire le
	 * nombre de frames par secondes et/ou le nombre de ticks par secondes en
	 * foncion des booleens de la classe {@link main.Constantes}.
	 */
	public void setTitre() {
		if (Constantes.SYSOUT_FPS && Constantes.SYSOUT_TPS) {
			this.setTitle("[" + Constantes.SYSOUT_FPS + " FPS , " + Constantes.SYSOUT_TPS + " TPS] "
					+ Constantes.TITRE_FENETRE);
		} else if (Constantes.SYSOUT_FPS) {
			this.setTitle("[" + Constantes.SYSOUT_FPS + " FPS] " + Constantes.TITRE_FENETRE);
		} else if (Constantes.SYSOUT_TPS) {
			this.setTitle("[" + Constantes.SYSOUT_TPS + " TPS] " + Constantes.TITRE_FENETRE);
		}
	}

	/**
	 * M�thode impl�ment�e par l'interface {@link KeyListener} appel�e quand une
	 * touche vient d'�tre tap�e. Ne fais rien car
	 * {@link Fenetre#keyPressed(KeyEvent)} suffit.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * M�thode impl�ment�e par l'interface {@link KeyListener} appel�e quand une
	 * touche vient d'�tre enfonc�e.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		CONTROLEUR.keyPressed(e);
	}

	/**
	 * M�thode impl�ment�e par l'interface {@link KeyListener} appel�e quand une
	 * touche vient d'�tre rel�ch�e.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		CONTROLEUR.keyReleased(e);
	}
}
