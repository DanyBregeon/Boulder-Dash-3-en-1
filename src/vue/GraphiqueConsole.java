package vue;

import java.util.List;

import entitees.abstraites.Entitee;
import entitees.fixes.Amibe;
import entitees.fixes.Mur;
import entitees.fixes.MurEnTitane;
import entitees.fixes.Poussiere;
import entitees.fixes.Sortie;
import entitees.tickables.Diamant;
import entitees.tickables.Libellule;
import entitees.tickables.Luciole;
import entitees.tickables.MurMagique;
import entitees.tickables.Pierre;
import entitees.tickables.Rockford;
import loader.Niveau;
import main.Partie;

/**
 * La classe GraphiqueConsole n'est jamais instanciée, elle sert uniquement à
 * stocker des méthodes en rapport avec l'affichage du jeu en mode console.
 *
 * @author Murloc
 */
public class GraphiqueConsole {

    /**
     * Affiche un niveau en mode console.
     *
     * @param niveau Le niveau à afficher.
     */
    public static void afficher(Niveau niveau) {
        // Get la map du niveau.
        Entitee[][] map = niveau.getMap();

		/*
         * Initialise un string auquel va être concaténé la représentation du
		 * niveau, ce string sera affiché à la fin de la méthode.
		 */
        String s = "";

        // Concatène beaucoup de retours à la ligne pour un meilleur rendu.
        for (int i = 0; i <= 30; i++) {
            s += "\n";
        }

		/*
         * Concatène diverses informations.
		 */
        s += ("Diamants : " + Partie.gererNiveau.getNbDiamants() + "/"
              + Partie.gererNiveau.getNiveau().getDiamonds_required() + "\n");
        s += "Score : " + Partie.gererNiveau.getScore() + "			Temps restant : "
             + Partie.gererNiveau.getTempsRestant() + " 		";
        s += "Niveau : " + Partie.niveau + "/" + Partie.ensembleDeNiveau.getNombre_de_niveaux() + "\n";
        s += "Points/diamant : " + (Partie.gererNiveau.getNiveau().getDiamond_value()) + "	  (diamants bonus) : "
             + Partie.gererNiveau.getNiveau().getDiamond_value_bonus();
        s += "\n\n";

        // Parcours de toutes les entitees de la map.
        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {
                // Concat�ne un caract�re sp�cifique suivant l'entit�e.
                s += getCharDeEntitee(map[j][i]);
            }
            s += "\n";
        }
        // Affiche le string.
        System.out.println(s);
    }

    /**
     * Retourne un caractère spéficique suivant l'entitée en paramètre.
     *
     * @param e L'entitée dont on veut le caractère.
     *
     * @return Le caractère propre à l'entitée. Renvoie ' ' si l'entiée est
     * inconnue (ou si c'est l'entitée Vide).
     */
    public static char getCharDeEntitee(Entitee e) {
        // Get la classe de l'entitée.
        Class<? extends Entitee> l = e.getClass();

        // Crée le char qui va être renvoyé.
        char s = ' ';

		/*
		 * Compare la classe avec les classes des entitées, puis affecte le char
		 * correspondant au string.
		 */
        if (l.equals(Rockford.class)) {
            s = 'P';
        } else if (l.equals(Mur.class)) {
            s = 'w';
        } else if (l.equals(Diamant.class)) {
            s = 'd';
        } else if (l.equals(Amibe.class)) {
            s = 'a';
        } else if (l.equals(Luciole.class)) {
            s = 'q';
        } else if (l.equals(Libellule.class)) {
            s = 'o';
        } else if (l.equals(MurEnTitane.class)) {
            s = 'W';
        } else if (l.equals(Pierre.class)) {
            s = 'r';
        } else if (l.equals(Poussiere.class)) {
            s = '.';
        } else if (l.equals(Sortie.class)) {
            if (((Sortie) e).isOuvert()) {
                s = 'X';
            } else {
                s = 'W';
            }
        } else if (l.equals(MurMagique.class)) {
            s = 'M';
        }
        return s;
    }

    /**
     * Affiche le score à la fin d'un niveau.
     *
     * @param niveau Le niveau fini.
     * @param score Le score à afficher.
     */
    public static void afficherScoreUnNiveau(int niveau, int score) {
        if (!Partie.IA && !Partie.lecture) {
            System.out.println("FIN DU JEU , SCORE DU NIVEAU " + niveau + " : " + score + ".\n");
        }
    }

    /**
     * Affiche les scores à la fin d'une suite de niveaux.
     *
     * @param scores La liste des scores à afficher.
     */
    public static void afficherScoreTousLesNiveaux(List<Integer> scores) {
        System.out.println("FIN DU JEU , SCORE DES NIVEAUX : \n");
        for (int i = 0; i < scores.size(); i++) {
            System.out.println("                         Niveau " + (i + 1) + " : " + scores.get(i) + "\n");
        }
        System.out.println("\n");
    }

}
