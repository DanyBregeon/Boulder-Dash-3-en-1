package testUnitaire;

import entitees.abstraites.Entitee;
import entitees.tickables.Diamant;
import ia.IaDirective;
import junit.framework.TestCase;
import loader.Loader;
import main.GererNiveau;
import main.Partie;
import outils.Noeud;

public class IaDirectiveTest extends TestCase{

	private static Entitee[][] map;
	private static Noeud[][] graphe;
	private static IaDirective ia;
	
	public static void setUpBeforeClass() {
		Partie.ensembleDeNiveau = Loader.charger_ensemble_de_niveaux("niveau.txt");
		Partie.niveau = 1;
		Partie.gererNiveau = new GererNiveau(Partie.ensembleDeNiveau.getNiveaux().get(Partie.niveau - 1).clone());
		Partie.ia = new IaDirective();
		ia = (IaDirective) Partie.ia;
		map = Partie.gererNiveau.getNiveau().getMap();
		graphe = new Noeud[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				graphe[i][j] = new Noeud(map[i][j]);
			}
		}
		ia.setGraphe(graphe);
	}
	
	public void testPositionValide() {
		setUpBeforeClass();		
		Noeud n = graphe[1][2];
		assertTrue(((IaDirective) Partie.ia).positionValide(n.getX(), n.getY()) == true);
		n = graphe[0][0];
		assertTrue(((IaDirective) Partie.ia).positionValide(n.getX(), n.getY()) == false);
		assertTrue(((IaDirective) Partie.ia).positionValide(-42,-42) == false);
	}
	
	public void testVoisinNoeud() {
		setUpBeforeClass();		
		Noeud n = graphe[1][2];
		assertTrue(((IaDirective) Partie.ia).VoisinsNoeud(n).contains(graphe[2][2]));
		assertTrue(!((IaDirective) Partie.ia).VoisinsNoeud(n).contains(graphe[3][3]));
		assertTrue(!((IaDirective) Partie.ia).VoisinsNoeud(n).contains(graphe[0][2]));
	}
	
	public void testdiamantLePlusProche() {
		setUpBeforeClass();		
		Noeud n = graphe[1][2];
		assertTrue(((IaDirective) Partie.ia).diamantLePlusProche(n).getEntite().getClass() == Diamant.class);
	}

	
	public void testTick() {
		setUpBeforeClass();
		assertTrue(ia.tick(map) != 'a' && ia.tick(map) != 'h' && ia.tick(map) != 'g');
	}
	
	public void testCheminPlusCourt() {
		setUpBeforeClass();		
		Noeud d = graphe[1][1];
		Noeud a = graphe[5][5];
		assertTrue(((IaDirective) Partie.ia).cheminPlusCourt(d,a).contains(a));
		d = graphe[0][0];
		assertTrue(((IaDirective) Partie.ia).cheminPlusCourt(d,a) == null);
	}

}