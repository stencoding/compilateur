package compilateur;

import java.util.Arrays;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {

			// TESTS POUR LE LEXER
//			Lexer lexer = new Lexer("./files/in/general.txt");
			Lexer lexer = new Lexer("./files/in/test_exp.txt");
			
			// TESTS POUR LE PARSER
			Parser parser = new Parser(lexer);
			//arser.affichageParser();
			Generator generator = new Generator(parser);
			//./msm -d -d ../files/out/code_generated.txt 
			// TODO: 
			// Parser :
			// Arbre :
			//   finir l'affichage en Json
			// Gérénale:
			//   factoriser + nettoyer + doc
			

		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage() + " stacktrace : " + Arrays.toString(e.getStackTrace()));
		}

	}

}
