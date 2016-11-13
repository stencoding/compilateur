package compilateur;

public class Symbole {

	// Identificateur du token
	private String ident;
	
	// Type de l'identificateur (variable)
	private String type;
	// TODO: pourquoi c'est un String et pas une Classe ?
	
	// Position dans la mémoire (adresse mémoire si fonction)
	private int	position;
	
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
		return "Symbole [ident=" + ident + "]";
	}

}
