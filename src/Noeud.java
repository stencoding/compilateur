/**
 * Noeud : générer par le FRONTEND (Parser) et utilisé dans le BACKEND (tout ce qui est lié au langage cible) par le Generator.
 * Il compose l'arbre syntaxique et est lui-même composé d'une Catégorie.
 * 
 * @author Mathilde PREVOST & Steve NEGRINE
 */

public class Noeud {

	/**
     * La catégorie du noeud (= classe d'un Token).
     * 
     * @see Categorie
     * 
     * @see Noeud#getCategorie()
     */
	private Categorie categorie;
	
	/**
     * Valeur du noeud si int (= chargeInt d'un Token).
     * 
     * @see Noeud#getIntValue()
     * @see Noeud#setIntValue(int)
     * 
     */
	private int intValue;
	

	/**
     * Valeur du noeud si str (= chargeStr d'un Token).
     * 
     * @see Noeud#getStrValue()
     * @see Noeud#setStrValue(String)
     * 
     */
	private String strValue;
		
	/**
     * Position dans la pile (slot).
     * 
     * @see Noeud#getPosition()
     * @see Noeud#setPosition(int)
     * 
     */
	private int position;
	
	/**
     * Constructeur Noeud.
     * 
     * @param categorie
     *            La catégorie du noeud.
     * 
     * @see Noeud#categorie
     */
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
		Categorie categorie;
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

		int intValue = token.getChargeInt();
		String strValue = token.getChargeStr();
		
		Noeud node = new Noeud(categorie);
		node.setStrValue(strValue);
		node.setIntValue(intValue);
		
		return node;
	}
	
	/**
     * Retourne la catégorie du noeud
     * 
     * @return La catégorie du noeud. 
     */
	public Categorie getCategorie() {
		return categorie;
	}
	
	/**
     * Retourne la valeur int du noeud
     * 
     * @return La valeur int du noeud. 
     */
	public int getIntValue() {
		return this.intValue;
	}
	
	/**
     * Met à jour la valeur int du noeud.
     * 
     * @param intValue
     *            La valeur int du noeud.
     * 
     */
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}
	
	/**
     * Retourne la valeur str du noeud
     * 
     * @return La valeur str du noeud. 
     */
	public String getStrValue() {
		return strValue;
	}
	
	/**
     * Met à jour la valeur str du noeud.
     * 
     * @param strValue
     *            La valeur str du noeud.
     * 
     */
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	
	/**
     * Retourne la position du noeud dans la pile.
     * 
     * @return La position du noeud dans la pile.
     */
	public int getPosition() {
		return position;
	}
	
	/**
     * Met à jour la position du noeud dans la pile.
     * 
     * @param position
     *            La position du noeud dans la pile.
     * 
     */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Affiche le noeud
	 */
	public String toString() {
		return "Noeud [categorie=" + categorie + ", intValue=" + intValue + ", strValue=" + strValue + ", position="
				+ position + "]";
	}
}