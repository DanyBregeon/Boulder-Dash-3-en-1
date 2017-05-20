package entitees.tickables;

import static entitees.abstraites.Entitee.Entitees.Diamant;
import static entitees.abstraites.Entitee.Entitees.Explosion;
import static entitees.abstraites.Entitee.Entitees.Libellule;
import static entitees.abstraites.Entitee.Entitees.Luciole;
import static entitees.abstraites.Entitee.Entitees.MurMagique;
import static entitees.abstraites.Entitee.Entitees.Rockford;

import entitees.abstraites.Entitee;
import entitees.abstraites.Tickable;
import main.Partie;

/**
 * Cette classe représente les entitées Diamants.
 * 
 * @author celso
 */
public class Diamant extends Tickable {

	/**
	 * Constructeur qui prend les coordonnées.
	 * 
	 * Ajoute les case de déplacement possibles pour cet objet.
	 * 
	 * @param x
	 *            Coordonnée en x.
	 * @param y
	 *            Coordonnée en y.
	 */
	public Diamant(int x, int y) {
		super(x, y);
		setDestructible(true);
		getDeplacementsPossibles().add(Rockford);
		getDeplacementsPossibles().add(MurMagique);
		getDeplacementsPossibles().add(Libellule);
		getDeplacementsPossibles().add(Luciole);
		getDeplacementsPossibles().add(Explosion);
		enumeration = Diamant;
	}

	@Override
	protected int contactAutreEntitee(Entitee entitee) {
		setDirection('b');
		if (entitee.is(Rockford)) {
			mourir();
			((Rockford) entitee).ramasserDiamant(this);
			return -1;
		} else if (entitee.is(MurMagique)) {
			return 0;
		} else if (entitee.is(Libellule)) {
			exploser(true);
			return 0;
		} else if (entitee.is(Luciole)) {
			exploser(false);
			return 0;
		} else if (entitee.is(Explosion)) {
			mourir();
			return -1;
		}
		return 1;
	}

	@Override
	public void tick() {
		gererChute();
		rockfordEndessous();
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
	 * Regarde si Rockford se trouve en dessous, si oui l'objet se met à chuter.
	 */
	private void rockfordEndessous() {
		if (Partie.gererNiveau.getNiveau().getMap()[getX()][getY() + 1].is(Rockford)) {
			setChute(true);
		}
	}
}
