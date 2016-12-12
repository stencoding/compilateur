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
//			Lexer lexer = new Lexer("./files/in/test_exp.txt");
			Lexer lexer = new Lexer("./files/in/test_var.txt");
			
			// TESTS POUR LE PARSER
			Parser parser = new Parser(lexer);
//			parser.affichageParser();
			Generator generator = new Generator(parser);
			//./msm -d -d ../files/out/code_generated.txt
			// QUESTIONS :
			//   - comment on gère la récupération des variables ... position dans chaque ident de l'arbre ?
			//   - que faisons-nous des symboles créé
			// PARSER :
			//   - modifier le for pour gérer l'affectation
			//   - ajouter l'affectation
			//   - voir pb noté dans TODO
			

		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage() + " stacktrace : " + Arrays.toString(e.getStackTrace()));
		}

	}

}
