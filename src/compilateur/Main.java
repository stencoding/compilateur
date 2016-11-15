package compilateur;

import java.util.Arrays;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {

			// TESTS POUR LE LEXER
			Lexer lexer = new Lexer("./files/generale.txt");
			
			// TESTS POUR LE PARSER
			Parser parser = new Parser(lexer);
			
			// TODO: 
			// Parser :
			//   poser la question pour le for au prof ...
			// Arbre :
			//   finir l'affichage en Json
			// Gérénale:
			//   factoriser + nettoyer + doc
			

		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage() + " stacktrace : " + Arrays.toString(e.getStackTrace()));
		}

	}

}
