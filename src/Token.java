

/**
 * 
 * Token (FRONTEND)
 * Il est créé lors de l'analyse lexicale.
 * Lorsque des mots-clés, ... sont reconnus, ils sont remplacés par des tokens.
 *
 */
public class Token {

	private Classe classe;
	private int chargeInt;
	private String chargeStr;
	private String fichier;
	private int ligne;
	
	public Token() {}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public int getChargeInt() {
		return chargeInt;
	}

	public void setChargeInt(int chargeInt) {
		this.chargeInt = chargeInt;
	}

	public String getChargeStr() {
		return chargeStr;
	}

	public void setChargeStr(String chargeStr) {
		this.chargeStr = chargeStr;
	}

	public String getFichier() {
		return fichier;
	}

	public void setFichier(String fichier) {
		this.fichier = fichier;
	}

	public int getLigne() {
		return ligne;
	}

	public void setLigne(int ligne) {
		this.ligne = ligne;
	}

	@Override
	public String toString() {
		return "Token [classe=" + classe + ", chargeInt=" + chargeInt + ", chargeStr=" + chargeStr + "]";
	}

}
