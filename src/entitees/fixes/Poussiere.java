package entitees.fixes;

import entitees.abstraites.Entitee;
import static entitees.abstraites.Entitee.Entitees.*;

/**
 * Cette classe représente les entitées Poussière.
 * 
 * @author celso
 */
public class Poussiere extends Entitee {

	/**
	 * Constructeur qui prend les coordonnées.
	 * 
	 * @param x
	 *            Coordonnée en x.
	 * @param y
	 *            Coordonnée en y.
	 */
	public Poussiere(int x, int y) {
		super(x, y);
		setDestructible(true);
		enumeration = Poussiere;
	}
}