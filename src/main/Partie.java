package main;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import controleurs.ControleurConsole;
import ia.Ia;
import ia.IaDirective;
import ia.IaDirectiveEvolue;
import ia.IaEvolue;
import ia.IaRandom;
import loader.EnsembleDeNiveaux;
import loader.Loader;
import menu.SousMenu;
import outils.Paire;
import outils.Score;
import outils.SonToolKit;
import vue.FinPanel;
import vue.GraphiqueConsole;
import vue.JeuPanel;
import vue.ScorePanel;

/**
 * La classe Partie gère une partie (c'est à dire tous les essais de tous les
 * niveaux d'un fichier BDCFF, ou d'un seul niveau si l'utilisateur l'a choisi).
 * 
 * @author celso
 *
 */
public class Partie {
	/**
	 * Stock les scores obtenus à chaque niveau.
	 */
	public static final List<Integer> SCORES = new ArrayList<Integer>();
	/**
	 * Stock le chemin du fichier BDCFF utilisé sur cette partie.
	 */
	public static String cheminFichier;
	/**
	 * Booléen utilisé par l'ia évolutive pour ne pas provoquer de bug quand les
	 * générations sont finies.
	 */
	public static boolean finiEvolution;

	/**
	 * Stock l'ensemble de niveaux du fichier BDCFF.
	 */
	public static EnsembleDeNiveaux ensembleDeNiveau;

	/**
	 * Indique le numéro du niveau actuel aucun on joue.
	 */
	public static int niveau;

	/**
	 * L'objet qui permet de gérer l'essai actuel dans lequel le joueur/ia joue.
	 */
	public static GererNiveau gererNiveau;

	/**
	 * Booleen indiquant si l'utilisateur veut jouer à tous les niveaux ou a un
	 * seul, vrai = tous les niveaux.
	 */
	public static boolean tousLesNiveaux;

	/**
	 * Booleen indiquant si c'est l'ia qui joue.
	 */
	public static boolean IA;

	/**
	 * L'ia qui joue si tel est le cas.
	 */
	public static Ia ia;

	/**
	 * Booleens indiquant c'est on est en mode lecture ou en mode simulation.
	 */
	public static boolean lecture, simulation;

	/**
	 * String stockant le chemin pris par le joueur durant le GererNiveau
	 * actuel.
	 */
	public static String parcours;

	/**
	 * Objet servant à effectuer des sons globaux.
	 */
	public static SonToolKit sons = new SonToolKit();

	/**
	 * Stock la date actuel, utile pour sauvegarder à la fin l'essai.
	 */
	public static DateFormat df = new SimpleDateFormat("dd:MM:yyyy_HH:mm:ss");

	/**
	 * Utile pour la date.
	 */
	public static Date today = Calendar.getInstance().getTime();

	/*
	 * Utile pour la date.
	 */
	public static String dateDebut = df.format(today);

	/**
	 * Méthode appellée pour commener toutes les parties du niveau BDCFF dont le
	 * chemin est en paramètre.
	 * 
	 * @param chemin
	 *            Chemin du fichier BDCFF.
	 */
	public static void commencerPartie(String chemin) {
		ensembleDeNiveau = Loader.charger_ensemble_de_niveaux(chemin);
		tousLesNiveaux = true;
		niveau = 1;
		lancerNiveau();
	}

	/**
	 * Méthode appellée pour commener le niveau choisi du fichier BDCFF dont le
	 * chemin est en paramètre.
	 * 
	 * @param chemin
	 *            Chemin du fichier BDCFF.
	 * @param niveau
	 *            Niveau choisi.
	 */
	public static void commencerPartie(String chemin, int niveau) {
		ensembleDeNiveau = Loader.charger_ensemble_de_niveaux(chemin);
		tousLesNiveaux = false;
		Partie.niveau = niveau;
		lancerNiveau();
	}

	/**
	 * Méthode appellée pour commener le niveau choisi du fichier BDCFF dont le
	 * chemin est en paramètre, en mode lecture, c'est à dire que l'ia va jouer
	 * le parcour en paramètre.
	 * 
	 * @param cheminFichierBDCFF
	 *            Chemin du fichier BDCFF.
	 * @param niveau
	 *            Niveau choisi.
	 * @param parcours
	 *            Parcours à executer.
	 */
	public static void jouerFichier(String cheminFichierBDCFF, int niveau, String parcours) {
		ensembleDeNiveau = Loader.charger_ensemble_de_niveaux(cheminFichierBDCFF);
		cheminFichier = cheminFichierBDCFF;
		tousLesNiveaux = false;
		Partie.niveau = niveau;
		lecture = true;
		Partie.parcours = parcours;
		lancerNiveau();
	}

	/**
	 * Méthode appellée pour commener le niveau choisi du fichier BDCFF dont le
	 * chemin est en paramètre, en mode lecture, c'est à dire que l'ia va jouer
	 * le parcour en paramètre.
	 * 
	 * Retourne un score, utilisé durant les simulations et les ia évolutives
	 * pour comparer les scores.
	 * 
	 * @param chemin
	 *            Chemin du fichier BDCFF.
	 * @param niveau
	 *            Niveau choisi.
	 * @param parcours
	 *            Parcours à executer.
	 * @return Le score obtenu.
	 */
	public static Score jouerFichierScore(String chemin, int niveau, String parcours) {
		ensembleDeNiveau = Loader.charger_ensemble_de_niveaux(chemin);
		cheminFichier = chemin;
		tousLesNiveaux = false;
		Partie.niveau = niveau;
		lecture = true;
		Partie.parcours = parcours;
		String parcours2 = parcours;
		gererNiveau = new GererNiveau(ensembleDeNiveau.getNiveaux().get(niveau - 1).clone());
		Score s;
		String parcoursParcouru = "";
		int score = 0;
		List<Paire<Integer, Long>> liste = null;
		while (parcours.length() > 0 && !gererNiveau.isDemandeReset() && !gererNiveau.isDemandeFin()) {
			char direction = parcours.charAt(0);
			parcours = parcours.substring(1, parcours.length());
			parcoursParcouru += direction;
			liste = gererNiveau.getListeDiamants();
			score = gererNiveau.getScore();
			if (gererNiveau.tickLecture(direction) || gererNiveau.getNiveau().getRockford().isMort()) {
				break;
			}
		}
		s = new Score(score, parcoursParcouru.length(), liste);
		s.setChemin(parcours2);
		if (gererNiveau.isDemandeFin()) {
			s.setFini(true);
		} else {
			s.setFini(false);
		}
		return s;
	}

	/**
	 * Reset le niveau actuel, c'est à dire change d'objet GererNiveau.
	 */
	public static void resetNiveau() {
		if (IA) {
			gererNiveau = new GererNiveau(ensembleDeNiveau.getNiveaux().get(niveau - 1).clone());
			if (ia != null)
				ia.reset();
		} else if (lecture) {
			if (gererNiveau.getNiveau().getRockford().isMort())
				System.out.println("Mort de Rockford. Mauvais parcours. Fin du Programme.");
			else
				System.out.println("Fin du temps. Fin Du programme.");
			System.exit(1);
		} else {
			Coeur.running = false;
			lancerNiveau();
		}

	}

	/**
	 * Initialise l'ia suivant la stratégie passée en paramètre (Sauf pour les
	 * ia évolutives) et la fais jouer sur le niveau choisi du fichier BDCFF
	 * choisi.
	 * 
	 * Retourne le score obtenu.
	 * 
	 * @param strategie
	 *            L'ia voulue.
	 * @param cheminFichierBDCFF
	 *            Le chemin du fichier BDCFF contenant le niveau.
	 * @param niveau
	 *            Le niveau voulu.
	 * @return Le score obtenu.
	 */
	public static Score calculerStrategie(String strategie, String cheminFichierBDCFF, int niveau) {
		IA = true;

		ensembleDeNiveau = Loader.charger_ensemble_de_niveaux(cheminFichierBDCFF);
		Partie.niveau = niveau;
		gererNiveau = new GererNiveau(ensembleDeNiveau.getNiveaux().get(niveau - 1).clone());
		if (strategie.equals("-simplet")) {
			ia = new IaRandom();
		} else if (strategie.equals("-directif")) {
			ia = new IaDirective();
		}

		if (ia != null) {
			gererNiveau.tickIa(ia);
		}
		Score score = new Score(gererNiveau.getScore(), gererNiveau.getCompteurTicks(), gererNiveau.getListeDiamants());
		score.setChemin(gererNiveau.getTrajet());
		score.setParcours(score.getChemin().length());
		SousMenu.finIA(score);
		return score;
	
	}

	/**
	 * Initialise l'ia suivant la stratégie passée en paramètre (Sauf pour les
	 * ia non évolutives) et la fais jouer sur le niveau choisi du fichier BDCFF
	 * choisi.
	 * 
	 * Retourne le score obtenu.
	 * 
	 * @param strategie
	 *            L'ia voulue.
	 * @param cheminFichierBDCFF
	 *            Le chemin du fichier BDCFF contenant le niveau.
	 * @param niveau
	 *            Le niveau voulu.
	 * @return Le score obtenu.
	 */
	public static Score calculerStrategieEvolue(String strategie, int nbGenerations, String cheminFichierBDCFF,
			int niveau) {
		IA = true;
		Partie.cheminFichier = cheminFichierBDCFF;
		ensembleDeNiveau = Loader.charger_ensemble_de_niveaux(cheminFichierBDCFF);
		Partie.niveau = niveau;
		gererNiveau = new GererNiveau(ensembleDeNiveau.getNiveaux().get(niveau - 1).clone());

		if (strategie.equals("-evolue")) {
			ia = new IaEvolue(nbGenerations);
		} else if (strategie.equals("-direvol")) {
			ia = new IaDirectiveEvolue(nbGenerations);
		}
		Score score = null;

		if (ia instanceof IaEvolue) {
			score = ((IaEvolue) ia).debut();
		} else if (ia instanceof IaDirectiveEvolue) {
			score = ((IaDirectiveEvolue) ia).debut();
		}

		SousMenu.finIA(score);
		return score;
	}

	/**
	 * Appelée par l'objet GererNiveau afin de finir le niveau actuel, lance le
	 * prochain niveau s'il le faut ou lance les méthoes gérant la fin du
	 * programme sinon.
	 */
	public static void finNiveau() {
		sons.stopAll();
		finiEvolution = true;
		Coeur.running = false;
		if (!lecture && !simulation) {
			enregistrerEssai(gererNiveau.getTrajet());
		}
		if (tousLesNiveaux) {
			SCORES.add(gererNiveau.getScore());
		}
		if (tousLesNiveaux && niveau < ensembleDeNiveau.getNombre_de_niveaux()) {
			if (!Coeur.graphique) {
				ControleurConsole.prochainNiveau(niveau, gererNiveau.getScore());
			}
			niveau++;
			lancerNiveau();
		} else {
			fin();
		}
	}

	/**
	 * Méthode de fin utilisé pour afficher l'écran de fin en mode graphique ou
	 * console.
	 */
	public static void fin() {
		Coeur.running = false;
		if (Coeur.graphique) {
			Coeur.FENETRE.getContentPane().removeAll();
			Coeur.FENETRE.getContentPane().add(new FinPanel());
			Coeur.FENETRE.getContentPane().validate();
			Coeur.FENETRE.repaint();
		} else if (!simulation) {
			if (tousLesNiveaux) {
				GraphiqueConsole.afficherScoreTousLesNiveaux(SCORES);

			} else {
				GraphiqueConsole.afficherScoreUnNiveau(niveau, gererNiveau.getScore());
			}
		}
	}

	/**
	 * Lance un niveau, les paramètres ont été préparés par les méthodes qui
	 * appellent cette méthode.
	 */
	public static void lancerNiveau() {
		if (gererNiveau != null)
			gererNiveau.stop();
		gererNiveau = new GererNiveau(ensembleDeNiveau.getNiveaux().get(niveau - 1).clone());
		Coeur.running = true;
		if (Coeur.graphique) {
			Thread t = new Thread() {
				public void run() {
					while (true) {
						if (Coeur.running) {
							gererNiveau.gererTemps();
							if (gererNiveau.isDemandeReset()) {
								gererNiveau.tick();
							}
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
						}
					}
				}
			};
			t.start();
			Coeur.setTicks((int) (ensembleDeNiveau.getNiveaux().get(niveau - 1).getCaveDelay()
					* Constantes.VITESSE_JEU_TEMPS_REEL));
			preparerFenetre();

		} else {
			Coeur.CONTROLEUR_CONSOLE.run(gererNiveau);
		}

	}

	/**
	 * Méthode tick appelée toutes les X fois par secondes quand le jeu est en
	 * mode temps réel, appèle la bonne méthode tick suivant les booléens de
	 * {@link Partie}.
	 */
	public static void tick() {
		if (!Coeur.graphique) {
			GraphiqueConsole.afficher(gererNiveau.getNiveau());
		}
		if (lecture) {
			if (parcours.length() > 0) {
				gererNiveau.tickLecture(parcours.charAt(0));
				parcours = parcours.substring(1, parcours.length());
			} else {
				finiEvolution = true;
				if (!IA) {
					System.err
							.println("Chemin du fichier terminé or le niveau n'est pas fini, fermeture du programme.");
					System.exit(0);
				}
			}
		} else {
			gererNiveau.tick();
		}

	}

	/**
	 * Prépare la fenêtre à la fin d'un niveau.
	 */
	public static void preparerFenetre() {
		Coeur.FENETRE.getContentPane().removeAll();
		Coeur.FENETRE.getContentPane().setLayout(new BorderLayout());
		Coeur.FENETRE.getContentPane().add(new JeuPanel(), BorderLayout.CENTER);
		Coeur.FENETRE.getContentPane().add(new ScorePanel(), BorderLayout.NORTH);
		Coeur.FENETRE.getContentPane().validate();
	}

	/**
	 * Enregistre le trajet passé en paramètre, le dossier ou on l'enregistre
	 * est choisi e nfonctio ndes différents booleans de {@link Partie}.
	 * 
	 * @param trajet
	 *            Le trajet à enregistrer.
	 * @return Le chemin ou le fichier à été créé.
	 */
	public static String enregistrerEssai(String trajet) {
		String essai = "Trajet : " + trajet + "\nScore : " + gererNiveau.getScore() + "     Diamants : "
				+ gererNiveau.getNbDiamants() + "      Temps : ";
		if (gererNiveau.isTourParTour() || IA) {
			essai += (gererNiveau.getCompteurTicks() + " tours\n");
		} else {
			essai += (((double) gererNiveau.getCompteurTicks()) / ((double) gererNiveau.getNiveau().getCaveDelay()))
					+ " secondes\n";
		}

		String chemin = "";
		if (IA) {
			chemin += "Essais_IA/";
		} else {
			chemin += "Essais_Humain/";
		}
		chemin += dateDebut + "/";

		outils.Ecrivain.ecrire(essai, "Niveau_" + niveau + ".dash", chemin);
		return chemin + "Niveau_" + niveau + ".dash";
	}

	public static String getParcours() {
		return parcours;
	}

}
