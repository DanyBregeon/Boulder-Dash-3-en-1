package entitees.tickables;

import entitees.abstraites.Ennemi;

import static entitees.abstraites.Entitee.Entitees.Luciole;

/**
 * Cette classe représente les entitées Lucioles.
 *
 * @author celso
 */
public class Luciole extends Ennemi {

    /**
     * Constructeur qui prend les coordonnées.
     * Ajoute les case de déplacement possibles pour cet objet.
     *
     * @param x Coordonnée en x.
     * @param y Coordonnée en y.
     */
    public Luciole(int x, int y) {
        super(x, y);
        setDestructible(true);
        setDirection('g');
        enumeration = Luciole;
    }

    /**
     * Choisis une direction en fonction des cases autour.
     */
    protected void iASetDirection() {
        if (isFullLibre()) {
            setDirection('g');
        } else if (isGaucheLibre()) {
            tournerAGauche();
        } else if (isToutDroitLibre()) {

        } else if (isDroiteLibre()) {
            tournerADroite();
        } else if (isDerriereLibre()) {
            tournerADroite();
        }
    }
}
