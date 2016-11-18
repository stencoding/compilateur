package compilateur;

import java.util.ArrayList;

/**
 * 
 * Arbre	
 *
 */
public class Arbre {
	
	private Token noeud;
	
	private ArrayList<Arbre> enfants = null;

	public Arbre(Token noeud, ArrayList<Arbre> enfants) {
		this.noeud = noeud;
		this.enfants = enfants;
	}

	/**
	 * @return the noeud
	 */
	public Token getNoeud() {
		return noeud;
	}

	/**
	 * @param noeud the noeud to set
	 */
	public void setNoeud(Token noeud) {
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
		System.out.println("\"" + arbre.getNoeud().getClasse() + "\"" + ": {");
		System.out.println("\"chargeInt\" : " + "\"" + arbre.getNoeud().getChargeInt() + "\",");
		System.out.println("\"chargeStr\" : " + "\"" + arbre.getNoeud().getChargeStr() + "\",");
		
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
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * 
	 * TODO : finir affichage
	 */
//	@Override
//	public String toString() {
//		return this.affiche(this, 0);
//	}

}
