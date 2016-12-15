package compilateur;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Arrays;

/**
 * 
 * Analyseur lexical (FRONTEND)
 *
 */
public class Lexer {

	private String filename;
	private String[] data;
	private Token buffer;
	private int pos;

	public Lexer(String filename) throws Exception {
		this.filename = filename;
		this.pos = 0;
		this.data = lectureFile().split("");
		this.buffer = prochain();
	}

	/**
	 * Lecture du fichier "filename"
	 * 
	 * @return String chaine
	 * @throws Exception
	 */
	private String lectureFile() throws Exception {
		String chaine = "";

		try {
			File file = new File(filename);
			
			if (!file.isFile()) {
				throw new Exception("Fichier introuvable");
			}
			
			LineNumberReader lineReader = null;
			String ligne;
			lineReader = new LineNumberReader(new FileReader(filename));
			
			while ((ligne = lineReader.readLine()) != null) {
				chaine += ligne + "\n";
			}
			
			lineReader.close();

		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return chaine;
	}

	/**
	 * Prend le prochain élément de la chaîne,
	 * crée le token et avance dans la chaîne
	 * 
	 * @return Token token
	 * @throws Exception 
	 */
	public Token prochain() throws Exception {
		Token token = new Token();

		if (data.length == this.pos+1) {
			token.setClasse(Classe.TOK_EOF);
			return token;
		}
		
		while (estEspace(data[this.pos])) {
			this.pos++;
		}
		
		while (estSautDeLigne(data[this.pos])) {
			this.pos++;
		}
		
		while (estIndentation(data[this.pos])) {
			this.pos++;
		}
		
		if (estChiffre(data[this.pos])) {
			int intTmp = lireInt();
			token.setChargeInt(intTmp);
			token.setClasse(Classe.TOK_CST_INT);
			return token;
		}

		if (estGuillemet(data[this.pos])) {
			token.setChargeStr(lireChaine());
			token.setClasse(Classe.TOK_CST_STR);
			this.pos++;
			return token;
		}

		if (estLettre(data[this.pos])) {
			String strTmp = lireIdent();
			token.setClasse(filtrerMotCle(strTmp));
			
			if (token.getClasse().equals(Classe.TOK_IDENT)) {
				token.setChargeStr(strTmp);
			}
			
			return token;
		}

		switch (data[this.pos]) {
		case "+":
			token.setClasse(Classe.TOK_ADD);
			break;

		case "-":
			token.setClasse(Classe.TOK_SUB);
			break;

		case "*":
			token.setClasse(Classe.TOK_MUL);
			break;

		case "/":
			token.setClasse(Classe.TOK_DIV);
			break;

		case "%":
			token.setClasse(Classe.TOK_MODULO);
			break;

		case "(":
			token.setClasse(Classe.TOK_PAR_OUVR);
			break;

		case ")":
			token.setClasse(Classe.TOK_PAR_FERM);
			break;
			
		case "{":
			token.setClasse(Classe.TOK_ACC_OUVR);
			break;

		case "}":
			token.setClasse(Classe.TOK_ACC_FERM);
			break;

		case "!=":
			token.setClasse(Classe.TOK_DIFF);
			break;

		case "=":
			if (data[this.pos++] == "=") {
				token.setClasse(Classe.TOK_COMPARE);
				break;
			}

			this.pos--;
			token.setClasse(Classe.TOK_EGAL);
			break;

		case ">":
			if (data[this.pos++] == "=") {
				token.setClasse(Classe.TOK_SUP_EGAL);
				break;
			}

			this.pos--;
			token.setClasse(Classe.TOK_SUP);
			break;

		case "<":
			if (data[this.pos++] == "=") {
				token.setClasse(Classe.TOK_INF_EGAL);
				break;
			}

			this.pos--;
			token.setClasse(Classe.TOK_INF);
			break;
		
		case ":":
			token.setClasse(Classe.TOK_DEUX_POINTS);
			break;	
			
		case ";":
			token.setClasse(Classe.TOK_POINT_VIRGULE);
			break;
			
		case ",":
			token.setClasse(Classe.TOK_VIRGULE);
			break;
		}

		if (token.getClasse() == null) {
			throw new Exception("caractère inconnu : <" + data[this.pos] + ">");
		}

		this.pos++;

		return token;
	}

	/**
	 * Retourne le token courant et passe au suivant
	 * 
	 * @return Token tokenSuivant
	 * @throws Exception 
	 */
	public Token next() throws Exception {
		Token tokenCourant = this.buffer;
		this.buffer = prochain();
		
		return tokenCourant;
	}

	/**
	 * Retourne le token suivant sans avancer
	 * 
	 * @return Token tokenSuivant
	 */
	public Token look() {
		return this.buffer;
	}

	/**
	 * Si le caractère en paramètre est un espace
	 * 
	 * @param caractere
	 * @return boolean
	 */
	private boolean estEspace(String caractere) {
		if (caractere.equals(" ")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Si le caractère en paramètre est un saut de ligne
	 * 
	 * @param caractere
	 * @return boolean
	 */
	private boolean estSautDeLigne(String caractere) {
		if (caractere.equals("\n")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Si le caractère en paramètre est une indentation
	 * 
	 * @param caractere
	 * @return boolean
	 */
	private boolean estIndentation(String caractere) {
		if (caractere.equals("\t")) {
			return true;
		}
		return false;
	}

	/**
	 * Si le caractère en paramètre est un guillemet
	 * 
	 * @param caractere
	 * @return boolean
	 */
	private boolean estGuillemet(String caractere) {
		if (caractere.equals("\"")) {
			return true;
		}
		return false;
	}

	/**
	 * Si le caractère en paramètre est un chiffre
	 * 
	 * @param caractere
	 * @return boolean
	 */
	private boolean estChiffre(String caractere) {
		if (caractere.matches("[0-9]")) {
			return true;
		}
		return false;
	}

	/**
	 * Si le caractère en paramètre est une lettre
	 * 
	 * @param caractere
	 * @return boolean
	 */
	private boolean estLettre(String caractere) {
		if (caractere.matches("[A-Za-z_]")) {
			return true;
		}
		return false;
	}

	/**
	 * Retourne un nombre
	 * On lit tous les chiffres jusqu'à ce que ça n'en soit pas un
	 * ex. : 1234; => lit 1, puis 2, puis 3, puis 4 et s'arrête au ; 
	 * 
	 * @return Integer nb
	 */
	private int lireInt() {
		String nb = "";
		while (data.length > this.pos && estChiffre(data[this.pos])) {
			nb += data[this.pos];
			this.pos++;
		}
		return Integer.parseInt(nb);

	}

	/**
	 * Retourne une chaine de caractères qui correspond 
	 * au nom de la variable ou de la fonction
	 * On lit tous les caractères jusqu'à ce que ça n'en soit pas un
	 * ex. : toto; => lit t, puis o, puis t, puis o et s'arrête au ; 
	 * 
	 * @return String chaine
	 */
	private String lireIdent() {
		String chaine = "";
		while (data.length > this.pos && estLettre(data[this.pos])) {
			chaine += data[this.pos];
			this.pos++;
		}
		return chaine;
	}

	/**
	 * Retourne une chaine de caractères qui correspond 
	 * à la valeur d'une variable de type String
	 * On lit tous les caractères jusqu'à ce que ça n'en soit pas un
	 * ex. : toto" => lit t, puis o, puis t, puis o et s'arrête au " 
	 * 
	 * @return String chaine
	 */
	public String lireChaine() {
		String chaine = "";
		while (data.length > this.pos && !data[this.pos].equals("\"")) {
			chaine += data[this.pos];
			this.pos++;
		}
		return chaine;
	}

	/**
	 * Retourne la classe du token de la chaine passée en paramètre.
	 * Si ce n'est pas un mot clé c'est alors une variable ou une fonction : TOK_IDENT
	 * 
	 * @param chaine
	 * @return Classe motCle
	 */
	private Classe filtrerMotCle(String chaine) {
		Classe motCle = Classe.TOK_IDENT;

		switch (chaine.toLowerCase()) {
		case "if":
			motCle = Classe.TOK_IF;
			break;
		case "else":
			motCle = Classe.TOK_ELSE;
			break;

		case "for":
			motCle = Classe.TOK_FOR;
			break;

		case "while":
			motCle = Classe.TOK_WHILE;
			break;

		case "var":
			motCle = Classe.TOK_VAR;
			break;

		case "int":
			motCle = Classe.TOK_INT;
			break;
		}
		return motCle;
	}

	@Override
	public String toString() {
		return "Lexer \n [data=" + Arrays.toString(data) + "]";
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the data
	 */
	public String[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String[] data) {
		this.data = data;
	}

	/**
	 * @return the pos
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * @return the buffer
	 */
	public Token getBuffer() {
		return buffer;
	}

	/**
	 * @param buffer the buffer to set
	 */
	public void setBuffer(Token buffer) {
		this.buffer = buffer;
	}

}
