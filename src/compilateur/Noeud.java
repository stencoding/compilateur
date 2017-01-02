package compilateur;

/**
 * Noeud : générer par le FRONTEND (Parser) et utilisé dans le BACKEND (tout ce qui est lié au langage cible) par le Generator.
 * Il compose l'arbre syntaxique et est lui-même composé d'une Catégorie.
 */
public class Noeud {

	// Catégorie du noeud (= la classe d'un Token)
	private Categorie categorie;
	
	// Valeur du noeud si int (= chargeInt d'un Token)
	private int intValue;
	
	// Valeur du noeud si str (= chargeStr d'un Token)
	private String strValue;
		
	// Position dans la pile (slot)
	private int position;
		
	public Noeud(Categorie categorie) {
		this.categorie = categorie;
	}
	
	/**
	 * Retourne le noeud correspondant au token
	 * 
	 * @param token
	 * @return noeud
	 * @throws Exception 
	 */
	public static Noeud tokenToNode (Token token) throws Exception {
		// Pour des raisons de simplicité
		// on créer ici le noeud à partir du token passé
		Categorie categorie;
		int intValue = token.getChargeInt();
		String strValue = token.getChargeStr();
		switch (token.getClasse()) {
			// Opérateurs
			case TOK_ADD:
				categorie = Categorie.ADD;
				break;
			case TOK_SUB:
				categorie = Categorie.SUB;
				break;
			case TOK_MUL:
				categorie = Categorie.MUL;
				break;
			case TOK_DIV:
				categorie = Categorie.DIV;
				break;
			case TOK_MODULO:
				categorie = Categorie.MODULO;
				break;
			case TOK_COMPARE:
				categorie = Categorie.COMPARE;
				break;
			case TOK_DIFF:
				categorie = Categorie.DIFF;
				break;
			case TOK_INF:
				categorie = Categorie.INF;
				break;
			case TOK_SUP:
				categorie = Categorie.SUP;
				break;
			case TOK_INF_EGAL:
				categorie = Categorie.INF_EGAL;
				break;
			case TOK_SUP_EGAL:
				categorie = Categorie.SUP_EGAL;
				break;
			case TOK_EGAL:
				categorie = Categorie.EGAL;
				break;
			// Mots-clés
			case TOK_IF:
				categorie = Categorie.IF;
				break;
			case TOK_VAR:
				categorie = Categorie.VAR;
				break;
			// Type de variable
			case TOK_INT:
				categorie = Categorie.INT;
				break;
			case TOK_STR:
				categorie = Categorie.STR;
				break;
			// stocke une constante
			case TOK_CST_INT:
				categorie = Categorie.CST_INT;
				break;
			case TOK_CST_STR:
				categorie = Categorie.CST_STR;
				break;
			case TOK_IDENT:
				categorie = Categorie.IDENT;
				break;
			default:
				throw new Exception("<" + token.getClasse() + "> inconnu pour construire le noeud");
		}
		
		Noeud node = new Noeud(categorie);
		node.setStrValue(strValue);
		node.setIntValue(intValue);
		
		return node;
	}
	
	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public int getIntValue() {
		return this.intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Noeud [categorie=" + categorie + ", intValue=" + intValue + ", strValue=" + strValue + ", position="
				+ position + "]";
	}

}
