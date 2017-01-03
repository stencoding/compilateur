/**
 * 
 * Symbole (entre FRONTEND et BACKEND).
 * Il représente une variable et est utilisé dans la Table des Symboles.
 *
 * @author Mathilde PREVOST & Steve NEGRINE
 */

public class Symbole {

	/**
     * L'identificateur du symbole.
     */	
	private String ident;
	
	/**
     * Le type de l'identificateur (variable).
     */	
	private String type;
	
	/**
     * La position dans la mémoire (adresse mémoire si fonction).
     */	
	private int	position;
	
	/**
	 * Constructeur Symbole.
	 * 
	 * @param ident
     *            L'ident du Symbole
	 */
	public Symbole(String ident) {
		this.ident = ident;
	}	
	
	/**
	 * Met à jour le type du symbole en fonction du noeud
	 * 
	 * @param node
	 * @param symbole
	 * @throws Exception
	 */
	public static void setTypeSymbole (Noeud node, Symbole symbole) throws Exception {
		switch (node.getCategorie()) {
			case INT:
			case CST_INT:
				symbole.setType("INT");
				break;
			case STR:
			case CST_STR:
				symbole.setType("STR");
				break;
			default:
				break;
		}
	}
	
	/**
     * Retourne l'ident du symbole
     * 
     * @return L'ident du symbole. 
     */
	public String getIdent() {
		return ident;
	}
	
	/**
     * Met à jour l'ident du symbole.
     * 
     * @param ident
     *            L'ident du symbole.
     * 
     */
	public void setIdent(String ident) {
		this.ident = ident;
	}
	
	/**
     * Retourne le type du symbole
     * 
     * @return Le type du symbole. 
     */
	public String getType() {
		return type;
	}
	
	/**
     * Met à jour le type du symbole.
     * 
     * @param type
     *            Le type du symbole.
     * 
     */
	public void setType(String type) {
		this.type = type;
	}

	/**
     * Retourne la position du symbole
     * 
     * @return La position du symbole. 
     */
	public int getPosition() {
		return position;
	}

	/**
     * Met à jour la position du symbole.
     * 
     * @param position
     *            La position du symbole.
     * 
     */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Affiche le symbole
	 */
	public String toString() {
		return "Symbole [ident=" + ident + ", type=" + type + ", position=" + position + "]";
	}

}
