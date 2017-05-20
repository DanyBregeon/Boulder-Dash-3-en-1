package testUnitaire;

import entitees.abstraites.Entitee;
import entitees.fixes.Sortie;
import entitees.tickables.Diamant;
import entitees.tickables.Pierre;
import entitees.tickables.Rockford;
import junit.framework.TestCase;
import loader.Loader;
import main.GererNiveau;
import main.Partie;

public class RockfordTest extends TestCase{

	private static Rockford rockford;
	
	public static void setUpBeforeClass() {
		Partie.ensembleDeNiveau = Loader.charger_ensemble_de_niveaux("niveau.bdcff");
		Partie.niveau = 1;
		Partie.gererNiveau = new GererNiveau(Partie.ensembleDeNiveau.getNiveaux().get(Partie.niveau - 1).clone());
		rockford = Partie.gererNiveau.getNiveau().getRockford();
	}
	
	public void testRamasserDiamant() {
		setUpBeforeClass();
		int nbDiamant = Partie.gererNiveau.getNbDiamants();
		Diamant d = new Diamant(1,2);
		rockford.ramasserDiamant(d);
		assertTrue(Partie.gererNiveau.getNbDiamants() == nbDiamant+1);
	}
	
	public void testContactAutreEntitee() {
		setUpBeforeClass();
		Entitee e = new Pierre(1,2);
		assertTrue(rockford.contactAutreEntitee(e) == 0);
		e = new Diamant(1,2);
		assertTrue(rockford.contactAutreEntitee(e) == 1);
		e = new Sortie(1,2);
		assertTrue(rockford.contactAutreEntitee(e) == 0);
		Diamant d = new Diamant(1,2);
		for(int i=0; i<=Partie.gererNiveau.getNiveau().getDiamonds_required()+1; i++){
			rockford.ramasserDiamant(d);
		}
		assertTrue(rockford.contactAutreEntitee(e) == 1);
	}
	
}