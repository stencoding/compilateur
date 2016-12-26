package compilateur;

import java.util.ArrayList;

/**
 * 
 * Arbre	
 *
 */
public class Arbre {
	
	private Noeud noeud;
	
	private ArrayList<Arbre> enfants = null;

	public Arbre(Noeud noeud, ArrayList<Arbre> enfants) {
		this.noeud = noeud;
		this.enfants = enfants;
	}

	/**
	 * @return the noeud
	 */
	public Noeud getNoeud() {
		return noeud;
	}

	/**
	 * @param noeud the noeud to set
	 */
	public void setNoeud(Noeud noeud) {
		this.noeud = noeud;
	}

	/**
	 * @return the enfants
	 */
	public ArrayList<Arbre> getEnfants() {
		return enfants;
	}

	/**
	 * @param enfants the enfants to set
	 */
	public void setEnfants(ArrayList<Arbre> enfants) {
		this.enfants = enfants;
	}
	
	/**
	 * Affichage de l'arbre
	 * 
	 * @param arbre
	 * @param level
	 */
	public static void affiche(Arbre arbre, int level) {
		if (arbre == null) return;
		String printLevel = "";
		for (int i = 0; i<=level; i++) {
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
	
	/**
	 * Affichage de l'arbre au format json
	 * TODO : à finir
	 * 
	 * @param arbre
	 * @param level
	 */
	public static void afficheJson(Arbre arbre, int level) {
		if (arbre == null) return;

		System.out.println("{");
		System.out.println("\"" + arbre.getNoeud().getCategorie() + "\"" + ": {");
		// FIXME : si on sépare la valeur d'un noeud en 2 (Int et Str 
		// comme pour Token (cf classe Noeud)
		
		if (arbre.enfants == null) {
			System.out.println("}");
			return;
		}

		System.out.println("\"enfants\":[");
		for (Arbre enfant : arbre.enfants) {
			afficheJson(enfant, level+1);
		}
		System.out.println("}");
		System.out.println("]");
		System.out.println("}");

		return;
	}
}
