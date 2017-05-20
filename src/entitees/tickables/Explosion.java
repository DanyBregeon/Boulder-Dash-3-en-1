package entitees.tickables;

import entitees.abstraites.Entitee;
import entitees.abstraites.Tickable;

import static entitees.abstraites.Entitee.Entitees.Explosion;

/**
 * Cette classe représente les entitées Explosions.
 * C'est à dire le feu qui reste quelques ticks après une explosion.
 *
 * @author celso
 */
public class Explosion extends Tickable {

    /**
     * Représente le nombre de tous de boucle durant lesquels l'explosion
     * existe.
     */
    private int ticksAvantFinExplosion = 3;

    /**
     * Constructeur qui prend les coordonnées.
     *
     * @param x Coordonnée en x.
     * @param y Coordonnée en y.
     */
    public Explosion(int x, int y) {
        super(x, y);
        setDestructible(true);
        enumeration = Explosion;
    }

    @Override
    protected int contactAutreEntitee(Entitee entitee) {
        return 0;
    }

    /**
     * Décremente le nombre de tours de boucle restants, quand ce nombre vaut 0,
     * l'objet meurt.
     */
    @Override
    public void tick() {
        if (ticksAvantFinExplosion > 0) { ticksAvantFinExplosion--; } else {
            mourir();
        }
    }
}
