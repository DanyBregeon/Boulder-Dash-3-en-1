package entitees.tickables;

import java.util.ArrayList;
import java.util.List;

import entitees.abstraites.Entitee;
import entitees.abstraites.Tickable;
import main.Partie;

import static entitees.abstraites.Entitee.Entitees.Amibe;
import static entitees.abstraites.Entitee.Entitees.Explosion;
import static entitees.abstraites.Entitee.Entitees.Libellule;
import static entitees.abstraites.Entitee.Entitees.Luciole;
import static entitees.abstraites.Entitee.Entitees.MurMagique;
import static entitees.abstraites.Entitee.Entitees.Pierre;
import static entitees.abstraites.Entitee.Entitees.Rockford;
import static entitees.abstraites.Entitee.Entitees.Vide;

/**
 * Cette classe représente les entitées Pierres.
 *
 * @author celso
 */
public class Pierre extends Tickable {

    /**
     * Les éléments de cette liste représentent sur quels types d'élément notre
     * objet peut tomber.
     */
    private List<Entitees> deplacementsPossiblesChute = new ArrayList<Entitees>();

    /**
     * Constructeur qui prend les coordonnées.
     * Ajoute les case de déplacement possibles pour cet objet.
     *
     * @param x Coordonnée en x.
     * @param y Coordonnée en y.
     */
    public Pierre(int x, int y) {
        super(x, y);
        setDestructible(true);
        getDeplacementsPossibles().add(Rockford);
        getDeplacementsPossibles().add(Luciole);
        getDeplacementsPossibles().add(Libellule);
        getDeplacementsPossibles().add(Amibe);
        getDeplacementsPossibles().add(MurMagique);
        getDeplacementsPossibles().add(Explosion);
        deplacementsPossiblesChute.add(Vide);
        enumeration = Pierre;
    }

    public void tick() {
        if (!bloque || chute) {
            gererChute();
        } else {
            glisser();
        }
        testBloquer();
    }

    @Override
    protected int contactAutreEntitee(Entitee entitee) {

        setDirection('b');
        if (entitee.is(Rockford)) {
            exploser(false);
            return 0;
        } else if (entitee.is(MurMagique)) {
            return 0;
        } else if (entitee.is(Libellule)) {
            exploser(true);
            return 0;
        } else if (entitee.is(Luciole)) {
            exploser(false);
            return 0;
        } else if (entitee.is(Amibe)) {
            return 0;
        } else if (entitee.is(Explosion)) {
            exploser(false);
            return 0;
        }
        return 1;

    }

    /**
     * Explosion différente car ici il faut jouer un son.
     */
    protected void exploser(boolean popDiamants) {
        sons.jouerSon1("explosion.wav", 1);
        for (int i = -1; i < 2; i++) {
            for (int j = 0; j <= 2; j++) {
                explosion(i, j, popDiamants);
            }
        }
    }

    /**
     * Change le booleen bloque si il y a de la place en dessous.
     */
    protected void testBloquer() {
        bloque = !(placeLibreChute(getX(), getY() + 1));
    }

    /**
     * Regarde si il y a de la place pour tomber à la case de coordonnées
     * entrées en paramètre.
     *
     * @param x Coordonnée en x.
     * @param y Coordonnée en y.
     *
     * @return Vrai si c'est le cas, faux sinon.
     */
    protected boolean placeLibreChute(int x, int y) {
        for (Entitees e : deplacementsPossiblesChute) {
            if (Partie.gererNiveau.getNiveau().getMap()[x][y].getEnumeration().equals(e)) {
                return true;
            }
        }
        return false;
    }
}
