package compilateur;

public class Noeud {

	// Catégorie du noeud
	private Classe classe;
	// Position dans la pile (slot)
	private int position;
	
	public Noeud() {}

	/**
	 * @return the classe
	 */
	public Classe getClasse() {
		return classe;
	}

	/**
	 * @param classe the classe to set
	 */
	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Noeud [classe=" + classe + ", position=" + position + "]";
	}

}
