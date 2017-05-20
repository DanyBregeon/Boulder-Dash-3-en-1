package entitees.tickables;

import entitees.abstraites.Entitee;
import entitees.abstraites.Tickable;
import main.Constantes;
import main.Partie;

import static entitees.abstraites.Entitee.Entitees.Bombe;

/**
 * Cette classe représente les entitées Bombes.
 *
 * @author celso
 */
public class Bombe extends Tickable {

    /**
     * Représente le nombre de tours de jeu restants avant l'explosion de la
     * bombe.
     */
    private int tempsRestantAvantExplosion = Constantes.BOOM;

    /**
     * Constructeur qui prend les coordonnées.
     *
     * @param x Coordonnée en x.
     * @param y Coordonnée en y.
     */
    protected Bombe(int x, int y) {
        super(x, y);
        enumeration = Bombe;
        setDestructible(false);
        Partie.gererNiveau.ajouterTickable(this);
    }

    @Override
    protected int contactAutreEntitee(Entitee entitee) {
        return 0;
    }

    /**
     * Si le temps restant vaut 0 l'objet explose.
     */
    @Override
    public void tick() {
        if (tempsRestantAvantExplosion == 0 && !isMort()) {
            exploser(false);
        }
        tempsRestantAvantExplosion--;
    }

    /**
     * Si se fait tuer par une explosion (seule façon de tuer cet objet),
     * explose aussi.
     */
    public boolean mourir() {
        if (tempsRestantAvantExplosion > 0) {
            tempsRestantAvantExplosion = -1;
            exploser(false);
        }
        return true;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public int getTempsRestantAvantExplosion() {
        return tempsRestantAvantExplosion;
    }
}
