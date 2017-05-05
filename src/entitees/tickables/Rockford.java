package entitees.tickables;

import entitees.abstraites.Entitee;
import entitees.abstraites.Tickable;
import main.Coeur;
import main.Partie;

import static entitees.abstraites.Entitee.Entitees.*;

public class Rockford extends Tickable {

    private char ancienneDirection = 'd';

    public Rockford(int x, int y) {
        super(x, y);
        setDestructible(true);
        setDirection(' ');
        getDeplacementsPossibles().add(Poussiere);
        getDeplacementsPossibles().add(Diamant);
        getDeplacementsPossibles().add(Pierre);
        getDeplacementsPossibles().add(Sortie);
        getDeplacementsPossibles().add(Amibe);
        getDeplacementsPossibles().add(Explosion);
        enumeration = Rockford;
    }

    public void ramasserDiamant() {
        if (Partie.gererNiveau.getNbDiamants() >= Partie.gererNiveau.getNiveau().getDiamonds_required()) {
            Partie.gererNiveau
                    .setScore(Partie.gererNiveau.getScore() + Partie.gererNiveau.getNiveau().getDiamond_value_bonus());
        } else {
            Partie.gererNiveau
                    .setScore(Partie.gererNiveau.getScore() + Partie.gererNiveau.getNiveau().getDiamond_value());
        }
        Partie.gererNiveau.incrementerNbDiamants();
    }

    @Override
    protected int contactAutreEntitee(Entitee entitee) {
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
            ramasserDiamant();
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

        checkCamouflage();
        if (enumeration == Rockford)
            deplacement();
        else if (enumeration == Pierre) {
            gererChute();
        }
        orienterDirectionPourSauvegarde();
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

    private void deplacement() {
        setDirection(Partie.gererNiveau.getToucheClavier());
        if (getDirection() != ' ') {
            seDeplacer();
        }
    }

    public boolean mourir() {
        super.mourir();
        Partie.gererNiveau.setDemandeReset(true);
        return true;
    }

    public void checkCamouflage() {
        //si on est en mode lecteur
        if (Partie.lecture) {
            if (Partie.gererNiveau.getToucheClavier() == 'p' && enumeration == Rockford) {
                seCamoufler();
            } else if (enumeration == Pierre && Partie.gererNiveau.getToucheClavier() != 'p') {
                seDecamoufler();
            }
        } else {
            if (Coeur.CONTROLEUR.isPierre() && enumeration == Rockford) {
                seCamoufler();
            } else if (enumeration == Pierre && !Coeur.CONTROLEUR.isPierre()) {
                seDecamoufler();
            }
        }
    }

    private void seCamoufler() {
        enumeration = Pierre;
        getDeplacementsPossibles().remove(Poussiere);
        getDeplacementsPossibles().remove(Diamant);
        getDeplacementsPossibles().remove(Sortie);
        getDeplacementsPossibles().add(Luciole);
        getDeplacementsPossibles().add(Libellule);
        getDeplacementsPossibles().add(Amibe);
    }

    private void seDecamoufler() {
        enumeration = Rockford;
        getDeplacementsPossibles().add(Poussiere);
        getDeplacementsPossibles().add(Diamant);
        getDeplacementsPossibles().add(Sortie);
        getDeplacementsPossibles().remove(Luciole);
        getDeplacementsPossibles().remove(Libellule);
        getDeplacementsPossibles().remove(Amibe);
    }

    public void orienterDirectionPourSauvegarde() {
        if (enumeration == Pierre) {
            setDirection('p');
        }
    }

    public char getAncienneDirection() {
        return ancienneDirection;
    }

    public void setAncienneDirection(char ancienneDirection) {
        this.ancienneDirection = ancienneDirection;
    }

}
