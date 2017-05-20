package vue;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

import main.Constantes;

import static main.Coeur.CONTROLEUR;
import static main.Constantes.HEIGHT_FENETRE;
import static main.Constantes.WIDTH_FENETRE;

/**
 * La classe Fenétre hérite de JFrame et sert à gérer la fenètre du mode
 * graphique du jeu.
 * Elle implémente la classe {@link KeyListener} afin de pouvoir récupérer les
 * entrées clavier.
 *
 * @author Murloc
 * @see JFrame
 */
public class Fenetre extends JFrame implements KeyListener {

    /**
     * Les FPS.
     */
    private int fps;

    /**
     * Les TPS (ticks par secondes).
     */
    private int tps;

    /**
     * Constructeur Fenetre.
     * Elle ne prend pas de paramètres mais trouve les différentes variables
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
     * Méthode servant à rafraichir le titre de la fenètre afin d'y inscrire le
     * nombre de frames par secondes et/ou le nombre de ticks par secondes en
     * foncion des booleens de la classe {@link main.Constantes}.
     */
    public void setTitre() {

        if (Constantes.SYSOUT_FPS && Constantes.SYSOUT_TPS) {
            this.setTitle("[" + fps + " FPS , " + tps + " TPS] " + Constantes.TITRE_FENETRE);
        } else if (Constantes.SYSOUT_FPS) {
            this.setTitle("[" + fps + " FPS] " + Constantes.TITRE_FENETRE);
        } else if (Constantes.SYSOUT_TPS) {
            this.setTitle("[" + tps + " TPS] " + Constantes.TITRE_FENETRE);
        }
    }

    /**
     * Méthode implémentée par l'interface {@link KeyListener} appelée quand une
     * touche vient d'être tapée. Ne fais rien car
     * {@link Fenetre#keyPressed(KeyEvent)} suffit.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Méthode implémentée par l'interface {@link KeyListener} appelée quand une
     * touche vient d'être enfoncée.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        CONTROLEUR.keyPressed(e);
    }

    /**
     * Méthode implémentée par l'interface {@link KeyListener} appelée quand une
     * touche vient d'être relâchée.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        CONTROLEUR.keyReleased(e);
    }

    /**
     * Un setter.
     *
     * @param fps L'objet en question.
     */
    public void setFps(int fps) {
        this.fps = fps;
    }

    /**
     * Un setter.
     *
     * @param tps L'objet en question.
     */
    public void setTps(int tps) {
        this.tps = tps;
    }
}
