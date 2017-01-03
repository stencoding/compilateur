import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 * 
 * Table des symboles (entre FRONTEND et BACKEND).
 * Elle stocke les variables, leur type et leur portée dans le scope.
 * Les variables sont stockées sous forme de Symbole.
 *
 * @author Mathilde PREVOST & Steve NEGRINE
 */
public class TableDeSymbole {

	/**
     * La pile de la tableDeSymbole (table de hashage)
     */	
	private Stack<HashMap<String, Symbole>> pile;

	/**
	 * Constructeur TableDeSymbole.
	 * 
	 */
	public TableDeSymbole() {
		this.pile = new Stack<HashMap<String, Symbole>>();
	}

	/**
	 * Recherche de manière récursive un identificateur dans la pile.
	 * Remonte dans la hiérarchie des blocs (tables de hashage).
	 * Renvoie une erreur si pas d'identificateur trouvé. 
	 * 
	 * @param ident
     *            L'ident que l'on cherche dans la table de hashage
	 * @return Le symbole trouvé.
	 * @throws Exception 
	 */
	public Symbole chercher(String ident) throws Exception {

		for (HashMap<String, Symbole> bloc : pile) {
			Symbole symbole = bloc.get(ident);
			if (symbole != null) {
				return symbole;
			}
		}
		
		throw new Exception ("Identificateur inconnu : " + ident);
	}

	/**
	 * Recherche dans la table de hashage courante (bloc courant). 
	 * Retourne une erreur si l'identificateur existe. 
	 * Sinon on crée un symbole et on l'ajoute à la table de hashage.
	 * 
	 * @param ident
     *            L'ident que l'on cherche dans la table de hashage
	 * @return Le symbole créé
	 * @throws Exception
	 */
	public Symbole definir(String ident) throws Exception {
		// récupération du bloc courant (1er élément de la pile)
		HashMap<String, Symbole> blocCourant = this.pile.lastElement();
		// récupère le symbole
		Symbole symbole = blocCourant.get(ident);
		
		// on crée une erreur car identificateur déjà présent
		if (symbole != null) {
			throw new Exception("Variable déjà présente : " + ident);
		}
		
		// on créé le nouveau symbole
		symbole = new Symbole(ident);

		// ajout du symbole au bloc
		blocCourant.put(ident, symbole);

		return symbole;
	}

	/**
	 * Démarre une nouvelle table de hashage (nouveau bloc). 
	 * Ajoute à la pile
	 */
	public void push() {
		this.pile.push(new HashMap<String, Symbole>());
		return;
	}

	/**
	 * Supprime la dernière table de hashage dans la pile (la plus haute).
	 */
	public void pop() {
		this.pile.pop();
		return;
	}

	/**
	 * Affiche la tableDeSymbole
	 */
	public String toString() {
		String string = "TableDeSymbole : \n\n";
		int i = 0;
		
		for (HashMap<String, Symbole> bloc : pile) {
			i++;
			// Obtenir de la liste des clés
			Set<String> listIdent = bloc.keySet(); 
			Iterator<String> iterateur = listIdent.iterator();

			string += "bloc numero " + i+" (taille : " + bloc.size() + ")\n";
			// Parcourir les clés et afficher les entrées de chaque clé;
			while (iterateur.hasNext()) {
				Object identificateur = iterateur.next();
				string += "identificateur = " + identificateur + ", symbole = " + bloc.get(identificateur);
				string += "\n";
			}
		}		
		return string;
	}
}