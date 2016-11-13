package compilateur;

public class Token {

	private Classe classe;
	private int chargeInt;
	private String chargeStr;
	private String fichier;
	private int ligne;
	private int position;
	
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Token [classe=" + classe + ", chargeInt=" + chargeInt + ", chargeStr=" + chargeStr + "]";
	}

}