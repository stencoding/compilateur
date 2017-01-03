import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Arrays;

/**
 * 
 * Analyseur lexical (FRONTEND : tout ce qui est lié au langage source)
 * Il lit le code à compiler (fichier texte) et le transforme en Token
 *
 * @author Mathilde PREVOST & Steve NEGRINE
 */

public class Lexer {

	/**
     * Le nom du fichier à analyser.
     */	
	private String filename;
	
	/**
     * Tableau contenant le fichier à analyser.
     */	
	private String[] data;
	
	/**
     * Le Token suivant.
     * 
     * @see Token
     */	
	private Token buffer;
	
	/**
     * La position dans le tableau "data"
     */
	private int pos;

	
	/**
	 * Constructeur Lexer.
	 * La position est initialisé à 0 (on se met au début du tableau "data").
	 * Le tableau "data" contient le fichier à analyser spliter mot par mot.
	 * Le buffer est initialisé avec le prochain Token du tableau "data".
	 * 
	 * @param filename
	 *            Le nom du fichier à analyser
	 * @throws Exception
	 */
	public Lexer(String filename) throws Exception {
		this.filename = filename;
		this.pos = 0;
		this.data = lectureFile().split("");
		this.buffer = prochain();
	}

	/**
	 * Lecture du fichier "filename"
	 * 
	 * @return Le contenu du fichier à analyser dans une chaine
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
	 * Prend le prochain élément de la chaîne, crée le token et avance dans la chaîne.
	 * 
	 * @return Le token crée
	 * @throws Exception 
	 */
	public Token prochain() throws Exception {
		Token token = new Token();

		if (data.length == this.pos+1) {
			token.setClasse(Classe.TOK_EOF);
			return token;
		}
		
		while (estEspace(data[this.pos])
			|| estSautDeLigne(data[this.pos])
			|| estIndentation(data[this.pos])
		) {
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
			token.setClasse(getToken(strTmp));
			
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

		case "!":
			if (data[++this.pos].equals("=")) {
				token.setClasse(Classe.TOK_DIFF);
				break;
			}

		case "=":
			if (data[++this.pos].equals("=")) {
				token.setClasse(Classe.TOK_COMPARE);
				break;
			}

			this.pos--;
			token.setClasse(Classe.TOK_EGAL);
			break;

		case ">":
			if (data[++this.pos].equals("=")) {
				token.setClasse(Classe.TOK_SUP_EGAL);
				break;
			}

			this.pos--;
			token.setClasse(Classe.TOK_SUP);
			break;

		case "<":
			if (data[++this.pos].equals("=")) {
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
	 * Retourne le token courant et passe au suivant.
	 * 
	 * @return Le token suivant
	 * @throws Exception 
	 */
	public Token next() throws Exception {
		Token tokenCourant = this.buffer;
		this.buffer = prochain();		
		return tokenCourant;
	}

	/**
	 * Retourne le token suivant sans avancer.
	 * 
	 * @return Le token suivant
	 */
	public Token look() {
		return this.buffer;
	}

	/**
	 * Si le caractère en paramètre est un espace.
	 * 
	 * @param caractere
     *            Le caractère à évaluer.
	 * @return boolean
	 */
	private boolean estEspace(String caractere) {
		if (caractere.equals(" ")) {
			return true;
		}
		return false;
	}

	/**
	 * Si le caractère en paramètre est un saut de ligne.
	 * 
	 * @param caractere
     *            Le caractère à évaluer.
	 * @return boolean
	 */
	private boolean estSautDeLigne(String caractere) {
		if (caractere.equals("\n") || caractere.equals("\r\n")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Si le caractère en paramètre est une indentation.
	 * 
	 * @param caractere
     *            Le caractère à évaluer.
	 * @return boolean
	 */
	private boolean estIndentation(String caractere) {
		if (caractere.equals("\t")) {
			return true;
		}
		return false;
	}

	/**
	 * Si le caractère en paramètre est un guillemet.
	 * 
	 * @param caractere
     *            Le caractère à évaluer.
	 * @return boolean
	 */
	private boolean estGuillemet(String caractere) {
		if (caractere.equals("\"")) {
			return true;
		}
		return false;
	}

	/**
	 * Si le caractère en paramètre est un chiffre.
	 * 
	 * @param caractere
     *            Le caractère à évaluer.
	 * @return boolean
	 */
	private boolean estChiffre(String caractere) {
		if (caractere.matches("[0-9]")) {
			return true;
		}
		return false;
	}

	/**
	 * Si le caractère en paramètre est une lettre.
	 * 
	 * @param caractere
     *            Le caractère à évaluer.
	 * @return boolean
	 */
	private boolean estLettre(String caractere) {
		if (caractere.matches("[A-Za-z_]")) {
			return true;
		}
		return false;
	}

	/**
	 * Lit un nombre
	 * On lit tous les chiffres jusqu'à ce que le suivant ne soit pas un chiffre
	 * exemple : 1234; => on lit 1, puis 2, puis 3, puis 4 et on s'arrête au ; 
	 * 
	 * @return Le nombre entier
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
	 * Lit une chaine de caractères qui correspond soit à un nom de variable soit à un nom de fonction
	 * On lit tous les caractères jusqu'à ce que le suivant ne soit pas une lettre
	 * exemple : toto; => on lit t, puis o, puis t, puis o et on s'arrête au ; 
	 * 
	 * @return La chaine de caractère entière.
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
	 * Lit une chaine de caractères qui correspond à la valeur d'une variable de type String
	 * On lit tous les caractères jusqu'à ce que le suivant ne soit pas une lettre
	 * exemple : toto" => on lit t, puis o, puis t, puis o et on s'arrête au " 
	 * 
	 * @return La chaine de caractère entière.
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
	 * Crée un token en fonction de la chaine passée en paramètre.
	 * Si la chaine n'est pas un mot clé, alors c'est un nom de variable ou de fonction
	 * donc la classe est "TOK_IDENT"
	 * 
	 * @param chaine
     *            La chaine à convertir en Token.
	 * @return La classe de la chaine passée en paramètre.
	 */
	private Classe getToken(String chaine) {
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
				
			case "return":
				motCle = Classe.TOK_RETURN;
				break;
				
			case "echo":
				motCle = Classe.TOK_ECHO;
				break;
		}
		return motCle;
	}

	/**
	 * Affiche le lexer
	 */
	public String toString() {
		return "Lexer \n [data=" + Arrays.toString(data) + "]";
	}
}