import java.util.ArrayList;

/**
 * 
 * Arbre (entre FRONTEND et BACKEND).
 * Cet arbre sert notamment pour créer un arbre syntaxique composé de Noeuds.
 * Il est créé par le Lexer et analysé par le Generator pour généré le code (pseudo-code assembleur).
 *
 * @author Mathilde PREVOST & Steve NEGRINE
 */

public class Arbre {
	
	/**
     * Le noeud de l'arbre.
     * 
     * @see Noeud
     * @see Arbre#getNoeud()
     * @see Arbre#setNoeud(Noeud)
     */
	private Noeud noeud;
	
	/**
     * Les enfants de l'arbre. 
     * Il est possible d'ajouter ou de retirer des enfants dans cette liste.
     * 
     * @see Arbre#getEnfants()
     * @see Arbre#setEnfants(ArrayList<Arbre>)
     */
	private ArrayList<Arbre> enfants = null;

	/**
     * Constructeur Arbre.
     * 
     * @param noeud
     *            Le noeud de l'arbre.
     * @param enfants
     *            Les enfants de l'arbre.
     * 
     * @see Arbre#noeud
     * @see Arbre#enfants
     */
	public Arbre(Noeud noeud, ArrayList<Arbre> enfants) {
		this.noeud = noeud;
		this.enfants = enfants;
	}
	
	/**
     * Retourne le noeud de l'arbre
     * 
     * @return Le noeud de l'arbre. 
     */
	public Noeud getNoeud() {
		return noeud;
	}

    /**
     * Met à jour le noeud de l'arbre.
     * 
     * @param noeud
     *            Le nouveau noeud de l'arbre.
     * 
     * @see Noeud
     */
	public void setNoeud(Noeud noeud) {
		this.noeud = noeud;
	}

	/**
     * Retourne les enfants de l'arbre
     * 
     * @return Les enfants de l'arbre. 
     */
	public ArrayList<Arbre> getEnfants() {
		return enfants;
	}

	/**
     * Met à jour les enfants de l'arbre.
     * 
     * @param enfants
     *            Les nouveaux enfants de l'arbre.
     * 
     */
	public void setEnfants(ArrayList<Arbre> enfants) {
		this.enfants = enfants;
	}
	
	/**
	 * Affiche l'arbre
	 * Fonction récursive
	 * 
	 * @param arbre
     *            L'arbre à afficher.
	 * @param level
     *            Le niveau du sous-arbre dans l'arbre.
	 */
	public static void affiche(Arbre arbre, int level) {
		if (arbre == null) {
			return;
		}
		String printLevel = "";
		for (int i = 0 ; i <= level ; i++) {
			printLevel += "--";
		}
		System.out.println(printLevel + " " + arbre.getNoeud().toString());
		
		if (arbre.enfants == null) {
			return;
		}

		for (Arbre enfant : arbre.enfants) {
			affiche(enfant, level+1);
		}
		return;
	}
}