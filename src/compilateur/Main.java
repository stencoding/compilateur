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
//			Lexer lexer = new Lexer("./files/in/test_var.txt");
//			Lexer lexer = new Lexer("./files/in/test_for.txt");
			Lexer lexer = new Lexer("./files/in/test_function.txt");
			
			// TESTS POUR LE PARSER
			Parser parser = new Parser(lexer);
			parser.affichageParser();
//			Generator generator = new Generator(parser);
			//./msm -d -d ../files/out/code_generated.txt
			// QUESTIONS :
			//    on n'appel jamais primaire dans instruction et donc jamais la fonction dans primaire
			// PARSER :
			//   - voir pb noté dans TODO
			

		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage() + " stacktrace : " + Arrays.toString(e.getStackTrace()));
		}

	}

}
