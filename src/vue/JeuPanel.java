package vue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

import entitees.abstraites.Entitee;
import entitees.fixes.Amibe;
import entitees.fixes.Mur;
import entitees.fixes.MurEnTitane;
import entitees.fixes.Poussiere;
import entitees.fixes.Sortie;
import entitees.tickables.Bombe;
import entitees.tickables.Diamant;
import entitees.tickables.Explosion;
import entitees.tickables.Libellule;
import entitees.tickables.Luciole;
import entitees.tickables.MurMagique;
import entitees.tickables.Pierre;
import entitees.tickables.Rockford;
import main.Constantes;
import main.Partie;

/**
 * La classe JeuPanel hérite de JPanel et sert à afficher un niveau du jeu quand
 * celui-ci est en mode fenêtré.
 * Elle dispose d'un tableau a 2 dimensions d'entitees et d'un compteur de FPS.
 * A chaque reset/changement de niveau correspond une instance de JeuPanel.
 *
 * @author Murloc
 * @see JPanel
 */
public class JeuPanel extends JPanel {

    /**
     * Le tableau d'entitées qui situe dans l'espace les elements d'une partie.
     *
     * @see entitees.abstraites.Entitee
     */
    private Entitee[][] map;

    /**
     * Le compteur de FPS qui s'incrémente à chaque appel de
     * {@link vue.JeuPanel#paintComponent(Graphics)}.
     */
    private long compteurFPS;

    /**
     * Constructeur JeuPanel.
     * Initialise le tableau d'entitées en allant cherchant celui du niveau
     * actuellement en cours dans Partie.
     * Initialise le compteur de FPS à 0.
     */
    public JeuPanel() {
        map = Partie.gererNiveau.getNiveau().getMap();
        compteurFPS = 0;
    }

    /**
     * La methode servant a dessiner le niveau.
     * Appellée par la fenêtre à chaque {@link vue.Fenetre#repaint()}.
     * Elle se sert du tableau d'entitees et du compteur de FPS.
     * Elle récupère la largeur du Panel {@link vue.JeuPanel#getWidth()}, le
     * divise par la largeur du niveau, ainsi a chaque entitée du niveau peut
     * être attribuée une coordonée. Idem pour la hauteur.
     *
     * @see vue.Sprites#oneFrame(long, Rockford)
     * @see vue.JeuPanel#getSprite(Entitee)
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Incrémentation du compteur.
        compteurFPS++;

        // On indique à la classe qui gère les sprites qu'une frame va avoir
        // lieu.
        Sprites.oneFrame(compteurFPS, Partie.gererNiveau.getNiveau().getRockford());

        // Get de la largeur et l'hauteur du panel.
        int largeur_case;
        int hauteur_case;
        largeur_case = getWidth() / map.length;
        hauteur_case = getHeight() / map[0].length;

        // Dessine le fond noir.
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Parcours de toutes les entitees de la map.
        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {

                // Get l'image suivant l'entité.
                Image image = getSprite(map[j][i]);

                // Dessine l'image en se basant sur i et j pour les coordonnées.
                if (image != null) {
                    g.drawImage(image, (j) * largeur_case, (i) * hauteur_case, largeur_case, hauteur_case, this);
                }
            }
        }

    }

    /**
     * Cette methode prend en parametre une entitee et renvoie une image en
     * fonction.
     * Elle va chercher l'image dans la base de données de la classe
     * {@link vue.Sprites}.
     *
     * @param e L'entitée dont on veut l'image.
     *
     * @return L'image propre à l'entitée, null si non trouvée.
     */
    private Image getSprite(Entitee e) {

        // Get de la classe de l'entitée.
        Class<? extends Entitee> classe = e.getClass();

        // Comparaison de la classe avec les classes des entitées existantes,
        // renvoie l'image correspondante.
        if (classe.equals(Mur.class)) {
            return Sprites.SPRITES_MURS.get(0);
        } else if (classe.equals(Diamant.class)) {
            return Sprites.SPRITES_DIAMANTS.get(0);
        } else if (classe.equals(Amibe.class)) {
            return Sprites.SPRITES_AMIBES.get(0);
        } else if (classe.equals(Luciole.class)) {
            return Sprites.SPRITES_LUCIOLES.get(0);
        } else if (classe.equals(Libellule.class)) {
            return Sprites.SPRITES_LIBELLULES.get(0);
        } else if (classe.equals(MurEnTitane.class)) {
            return Sprites.SPRITES_MURS_EN_TITANE.get(0);
        } else if (classe.equals(Pierre.class)) {
            return Sprites.SPRITES_PIERRES.get(0);
        } else if (classe.equals(Poussiere.class)) {
            return Sprites.SPRITES_POUSSIERES.get(0);
        } else if (classe.equals(MurMagique.class)) {
            return Sprites.SPRITES_MURS_MAGIQUES.get(0);
        } else if (classe.equals(Explosion.class)) {
            return Sprites.SPRITES_EXPLOSIONS.get(0);
        } else if (classe.equals(Bombe.class)) {
            /*
             * Algo permettant le clignotement de la bombe avant l'explosion,
			 * renvoie une image de la bombe noire ou celle de la bombe rouge
			 * (suivant l'évolution du compteur de FPS), pour créer l'illusion
			 * du clignotement.
			 */
            if (((Bombe) e).getTempsRestantAvantExplosion() < 1) {
                return Sprites.SPRITES_BOMBE.get(1);
            }

            if (compteurFPS % ((Bombe) e).getTempsRestantAvantExplosion() == 0) {
                return Sprites.SPRITES_BOMBE.get(1);
            }
            if (((Bombe) e).getTempsRestantAvantExplosion() + Constantes.BOOM / 3 > Constantes.BOOM) {
                return Sprites.SPRITES_BOMBE.get(0);
            }
            return Sprites.SPRITES_BOMBE.get(0);
        } else if (classe.equals(Rockford.class)) {
			/*
			 * Si le camouflage de Rockford est actif renvoie l'image de
			 * rockford camouflé, sinon renvoie l'image de Rockford.
			 */
            if (Partie.gererNiveau.getNiveau().getRockford().camouflageActif()) {
                return Sprites.SPRITES_CAMOUFLAGE.get(0);
            } else {
                return Sprites.spriteRockford;
            }
        } else if (classe.equals(Sortie.class)) {
			/*
			 * Si la porte est ouverte renvoit l'image de la sortie, sinon
			 * renvoie l'image d'une pierre.
			 */
            if (((Sortie) e).isOuvert()) {
                return Sprites.SPRITES_SORTIE.get(1);
            } else {
                return Sprites.SPRITES_SORTIE.get(0);
            }

        } else {
            return null;
        }
    }

}
