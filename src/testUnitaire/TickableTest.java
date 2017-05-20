package testUnitaire;

import entitees.abstraites.Tickable;
import entitees.tickables.Luciole;
import junit.framework.TestCase;
import loader.Loader;
import main.GererNiveau;
import main.Partie;

public class TickableTest extends TestCase {

    public static void setUpBeforeClass() {
        Partie.ensembleDeNiveau = Loader.charger_ensemble_de_niveaux("niveau.txt");
        Partie.niveau = 1;
        Partie.gererNiveau = new GererNiveau(Partie.ensembleDeNiveau.getNiveaux().get(Partie.niveau - 1).clone());
    }

    public void testPlaceLibre() {
        setUpBeforeClass();
        Tickable rockford = Partie.gererNiveau.getNiveau().getRockford();
        assertTrue(rockford.placeLibre(1, 2));
        assertTrue(!rockford.placeLibre(0, 1));
    }

    public void testSeDeplacer() {
        setUpBeforeClass();
        Tickable rockford = Partie.gererNiveau.getNiveau().getRockford();
        rockford.setDirection('h');
        assertTrue(!rockford.seDeplacer());
        rockford.setDirection(' ');
        assertTrue(!rockford.seDeplacer());
        rockford.setDirection('d');
        assertTrue(rockford.seDeplacer());
        Tickable luciole = new Luciole(3, 1);
        luciole.setDirection('h');
        assertTrue(!luciole.seDeplacer());
        luciole.setDirection('g');
        assertTrue(luciole.seDeplacer());

    }

    public void testContactAutreEntitee() {
        //setUpBeforeClass();
    }

}
