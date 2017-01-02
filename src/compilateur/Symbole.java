package compilateur;

/**
 * 
 * Symbole (entre FRONTEND et BACKEND).
 * Il représente une variable et est utilisé dans la Table des Symboles.
 *
 */
public class Symbole {

	// Identificateur du token
	private String ident;
	
	// Type de l'identificateur (variable)
	private String type;
	
	// Position dans la mémoire (adresse mémoire si fonction)
	private int	position;
	
	/**
	 * Insère le type du symbole en fonction du token
	 * 
	 * @param node
	 * @param symbole
	 * @throws Exception
	 */
	public static void setTypeSymbole (Noeud node, Symbole symbole) throws Exception {
		// Pour des raisons de simplicité
		// on créer ici le type à partir du token passé
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
	
	public Symbole(String ident) {
		this.ident = ident;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Symbole [ident=" + ident + ", type=" + type + ", position=" + position + "]";
	}

}
