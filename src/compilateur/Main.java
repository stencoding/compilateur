package compilateur;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		try {

			// TESTS POUR LE LEXER
//			Lexer lexer = new Lexer("./files/in/general.txt");
			// test_exp OK
//			Lexer lexer = new Lexer("./files/in/test_exp.txt");
//			Lexer lexer2 = new Lexer("./files/in/test_exp.txt");
			
			// test_var OK
//			Lexer lexer = new Lexer("./files/in/test_var.txt");
//			Lexer lexer2 = new Lexer("./files/in/test_var.txt");
			
			//test_multi_var OK
//			Lexer lexer = new Lexer("./files/in/test_multi_var.txt");
//			Lexer lexer2 = new Lexer("./files/in/test_multi_var.txt");
			
			// test condition :
			// OK pour le if
			// reste while à faire
			// faire aussi le noeud sub avec add.i de - arbre.getNoeud().getIntValue()
			Lexer lexer = new Lexer("./files/in/test_if-else.txt");			
			Lexer lexer2 = new Lexer("./files/in/test_if-else.txt");
			
			// test boucle TODO
//			Lexer lexer = new Lexer("./files/in/test_for.txt");			
//			Lexer lexer2 = new Lexer("./files/in/test_for.txt");

			//test_function OK
//			Lexer lexer = new Lexer("./files/in/test_function.txt");			
//			Lexer lexer2 = new Lexer("./files/in/test_function.txt");

			
			// TESTS POUR LE PARSER
			Parser parser = new Parser(lexer);
			parser.affichageParser();
			Parser parserG = new Parser(lexer2);			
			Generator generator = new Generator(parserG);
			// A exécuter à la racine du projet
			//./MSM/msm -d -d ./files/out/code_generated.txt
			
			
		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage() + " stacktrace : " + Arrays.toString(e.getStackTrace()));
		}

	}

}
