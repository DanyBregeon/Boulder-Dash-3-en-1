package entitees.fixes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import entitees.abstraites.Entitee;
import entitees.tickables.Diamant;
import entitees.tickables.Pierre;
import main.Partie;

import static entitees.abstraites.Entitee.Entitees.Amibe;

/**
 * Cette classe représente les entitées Amibes.
 *
 * @author celso
 */
public class Amibe extends Entitee {

    /**
     * Constructeur qui prend les coordonnées.
     *
     * @param x Coordonnée en x.
     * @param y Coordonnée en y.
     */
    public Amibe(int x, int y) {
        super(x, y);
        setDestructible(true);
        enumeration = Amibe;
    }

    /**
     * Méthode qui fait se propager les amibes.
     * Un appel = Une propagation.
     * Fais une liste de toutes les cases ou une amibe peut apparaitre.
     * Puis en choisis une aleatoirement.
     * Appelle {@link Amibe#checkDetruireAmibes()} à la fin.
     *
     * @return Vrai si la propagation a été réussie, faux sinon.
     */
    public boolean sePropager() {
        List<Point> points = new ArrayList<Point>();
        if (placeLibre(getX() + 1, getY())) {
            points.add(new Point(getX() + 1, getY()));
        }
        if (placeLibre(getX() - 1, getY())) {
            points.add(new Point(getX() - 1, getY()));
        }
        if (placeLibre(getX(), getY() - 1)) {
            points.add(new Point(getX(), getY() - 1));
        }
        if (placeLibre(getX(), getY() + 1)) {
            points.add(new Point(getX(), getY() + 1));
        }
        if (!points.isEmpty()) {
            int rng = (int) (Math.random() * points.size());
            Partie.gererNiveau.getNiveau().getMap()[points.get(rng).x][points.get(rng).y] = new Amibe(points.get(rng).x,
                                                                                                      points.get(
                                                                                                        rng).y);
            Partie.gererNiveau.ajouterAmibe(
              ((Amibe) Partie.gererNiveau.getNiveau().getMap()[points.get(rng).x][points.get(rng).y]));
            checkDetruireAmibes();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Regarde si il y a plus de 200 amibes dans la map, si oui les tramsforme
     * en pierre.
     */
    private void checkDetruireAmibes() {
        if (Partie.gererNiveau.getListeAmibes().size() >= 200) {
            for (Amibe amibe : Partie.gererNiveau.getListeAmibes()) {
                Partie.gererNiveau.getNiveau().getMap()[amibe.getX()][amibe.getY()] = new Pierre(amibe.getX(),
                                                                                                 amibe.getY());
            }
            for (Amibe amibe : Partie.gererNiveau.getListeAmibesAjout()) {
                Partie.gererNiveau.getNiveau().getMap()[amibe.getX()][amibe.getY()] = new Pierre(amibe.getX(),
                                                                                                 amibe.getY());
            }
            Partie.gererNiveau.getListeAmibes().clear();
            Partie.gererNiveau.getListeAmibesAjout().clear();
        }
    }

    /**
     * Transforme toutes les amibes en diamant.
     */
    public void transformerTousLesAmibesEnDiamant() {
        for (Amibe amibe : Partie.gererNiveau.getListeAmibes()) {
            Partie.gererNiveau.getNiveau().getMap()[amibe.getX()][amibe.getY()] = new Diamant(amibe.getX(),
                                                                                              amibe.getY());
            Partie.gererNiveau
              .ajouterTickable((Diamant) Partie.gererNiveau.getNiveau().getMap()[amibe.getX()][amibe.getY()]);
        }
        Partie.gererNiveau.getListeAmibes().clear();
        Partie.gererNiveau.getListeAmibesAjout().clear();
    }

    public boolean mourir() {
        Partie.gererNiveau.getListeAmibes().remove(this);
        return super.mourir();
    }

    private boolean placeLibre(int x, int y) {
        if (Partie.gererNiveau.getNiveau().getMap()[x][y].getClass().equals(Vide.class)
            || Partie.gererNiveau.getNiveau().getMap()[x][y].getClass().equals(Poussiere.class)) {
            return true;
        }
        return false;
    }
}
