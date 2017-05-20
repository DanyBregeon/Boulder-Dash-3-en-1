package ia;

import entitees.abstraites.Entitee;

/**
 * Classe représentant les IA's.
 * 
 * @author celso
 *
 */
public abstract class Ia {

	/**
	 * Booleen qui sert à indiquer à l'ia que le niveau s'est reset.
	 */
	protected boolean reset = true;

	/**
	 * Méthode qui sera appelée à chaque tick et renvoit le direction que
	 * Rockford doit prendre d'après l'ia.
	 * 
	 * Prend en paramètre la carte du niveau afin d'effectuer le calcul.
	 * 
	 * @param map
	 *            La carte du niveau.
	 * 
	 * @return La direction que l'ia choisi.
	 */
	public char direction(Entitee[][] map) {
		if (reset) {
			initialiserTry();
			reset = false;
		}
		return tick(map);
	}

	/**
	 * Calcule la direction à envoyer.
	 * 
	 * Prend en paramètre la carte du niveau pour effecuer le calcul.
	 * 
	 * @param map
	 *            La carte du niveau.
	 * 
	 * @return La direction que l'ia choisi.
	 */
	public abstract char tick(Entitee[][] map);

	/**
	 * Méthode que l'ia effectue à chaque début d'essai.
	 */
	public abstract void initialiserTry();

	/**
	 * Met le booleen reset en vrai.
	 */
	public void reset() {
		reset = true;
	}

	/**
	 * Renvoie une direction au hasard.
	 * 
	 * @return Une direction au hasard.
	 */
	public static char directionRandom() {
		int random = 1 + (int) (Math.random() * 5);
		switch (random) {
		case 1:
			return 'h';
		case 2:
			return 'b';
		case 3:
			return 'd';
		case 4:
			return 'g';
		default:
			return 'a';
		}
	}
}
