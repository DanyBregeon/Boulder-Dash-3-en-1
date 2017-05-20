package entitees.fixes;

import entitees.abstraites.Entitee;
import main.Partie;

import static entitees.abstraites.Entitee.Entitees.*;

/**
 * Cette classe représente les entitées Sortie.
 * 
 * @author celso
 */
public class Sortie extends Entitee {

	/**
	 * Constructeur qui prend les coordonnées.
	 * 
	 * @param x
	 *            Coordonnée en x.
	 * @param y
	 *            Coordonnée en y.
	 */
	public Sortie(int x, int y) {
		super(x, y);
		enumeration = Sortie;
	}

	/**
	 * Retourne vrai si le nombre de diamant attrapé par le joueur est supérieur
	 * ou égal au nombre de diamant requis dans ce niveau.
	 * 
	 * @return Retourne vrai si c'est le cas, faux sinon.
	 */
	public boolean isOuvert() {
		return Partie.gererNiveau.getNbDiamants() >= Partie.gererNiveau.getNiveau().getDiamonds_required();
	}
}
