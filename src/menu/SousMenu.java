package menu;

import loader.Loader;
import main.Partie;
import outils.Comparaison;
import outils.Ecrivain;
import outils.Score;

/***
 * La classe SousMenu n'est jamais instanci�e. contient des m�thodes statics
 * g�rant le lancement et la direction que doit prendre le programme au
 * lancement.
 * 
 * Elle lance des m�thodes de la classe {@link Partie}.
 * 
 * @author Murloc
 *
 */
public class SousMenu {

	/**
	 * Lance une partie d'un seul niveau.
	 * 
	 * @param cheminFichierBDCFF
	 *            Le chemin du fichier BDCFF o� se trouve le niveau.
	 * @param niveau
	 *            Le num�ro du niveau voulu.
	 */
	public static void lancerNiveau(String cheminFichierBDCFF, int niveau) {
		Partie.commencerPartie(cheminFichierBDCFF, niveau);
	}

	/**
	 * Lance une partie de tous les niveaux du fichier BDCFF.
	 * 
	 * @param cheminFichierBDCFF
	 *            Le chemin du fichier BDCFF o� se trouve le niveau.
	 */
	public static void lancerTousLesNiveaux(String cheminFichierBDCFF) {
		Partie.commencerPartie(cheminFichierBDCFF);
	}

	/**
	 * Affiche sur la console les infos d'un fichier BDCFF pass� en param�tre.
	 * 
	 * @param cheminFichierBDCFF
	 *            Le chemin du fichier BDCFF o� se trouve le niveau.
	 */
	public static void lireInfos(String cheminFichierBDCFF) {
		System.out.println(Loader.lireInfos(cheminFichierBDCFF));
	}

	/**
	 * Cr�e un fichier DASH contenant le r�sultat du meilleur essai parmis les
	 * strat�gies tent�s (Seulement les strat�gies non �volutives ici).
	 * 
	 * @param strategie
	 *            La strat�gie voulue.
	 * @param cheminFichierBDCFF
	 *            Le chemin du fichier BDCFF o� se trouve le niveau.
	 * @param niveau
	 *            Le num�ro du niveau voulu.
	 */
	public static void calculerStrategie(String strategie, String cheminFichierBDCFF, int niveau) {
		System.out.println("Calcul en cours...");
		Partie.calculerStrategie(strategie, cheminFichierBDCFF, niveau);
	}

	/**
	 * 
	 * Cr�e un fichier DASH contenant le r�sultat du meilleur essai parmis les
	 * strat�gies tent�s (Seulement les strat�gies �volutives ici).
	 * 
	 * @param strategie
	 *            La strat�gie voulue.
	 * @param nbGenerations
	 * @param cheminFichierBDCFF
	 *            Le chemin du fichier BDCFF o� se trouve le niveau.
	 * @param niveau
	 *            Le num�ro du niveau voulu.
	 */
	public static void calculerStrategieEvol(String strategie, int nbGenerations, String cheminFichierBDCFF,
			int niveau) {
		System.out.println("Calcul en cours...");
		Partie.calculerStrategieEvolue(strategie, nbGenerations, cheminFichierBDCFF, niveau);
	}

	/**
	 * Rejoue une partie d'un niveau en jouant les d�placements se trouvant dans
	 * le fichier DASH pass� en param�tre.
	 * 
	 * @param cheminFichierDASH
	 *            Le chemin du fichier DASH.
	 * @param cheminFichierBDCFF
	 *            Le chemin du fichier BDCFF o� se trouve le niveau.
	 * @param niveau
	 *            Le num�ro du niveau voulu.
	 */
	public static void rejouerNiveau(String cheminFichierDASH, String cheminFichierBDCFF, int niveau) {
		Partie.jouerFichier(cheminFichierBDCFF, niveau, Ecrivain.lireParcours(cheminFichierDASH));
	}

	/**
	 * Affiche les meilleurs r�sultats des deux strat�gies voulues.
	 * 
	 * @param nombrePartie
	 *            Le nombre de partie voulues.
	 * @param strategie1
	 *            La strat�gie 1 voulue.
	 * @param strategie2
	 *            La strat�gie 2 voulue.
	 * @param cheminFichierBDCFF
	 *            Le chemin du fichier BDCFF o� se trouve le niveau.
	 * @param niveau
	 *            Le num�ro du niveau voulu.
	 */
	public static void simulerNiveau(int nombrePartie, String strategie1, String strategie2, String cheminFichierBDCFF,
			int niveau) {
		Partie.simulation = true;
		System.out.println("Calcul en cours...");
		Comparaison comp1 = new Comparaison(nombrePartie);
		Comparaison comp2 = new Comparaison(nombrePartie);
		for (int i = 0; i < nombrePartie; i++) {
			Score s = Partie.calculerStrategie(strategie1, cheminFichierBDCFF, niveau);
			comp1.addScore(s.getScore(), s.getParcours());
			System.out.println((i + 1) + "/" + (nombrePartie * 2));
		}
		comp1.fin();
		for (int i = 0; i < nombrePartie; i++) {
			Score s = Partie.calculerStrategie(strategie2, cheminFichierBDCFF, niveau);
			comp2.addScore(s.getScore(), s.getParcours());
			System.out.println((nombrePartie + i + 1) + "/" + (nombrePartie * 2));
		}
		comp2.fin();
		System.out.println("\n" + strategie1 + " :\nDistance Moyenne : " + comp1.getDistanceMoyenne());
		System.out.println("Score Moyen : " + comp1.getScoreMoyen());
		System.out.println("Temps Moyen : " + comp1.getTempsMoyen() + " millisecondes.\n");

		System.out.println(strategie2 + " :\nDistance Moyenne : " + comp2.getDistanceMoyenne());
		System.out.println("Score Moyen : " + comp2.getScoreMoyen());
		System.out.println("Temps Moyen : " + comp2.getTempsMoyen() + " millisecondes.");
		System.exit(1);
	}
}
