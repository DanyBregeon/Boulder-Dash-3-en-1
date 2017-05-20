package ia;

import static main.Constantes.NOMBRE_DE_TRY_GENERATION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entitees.abstraites.Entitee;
import main.Constantes;
import main.GererNiveau;
import main.Partie;
import outils.Score;

/**
 * Classe représentant l'ia évoluée directive..
 * 
 * @author celso
 *
 */
public class IaDirectiveEvolue extends Ia {

	/**
	 * Une liste de score représentant les scores obenus durant cette
	 * génération.
	 */
	private List<Score> liste = new ArrayList<>();
	/**
	 * Le score actuel.
	 */
	private Score scoreActuel;
	/**
	 * Le nombre de génération à effectuer.
	 */
	private int nbGenerations;
	/**
	 * La génération nactuelle.
	 */
	private int generationActuelle = 1;
	/**
	 * Le nomrbe d'essais par génération.
	 */
	private int trysDeGeneration = NOMBRE_DE_TRY_GENERATION;
	/**
	 * Une liste de score représentant les scores obenus durant la dernière
	 * génération.
	 */
	private List<Score> liste2 = new ArrayList<>();
	/**
	 * La taille maximale du chemin que peut prendre Rockford.
	 */
	private double tailleCheminMaximale;

	/**
	 * Le nombre de diamants à récupérer.
	 */
	private int nbObjectifs;

	/**
	 * Constructeur IaDirectiveEvolue.
	 * 
	 * Le initialise les attributs et effectue la première génération (full
	 * random).
	 * 
	 * @param nbGenerations
	 *            Le nombre de générations à effectuer.
	 */
	public IaDirectiveEvolue(int nbGenerations) {
		this.nbGenerations = nbGenerations;
		String chemin;
		for (int i = 0; i < NOMBRE_DE_TRY_GENERATION; i++) {
			Partie.gererNiveau = new GererNiveau(Partie.ensembleDeNiveau.getNiveaux().get(Partie.niveau - 1).clone());
			Partie.finiEvolution = false;
			chemin = "";
			GererNiveau g = Partie.gererNiveau;
			IaDirective ia = new IaDirective();
			for (int j = 0; j < (Partie.gererNiveau.getNiveau().getCaveDelay()
					* Partie.gererNiveau.getNiveau().getCave_time() * Constantes.VITESSE_JEU_TEMPS_REEL); j++) {
				if (g.isDemandeReset() || g.isDemandeFin()) {
					while (g.getTrajet().length() + chemin.length() <= (Partie.gererNiveau.getNiveau().getCaveDelay()
							* Partie.gererNiveau.getNiveau().getCave_time() * Constantes.VITESSE_JEU_TEMPS_REEL)) {
						chemin += Ia.directionRandom();
					}
				} else {
					g.tickIaDirevol(ia);
				}
			}

			chemin = g.getTrajet() + chemin;
			Score s;
			s = Partie.jouerFichierScore(Partie.cheminFichier, Partie.niveau, chemin);
			liste.add(s);
		}

		Collections.sort(liste);

	}

	@Override
	public char tick(Entitee[][] map) {
		return 'w';
	}

	@Override
	public void initialiserTry() {
		if (trysDeGeneration > 0) {
			trysDeGeneration--;
		} else {
			trysDeGeneration = NOMBRE_DE_TRY_GENERATION;
			if (generationActuelle > nbGenerations) {
				Partie.gererNiveau.setTrajet(liste.get(0).getChemin());
				Partie.gererNiveau.setScore(liste.get(0).getScore());
				Partie.finNiveau();
			}
			generationActuelle++;
		}
	}

	/**
	 * Appelée après la première génération, effectue le nombre de générations
	 * nécessaires et renvoie le meilleur score obtenu.
	 * 
	 * @return Le meilleur score obtenu.
	 */
	public Score debut() {
		Score aReturn;
		while (!critereArret()) {
			System.out.println("Génération " + generationActuelle + "/" + nbGenerations);
			generationActuelle++;
			for (int i = 0; i < (Constantes.NOMBRE_DE_TRY_GENERATION * Constantes.POURCENTAGE_DE_SURVIVANTS)
					/ 100; i++) {
				Score s = new Score(liste.get(i).getScore(), liste.get(i).getParcours(),
						liste.get(i).getListeDiamants());
				s.setChemin(liste.get(i).getChemin());
				liste2.add(s);
			}
			for (int i = 0; i < (Constantes.NOMBRE_DE_TRY_GENERATION * Constantes.POURCENTAGE_DE_ALEATOIRE)
					/ 100; i++) {
				Partie.finiEvolution = false;
				String chemin = "";
				for (int j = 0; j < (tailleCheminMaximale); j++) {
					chemin += Ia.directionRandom();
				}
				Score s;
				s = Partie.jouerFichierScore(Partie.cheminFichier, Partie.niveau, chemin);
				liste2.add(s);
			}
			for (int i = 0; i < (Constantes.NOMBRE_DE_TRY_GENERATION
					- ((Constantes.NOMBRE_DE_TRY_GENERATION * Constantes.POURCENTAGE_DE_SURVIVANTS) / 100)
					- ((Constantes.NOMBRE_DE_TRY_GENERATION * Constantes.POURCENTAGE_DE_ALEATOIRE) / 100)); i++) {

				int rng = (int) (Math.random()
						* (Constantes.POURCENTAGE_DES_SELECTIONNES * Constantes.NOMBRE_DE_TRY_GENERATION) / 100.0);
				Score s = liste.get(rng);

				Partie.gererNiveau = new GererNiveau(
						Partie.ensembleDeNiveau.getNiveaux().get(Partie.niveau - 1).clone());
				int tailleMutation = 2;
				String chemin = "";
				GererNiveau g = Partie.gererNiveau;
				if (g.isDemandeFin()) {
					tailleMutation = 0;
				}
				IaDirective ia = new IaDirective();
				for (int j = 0; j < s.getParcours() - tailleMutation; j++) {
					g.tickIaController(ia, s.getChemin().charAt(j));
					// System.out.println(g.getNiveau().getRockford().getX() + "
					// ; " + g.getNiveau().getRockford().getY());
				}
				// System.out.println(" ");
				for (int j = s.getParcours() - tailleMutation; j < s.getParcours(); j++) {
					char c = Ia.directionRandom();
					g.tickIaController(ia, c);
					// System.out.print(c);
				}
				// System.out.println(finChemin);
				for (int j = s.getParcours(); j < s.getChemin().length(); j++) {
					// g = Partie.gererNiveau;
					if (g.isDemandeReset() || g.isDemandeFin()) {
						while (g.getTrajet().length()
								+ chemin.length() <= (Partie.gererNiveau.getNiveau().getCaveDelay()
										* Partie.gererNiveau.getNiveau().getCave_time()
										* Constantes.VITESSE_JEU_TEMPS_REEL)) {
							chemin += Ia.directionRandom();
						}
					} else {
						g.tickIaDirevol(ia);
					}
				}
				chemin = g.getTrajet() + chemin;
				s.setChemin(chemin);
				// s.setChemin(s.getChemin().substring(0, s.getParcours() -
				// tailleMutation) + finChemin);
				s = Partie.jouerFichierScore(Partie.cheminFichier, Partie.niveau, chemin);
				liste2.add(s);
			}
			liste.clear();
			for (int i = 0; i < liste2.size(); i++) {
				liste.add(liste2.get(i));
			}
			liste2.clear();
			Collections.sort(liste);

		}

		aReturn = liste.get(0);
		return aReturn;
	}

	/**
	 * Renvoit vrai quand le nombre de générations dépasses le nombre de
	 * générations voulues.
	 * 
	 * Sert à indiquer à l'IA quand s'arréter.
	 * 
	 * @return Retourne vrai si 'il doit s'arrêter.
	 */
	public boolean critereArret() {
		if (generationActuelle > nbGenerations) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Crée un objet Score avec les valeurs actuelles que l'ia a, et l'ajotue
	 * dans la liste des scores.
	 */
	public void ajouterScore() {
		scoreActuel = new Score(Partie.gererNiveau.getScore(), Partie.gererNiveau.getTrajet().length(),
				Partie.gererNiveau.getListeDiamants());
		scoreActuel.setChemin(Partie.parcours);
		liste.add(scoreActuel);
		Collections.sort(liste);
	}

	/**
	 * Un getter.
	 * 
	 * @return l'objet en question.
	 */
	public int getNbGenerations() {
		return nbGenerations;
	}

	/**
	 * Un getter.
	 * 
	 * @return l'objet en question.
	 */
	public int getGenerationActuelle() {
		return generationActuelle;
	}

	/**
	 * Un getter.
	 * 
	 * @return l'objet en question.
	 */
	public int getNbObjectifs() {
		return nbObjectifs;
	}

}