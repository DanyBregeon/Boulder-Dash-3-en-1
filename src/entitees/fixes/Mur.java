package entitees.fixes;

import entitees.abstraites.Entitee;
import static entitees.abstraites.Entitee.Entitees.*;

/**
 * Cette classe représente les entitées Murs.
 * 
 * @author celso
 */
public class Mur extends Entitee {

	/**
	 * Constructeur qui prend les coordonnées.
	 * 
	 * @param x
	 *            Coordonnée en x.
	 * @param y
	 *            Coordonnée en y.
	 */
	public Mur(int x, int y) {
		super(x, y);
		setDestructible(true);
		enumeration = Mur;
	}
}