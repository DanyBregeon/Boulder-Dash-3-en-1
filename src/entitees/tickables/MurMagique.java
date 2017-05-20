package entitees.tickables;

import entitees.abstraites.Entitee;
import entitees.abstraites.Tickable;
import entitees.fixes.Mur;
import main.Partie;

import static entitees.abstraites.Entitee.Entitees.Diamant;
import static entitees.abstraites.Entitee.Entitees.MurMagique;
import static entitees.abstraites.Entitee.Entitees.Vide;

/**
 * Cette classe représente les entitées Murs Magiques.
 *
 * @author celso
 */
public class MurMagique extends Tickable {

    /**
     * Représente le nombre d'utilisations possibles de ce mur magique.
     */
    private int magicWallTime;

    /**
     * Constructeur qui prend les coordonnées.
     *
     * @param x Coordonnée en x.
     * @param y Coordonnée en y.
     * @param magicWallTime Nombre d'utilisations possibles de ce mur magique.
     */
    public MurMagique(int x, int y, int magicWallTime) {
        super(x, y);
        this.magicWallTime = magicWallTime;
        setDestructible(true);
        enumeration = MurMagique;
    }

    /**
     * Décrémente le nombre d'utilisations possibles du mur magique.
     * Transforme le mur magique en mur normal si la nombre d'utilisation est
     * égal à 0.
     */
    public void decrementerMagicWallTime() {
        magicWallTime--;
        if (magicWallTime <= 0) {
            mourir();
            Partie.gererNiveau.getNiveau().getMap()[getX()][getY()] = new Mur(getX(), getY());
        }
    }

    /**
     * Appelé à chaque tick.
     * Regarde si il y a du vide en dessous de l'objet et s'il y a un
     * diamant/pierre a dessus.
     * Si tel est le cas le détruit est crée l'entité inverse en dessous,
     * ensuite appelle {@link MurMagique#decrementerMagicWallTime()}..
     *
     * @return Vrai si une transformation a eu lieu, faux sinon.
     */
    public boolean traverser() {
        if (Partie.gererNiveau.getNiveau().getMap()[getX()][getY() + 1].is(Vide)) {
            if (Partie.gererNiveau.getNiveau().getMap()[getX()][getY() - 1].is(Diamant)) {
                Partie.gererNiveau.getNiveau().getMap()[getX()][getY() - 1].mourir();
                Partie.gererNiveau.getNiveau().getMap()[getX()][getY() + 1] = new Pierre(getX(), getY() + 1);
                Partie.gererNiveau
                  .ajouterTickable((Tickable) Partie.gererNiveau.getNiveau().getMap()[getX()][getY() + 1]);
                decrementerMagicWallTime();
                return true;
            } else if (Partie.gererNiveau.getNiveau().getMap()[getX()][getY() - 1] instanceof Pierre) {
                Partie.gererNiveau.getNiveau().getMap()[getX()][getY() - 1].mourir();
                Partie.gererNiveau.getNiveau().getMap()[getX()][getY() + 1] = new Diamant(getX(), getY() + 1);
                Partie.gererNiveau
                  .ajouterTickable((Tickable) Partie.gererNiveau.getNiveau().getMap()[getX()][getY() + 1]);
                ((Tickable) Partie.gererNiveau.getNiveau().getMap()[getX()][getY() + 1]).setChute(true);
                decrementerMagicWallTime();
                return true;
            }
        }
        return false;

    }

    @Override
    protected int contactAutreEntitee(Entitee entitee) {
        return 0;
    }

    @Override
    public void tick() {
        traverser();
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public int getMagicWallTime() {
        return magicWallTime;
    }

}
