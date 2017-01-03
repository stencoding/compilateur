/**
 * 
 * Token (FRONTEND)
 * Il est créé lors de l'analyse lexicale.
 * Lorsque des mots-clés, ... sont reconnus, ils sont remplacés par des tokens.
 *
 * @author Mathilde PREVOST & Steve NEGRINE
 */
public class Token {
	
	/**
     * La classe du Token.
     * 
     * @see Classe
     */	
	private Classe classe;
	
	/**
     * La valeur du Token si int.
     */	
	private int chargeInt;
	
	/**
     * La valeur du Token si str.
     */	
	private String chargeStr;
	
	/**
	 * Constructeur Token.
	 */
	public Token() {}

	/**
     * Retourne la classe du token
     * 
     * @return La classe du token. 
     */
	public Classe getClasse() {
		return classe;
	}

	/**
     * Met à jour la classe du token.
     * 
     * @param classe
     *            La classe du token.
     * 
     */
	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	/**
     * Retourne la valeur int du token
     * 
     * @return La valeur int du token. 
     */
	public int getChargeInt() {
		return chargeInt;
	}

	/**
     * Met à jour la valeur int du token.
     * 
     * @param chargeInt
     *            La valeur int du token.
     * 
     */
	public void setChargeInt(int chargeInt) {
		this.chargeInt = chargeInt;
	}
	
	/**
     * Retourne la valeur str du token
     * 
     * @return La valeur str du token. 
     */
	public String getChargeStr() {
		return chargeStr;
	}

	/**
     * Met à jour la valeur str du token.
     * 
     * @param chargeStr
     *            La valeur str du token.
     * 
     */
	public void setChargeStr(String chargeStr) {
		this.chargeStr = chargeStr;
	}

	/**
	 * Affiche le token
	 */
	public String toString() {
		return "Token [classe=" + classe + ", chargeInt=" + chargeInt + ", chargeStr=" + chargeStr + "]";
	}

}
