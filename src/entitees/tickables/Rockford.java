package entitees.tickables;

import entitees.abstraites.Entitee;
import entitees.abstraites.Tickable;
import main.Constantes;
import main.Partie;

import static entitees.abstraites.Entitee.Entitees.*;
import static entitees.abstraites.Entitee.Entitees.Diamant;
import static entitees.abstraites.Entitee.Entitees.Explosion;
import static entitees.abstraites.Entitee.Entitees.Libellule;
import static entitees.abstraites.Entitee.Entitees.Luciole;
import static entitees.abstraites.Entitee.Entitees.Pierre;
import static entitees.abstraites.Entitee.Entitees.Rockford;

/**
 * Cette classe représente les entitées Rockfords.
 *
 * @author celso
 */
public class Rockford extends Tickable {

    /**
     * Représente la dernière direction (parmis gauche ou droite) que Rockford a
     * pris. Utile pour gérer le sprite de Rockford.
     */
    private char ancienneDirection = 'd';

    /**
     * Représente le nombre de nombes dont Rockford dispose.
     */
    private int nombreDeBombe = Constantes.NOMBRE_DE_BOMBES;

    /**
     * Est vrai si l'utilisateur a appuyé sur le bouton poru poser une bombe,
     * quand rockford changera de case redeviendra faux.
     */
    private boolean bombeAPoser = false;

    /**
     * Constructeur qui prend les coordonnées.
     * Ajoute les case de déplacement possibles pour cet objet.
     *
     * @param x Coordonnée en x.
     * @param y Coordonnée en y.
     */
    public Rockford(int x, int y) {
        super(x, y);
        setDestructible(true);
        setDirection(' ');
        getDeplacementsPossibles().add(Poussiere);
        getDeplacementsPossibles().add(Diamant);
        getDeplacementsPossibles().add(Pierre);
        getDeplacementsPossibles().add(Sortie);
        getDeplacementsPossibles().add(Amibe);
        getDeplacementsPossibles().add(Libellule);
        getDeplacementsPossibles().add(Luciole);
        getDeplacementsPossibles().add(Explosion);
        enumeration = Rockford;
    }

    /**
     * Ramasse le diamant passé en paramètre, améliore le score de
     * {@link Partie#gererNiveau}.
     *
     * @param d Le diamant ramassé.
     */
    public void ramasserDiamant(Diamant d) {
        if (Partie.gererNiveau.getNbDiamants() >= Partie.gererNiveau.getNiveau().getDiamonds_required()) {
            Partie.gererNiveau
              .setScore(Partie.gererNiveau.getScore() + Partie.gererNiveau.getNiveau().getDiamond_value_bonus());
        } else {
            Partie.gererNiveau
              .setScore(Partie.gererNiveau.getScore() + Partie.gererNiveau.getNiveau().getDiamond_value());
        }
        Partie.gererNiveau.incrementerNbDiamants(d);
    }

    @Override
    public int contactAutreEntitee(Entitee entitee) {
        if (entitee.is(Pierre)) {
            if (entitee.getX() - getX() > 0
                && Partie.gererNiveau.getNiveau().getMap()[entitee.getX() + 1][entitee.getY()].is(Vide)) {
                ((entitees.tickables.Pierre) entitee).setDirection('d');
                ((entitees.tickables.Pierre) entitee).seDeplacer();
            } else if (entitee.getX() - getX() < 0
                       && Partie.gererNiveau.getNiveau().getMap()[entitee.getX() - 1][entitee.getY()].is(Vide)) {
                ((entitees.tickables.Pierre) entitee).setDirection('g');
                ((entitees.tickables.Pierre) entitee).seDeplacer();
            }
            return 0;
        } else if (entitee.is(Diamant)) {
            entitee.mourir();
            ramasserDiamant((Diamant) entitee);
            return 1;
        } else if (entitee.is(Sortie)) {
            if (Partie.gererNiveau.getNbDiamants() >= Partie.gererNiveau.getNiveau().getDiamonds_required()) {
                Partie.gererNiveau.setFiniSuccess(true);
                Partie.gererNiveau.setDemandeFin(true);
                return 1;
            }
            return 0;
        } else if (entitee.is(Amibe)) {
            mourir();
            return -1;
        } else if (entitee.is(Explosion)) {
            mourir();
            return -1;
        } else if (entitee.is(Libellule)) {
            mourir();
            return -1;
        } else if (entitee.is(Luciole)) {
            mourir();
            return -1;
        }
        return 1;
    }

    @Override
    public void tick() {
        setDirection(Partie.gererNiveau.getToucheClavier());
        checkCamouflage();
        if (enumeration == Rockford) {
            if (getDirection() == 'B') {
                poserBombe();
            } else if (deplacement()) {
                checkBombe();
            }
        }
    }

    /**
     * Regarde si le joueur a appuyé sur la touche pour poser une bombe, si oui
     * pose une bombe à l'ancienne case de rockford.
     */
    private void checkBombe() {
        if (bombeAPoser == true && !Partie.gererNiveau.isTourParTour()) {
            int x = 0, y = 0;
            if (getDirection() == 'h') {
                y = -1;
            } else if (getDirection() == 'd') {
                x = 1;
            } else if (getDirection() == 'g') {
                x = -1;
            } else if (getDirection() == 'b') {
                y = 1;
            }
            Partie.gererNiveau.getNiveau().getMap()[getX() - x][getY() - y] = new Bombe(getX() - x, getY() - y);
            bombeAPoser = false;
            nombreDeBombe--;
        }
    }

    protected void gererChute() {
        if (chute && placeLibre(getX(), getY() + 1)) {
            setDirection('b');
            seDeplacer();
        } else if (Partie.gererNiveau.getNiveau().testEntitee(getX(), getY() + 1, Vide)) {
            chute = true;
            gererChute();
        } else {
            if (!placeLibre(getX(), getY() + 1)) {
                chute = false;
            }
            glisser();
        }

    }

    /**
     * Différent car il faut jouer un son.
     *
     * @return Vrai si un déplacement a eu lieu, faux sinon.
     */
    private boolean deplacement() {
        if (getDirection() != ' ') {
            sons.jouerSon1("walk_earth.wav", 1);
            if (seDeplacer()) { return true; }
        }
        return false;
    }

    /**
     * Différent car il faut jouer un son.
     */
    public boolean mourir() {
        super.mourir();
        sons.jouerSon3("mortRockford.wav", 1);
        Partie.gererNiveau.setDemandeReset(true);
        return true;
    }

    /**
     * Camoufle ou décamoufle Rockford si le joueur appuye sur la touche
     * approprié.
     */
    public void checkCamouflage() {
        if (Partie.gererNiveau.getToucheClavier() == 'p' && enumeration == Rockford) {
            seCamoufler();
        } else if (enumeration == Pierre && Partie.gererNiveau.getToucheClavier() != 'p') {
            seDecamoufler();
        }
    }

    /*
     * Camoufle Rockford.
     *
     * Change l'énumération de Rockford afin de changer son comportement envers
     * les autres entitées.
     *
     * Change aussi les déplacements possibles de rockford, (ne sert à rien dans
     * cette version, mais utile si on veut donner de la gravité à
     * RockfordPierre.
     */
    private void seCamoufler() {
        enumeration = Pierre;
        getDeplacementsPossibles().remove(Poussiere);
        getDeplacementsPossibles().remove(Diamant);
        getDeplacementsPossibles().remove(Sortie);
        getDeplacementsPossibles().add(Luciole);
        getDeplacementsPossibles().add(Libellule);
        getDeplacementsPossibles().add(Amibe);
    }

    /*
     * Déamoufle Rockford.
     *
     * Change l'énumération de Rockford afin de changer son comportement envers
     * les autres entitées.
     *
     * Change aussi les déplacements possibles de rockford, (ne sert à rien dans
     * cette version, mais utile si on veut donner de la gravité à
     * RockfordPierre.
     */
    private void seDecamoufler() {
        enumeration = Rockford;
        getDeplacementsPossibles().add(Poussiere);
        getDeplacementsPossibles().add(Diamant);
        getDeplacementsPossibles().add(Sortie);
        getDeplacementsPossibles().remove(Luciole);
        getDeplacementsPossibles().remove(Libellule);
        getDeplacementsPossibles().remove(Amibe);
    }

    /**
     * Controle le posage de bombe.
     * Si le nombre de bombes disponibles est ok, change le booleen aproprié.
     */
    public void poserBombe() {
        if (nombreDeBombe > 0) {
            bombeAPoser = true;
        }
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public boolean camouflageActif() {
        return getEnumeration() == Entitee.Entitees.Pierre;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public char getAncienneDirection() {
        return ancienneDirection;
    }

    /**
     * Un setter.
     *
     * @param ancienneDirection L'objet en question.
     */
    public void setAncienneDirection(char ancienneDirection) {
        this.ancienneDirection = ancienneDirection;
    }

}
