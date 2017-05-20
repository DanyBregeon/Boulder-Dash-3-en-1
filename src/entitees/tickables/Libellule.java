package entitees.tickables;

import entitees.abstraites.Ennemi;

import static entitees.abstraites.Entitee.Entitees.Libellule;

/**
 * Cette classe représente les entitées Libellules.
 *
 * @author celso
 */
public class Libellule extends Ennemi {

    /**
     * Constructeur qui prend les coordonnées.
     * Ajoute les case de déplacement possibles pour cet objet.
     *
     * @param x Coordonnée en x.
     * @param y Coordonnée en y.
     */
    public Libellule(int x, int y) {
        super(x, y);
        setDestructible(true);
        setDirection('d');
        enumeration = Libellule;
    }

    /**
     * Choisis une direction en fonction des cases autour.
     */
    protected void iASetDirection() {
        if (isFullLibre()) {
            setDirection('d');
        } else if (isDroiteLibre()) {
            tournerADroite();
        } else if (isToutDroitLibre()) {
            // rien
        } else if (isGaucheLibre()) {
            tournerAGauche();
        } else if (isDerriereLibre()) {
            tournerAGauche();
        }
    }

}
