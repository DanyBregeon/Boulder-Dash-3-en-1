package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entitees.abstraites.Tickable;
import entitees.fixes.Amibe;
import entitees.tickables.Diamant;
import ia.Ia;
import ia.IaEvolue;
import loader.Niveau;
import outils.Paire;
import outils.Score;

/**
 * La classe GererNiveau dispose des informations utiles d'une partie (un essai
 * à un niveau).
 *
 * @author celso
 */
public class GererNiveau {

    /**
     * Compteur de reset, inutile dans cette version.
     */
    private static long    compteurReset = 0;
    /**
     * Boolean qui définit si le jeu est en tour par tour.
     */
    private        boolean tourParTour   = true;
    ;
    /**
     * Booleans qui définissent si on doit arreter ou reset le niveau.
     */
    private boolean demandeReset, demandeFin, finiSuccess;
    /**
     * Entiers servant à stocker les données de jeu.
     */
    private int score, nbDiamants, tempsRestant, tempsTotal, compteurTicks;
    /**
     * Temps au debut de l'essai, utile pour gerer le timer.
     */
    private long tempsAuDebut = System.currentTimeMillis();
    /**
     * Stockage du niveau auquel on joue.
     */
    private Niveau niveau;
    /**
     * Stocke la touche que le joueur a appuyé durant le tick actuel.
     */
    private char   toucheClavier;
    /**
     * String qui stock le trajet qu'a emprunté Rockford depuis le début.
     */
    private String      trajet      = "";
    /**
     * Stock toutes les amibes du niveau.
     */
    private List<Amibe> listeAmibes = new ArrayList<Amibe>();

    /**
     * Stock tous les tickables du niveau.
     */
    private List<Tickable> listeTickable = new ArrayList<Tickable>();

    /**
     * Stock les amibes qui vont être ajoutés à la fin du tick à la vrai liste
     * de tick.(La liste est en train d'etre parcourue quand on veut ajouter un
     * objet donc on le stock ici et on l'ajoutera a la fin du parcours de la
     * liste).
     */
    private List<Amibe> listeAmibesAjout = new ArrayList<Amibe>();

    /**
     * Stock les amibes qui vont être ajoutés à la fin du tick à la vrai liste
     * de tick.(La liste est en train d'etre parcourue quand on veut ajouter un
     * objet donc on le stock ici et on l'ajoutera a la fin du parcours de la
     * liste).
     */
    private List<Tickable> listeTickableAjout = new ArrayList<Tickable>();

    /**
     * Stock chaque diamant attrapé depuis le début du niveau, avec aussi le
     * numéro du tick durant lequel le diamant s'est fait attrapé.
     */
    private List<Paire<Integer, Long>> listeDiamants = new ArrayList<Paire<Integer, Long>>();

    /**
     * Prend un niveau en paramètre et initialise les listes de tickables et
     * amibes.
     *
     * @param niveau Le niveau auquel on va jouer.
     */
    public GererNiveau(Niveau niveau) {
        this.niveau = niveau;
        if (niveau.getCaveDelay() >= 1 && Coeur.tempsReel) {
            tourParTour = false;
        }
        tempsRestant = niveau.getCave_time();
        tempsTotal = tempsRestant;
        for (int i = 0; i < niveau.getMap().length; i++) {
            for (int j = 0; j < niveau.getMap()[i].length; j++) {
                if (niveau.getMap()[i][j] instanceof Tickable) {
                    listeTickable.add((Tickable) niveau.getMap()[i][j]);
                }
            }
        }
        for (int i = 0; i < niveau.getMap().length; i++) {
            for (int j = 0; j < niveau.getMap()[i].length; j++) {
                if (niveau.getMap()[i][j] instanceof Amibe) {
                    listeAmibes.add((Amibe) niveau.getMap()[i][j]);
                }
            }
        }
        Collections.sort(listeTickable);
        Collections.shuffle(listeAmibes);
    }

    /**
     * Tick appelé quand c'est une ia (sauf l'ia directive évoluée) qui joue.
     *
     * @param ia L'ia qui en train de jouer.
     *
     * @return Vrai si le niveau vient d'être fini, faux sinon.
     */
    public boolean tickIa(Ia ia) {
        Score s = null;
        while (!finiSuccess) {
            compteurTicks++;
            toucheClavier = ia.direction(niveau.getMap());
            trajet += toucheClavier;

            if (compteurTicks >= niveau.getCave_time() * niveau.getCaveDelay()) {
                compteurReset++;
                if (ia.getClass().equals(IaEvolue.class)) {
                    s = ((IaEvolue) ia).ajouterScore();
                }
                Partie.resetNiveau();
                break;
            }
            tickInterne();
        }
        if (!finiSuccess) {
            if (ia.getClass().equals(IaEvolue.class)) {
                ((IaEvolue) ia).ajouterScore(s);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Tick appelé quand c'est l'ia directive évoluée qui joue.
     *
     * @param ia L'ia qui en train de jouer.
     *
     * @return Vrai si le niveau vient d'être fini, faux sinon.
     */
    public boolean tickIaDirevol(Ia ia) {
        if (!finiSuccess) {
            compteurTicks++;
            toucheClavier = ia.direction(niveau.getMap());
            trajet += toucheClavier;

            if (compteurTicks >= niveau.getCave_time() * niveau.getCaveDelay()) {
                compteurReset++;
                if (ia.getClass().equals(IaEvolue.class)) {
                    ((IaEvolue) ia).ajouterScore();
                }
                Partie.resetNiveau();
            }
            tickInterne();
        }
        if (!finiSuccess) {
            if (ia.getClass().equals(IaEvolue.class)) {
                ((IaEvolue) ia).ajouterScore();
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Tick appelé quand c'est l'ia en mode simulation qui joue.
     *
     * @param ia L'ia qui en train de jouer.
     * @param c L'instruction de l'ia poru ce tick.
     *
     * @return Vrai si le niveau vient d'être fini, faux sinon.
     */
    public boolean tickIaController(Ia ia, char c) {
        if (!finiSuccess) {
            compteurTicks++;
            toucheClavier = c;
            trajet += c;

            if (compteurTicks >= niveau.getCave_time() * niveau.getCaveDelay()) {
                compteurReset++;
                if (ia.getClass().equals(IaEvolue.class)) {
                    ((IaEvolue) ia).ajouterScore();
                }
                Partie.resetNiveau();
            }
            tickInterne();
        }
        if (!finiSuccess) {
            if (ia.getClass().equals(IaEvolue.class)) {
                ((IaEvolue) ia).ajouterScore();
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Tick appelé quand c'est le joueur qui joue en mode graphique.
     */
    public void tick() {
        toucheClavier = Coeur.CONTROLEUR.getDirection();
        trajet += toucheClavier;
        tickInterne();
    }

    /**
     * Tick appelé quand c'est le joueur qui joue en mode console.
     */
    public void tickConsole(char touche) {
        toucheClavier = touche;
        trajet += toucheClavier;
        tickInterne();
    }

    /**
     * Tick appelé quand le jeu tourne en mode lecture.
     */
    public boolean tickLecture(char touche) {
        toucheClavier = touche;
        return tickInterne();
    }

    /**
     * Appelé par toutes les méthodes tick, gère les objets ainsi que les
     * amibes.
     *
     * @return Vrai si le niveau est fini durant ce tick, sauf sinon.
     */
    public boolean tickInterne() {
        gererLesTickables();
        gererLesAmibes();
        ajouterAll();
        compteurTicks++;
        if (demandeReset) {
            Partie.resetNiveau();
            return true;
        }
        if (demandeFin) {
            Partie.finNiveau();
            return true;

        }
        gererTemps();
        Coeur.CONTROLEUR.tick();
        return false;
    }

    /**
     * Execute le tick de chaque objet Tickable dans la liste de tickables.
     */
    public void gererLesTickables() {
        for (Tickable t : listeTickable) {
            if (!t.isMort()) { t.tick(); }
        }
    }

    /**
     * Gere les amibes de ce niveau.
     */
    public void gererLesAmibes() {
        // son
        if (listeAmibes.isEmpty()) {
            Partie.sons.stopSon1();
        }
        if (!getListeAmibes().isEmpty()) {
            Partie.sons.jouerSon1("amoeba.wav", 965);
        }
        // fin son
        if (listeAmibes.size() > 0 && niveau.getAmoeba_time() != -1 && compteurTicks % niveau.getAmoeba_time() == 0) {
            Collections.shuffle(listeAmibes);
            for (Amibe amibe : listeAmibes) {
                if (amibe.sePropager()) {
                    return;
                }
            }
            listeAmibes.get(0).transformerTousLesAmibesEnDiamant();
        }
    }

    /**
     * Gère le timer ce ce niveau.
     */
    public void gererTemps() {
        long temps = System.currentTimeMillis();
        if (temps - tempsAuDebut > tempsTotal * 1000) {
            demandeReset = true;
            niveau.getRockford().mourir();
        }
        tempsRestant = (int) (tempsTotal - ((temps - tempsAuDebut) / 1000));
    }

    /**
     * Ajoute une amibe a la liste des amibes si elle n'y est pas déjà.
     *
     * @param e L'amibe à ajouter.
     */
    public void ajouterAmibe(Amibe e) {
        boolean ok = true;
        for (Amibe a : listeAmibes) {
            if (a.getId() == e.getId()) {
                ok = false;
                break;
            }
        }
        if (ok) { listeAmibesAjout.add(e); }
    }

    /**
     * Ajoute un tickable a la liste des tickables si il n'y est pas déjà.
     *
     * @param e Le tickable à ajouter.
     */
    public void ajouterTickable(Tickable e) {
        boolean ok = true;
        for (Tickable a : listeTickable) {
            if (a.getId() == e.getId()) {
                ok = false;
                break;
            }
        }
        if (ok) { listeTickableAjout.add(e); }
    }

    /**
     * Ajoute les Tickables et les Amibes qui sont dans les listes éphémères aux
     * listes permanantes.
     */
    public void ajouterAll() {
        listeAmibes.addAll(listeAmibesAjout);
        listeAmibesAjout.clear();
        for (int i = 0; i < listeAmibes.size(); i++) {
            if (listeAmibes.get(i).isMort()) {
                listeAmibes.remove(i);
                i--;
            }
        }
        Collections.shuffle(listeAmibes);
        listeTickable.addAll(listeTickableAjout);
        listeTickableAjout.clear();
        for (int i = 0; i < listeTickable.size(); i++) {
            if (listeTickable.get(i).isMort()) {
                listeTickable.remove(i);
                i--;
            }
        }
        Collections.sort(listeTickable);
    }

    /**
     * Arrête le niveau en netoyant toutes les listes.
     */
    public void stop() {
        listeTickable.clear();
        listeTickableAjout.clear();
        listeAmibes.clear();
        listeAmibesAjout.clear();
        niveau = null;
    }

    /**
     * Incrémente le nombres de diamants obtenus.
     *
     * @param d Le diamant ramassé.
     */
    public void incrementerNbDiamants(Diamant d) {
        d.getSons().jouerSon3("explosionDiamant.wav", 1);
        listeDiamants.add(new Paire<Integer, Long>(compteurTicks, d.getId()));
        nbDiamants++;
    }

    /**
     * Arrête me niveau.
     */
    public void finNiveau() {
        Partie.finNiveau();
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public boolean isTourParTour() {
        return tourParTour;
    }

    /**
     * Un setter.
     *
     * @param tourParTour L'objet en question.
     */
    public void setTourParTour(boolean tourParTour) {
        this.tourParTour = tourParTour;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */

    public int getTicks() {
        return niveau.getCaveDelay();
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public Niveau getNiveau() {
        return niveau;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public boolean isFiniSuccess() {
        return finiSuccess;
    }

    /**
     * Un setter.
     *
     * @param finiSuccess L'objet en question.
     */
    public void setFiniSuccess(boolean finiSuccess) {
        this.finiSuccess = finiSuccess;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */

    public int getScore() {
        return score;
    }

    /**
     * Un setter.
     *
     * @param score L'objet en question.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public int getNbDiamants() {
        return nbDiamants;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public int getTempsRestant() {
        return tempsRestant;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public char getToucheClavier() {
        return toucheClavier;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public List<Amibe> getListeAmibes() {
        return listeAmibes;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public List<Tickable> getListeTickable() {
        return listeTickable;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public boolean isDemandeReset() {
        return demandeReset;
    }

    /**
     * Un setter.
     *
     * @param demandeReset L'objet en question.
     */
    public void setDemandeReset(boolean demandeReset) {
        this.demandeReset = demandeReset;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public boolean isDemandeFin() {
        return demandeFin;
    }

    /**
     * Un setter.
     *
     * @param demandeFin L'objet en question.
     */
    public void setDemandeFin(boolean demandeFin) {
        this.demandeFin = demandeFin;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public String getTrajet() {
        return trajet;
    }

    /**
     * Un setter.
     *
     * @param trajet L'objet en question.
     */
    public void setTrajet(String trajet) {
        this.trajet = trajet;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public int getCompteurTicks() {
        return compteurTicks;
    }

    /**
     * Incrémente le nombre de reset.
     */
    public void incrCompteurReset() {
        compteurReset++;
    }

    /**
     * Reset le compteur de ticks.
     */
    public void resetCompteurTicks() {
        compteurTicks = 0;
    }

    /**
     * Reset le trajet.
     */
    public void resetTrajet() {
        trajet = "";
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public List<Paire<Integer, Long>> getListeDiamants() {
        return listeDiamants;
    }

    /**
     * Un getter.
     *
     * @return L'objet en question.
     */
    public List<Amibe> getListeAmibesAjout() {
        return listeAmibesAjout;
    }

}
