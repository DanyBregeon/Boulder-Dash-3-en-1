package entitees.fixes;

import entitees.abstraites.Entitee;

import static entitees.abstraites.Entitee.Entitees.MurEnTitane;

/**
 * Cette classe représente les entitées Murs En Titane.
 *
 * @author celso
 */
public class MurEnTitane extends Entitee {

    /**
     * Constructeur qui prend les coordonnées.
     *
     * @param x Coordonnée en x.
     * @param y Coordonnée en y.
     */
    public MurEnTitane(int x, int y) {
        super(x, y);
        enumeration = MurEnTitane;
    }
}
