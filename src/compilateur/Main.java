package compilateur;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		try {
			if(args.length > 0) {
				String argumentNomFichier = args[0];
				boolean affichage = false;
				if(args.length > 1) {
					String argumentAffichage = args[1];
					if("oui".equalsIgnoreCase(argumentAffichage)) {
						affichage = true;							
					}
				}
				String filemane = argumentNomFichier.substring(1);
				System.out.println("Fichier à traiter : " + filemane);
				if(affichage) {
					System.out.println("Affichage de l'arbre : ");
					Lexer lexerAffichage = new Lexer(filemane);
					Parser parserAffichage = new Parser(lexerAffichage);
					parserAffichage.affichageParser();
				}
				System.out.println("Analyse du fichier en cours...");
				Lexer lexerGenerateur = new Lexer(filemane);
				Parser parserGenerateur = new Parser(lexerGenerateur);			
				new Generator(parserGenerateur);
				System.out.println("Le code a été généré dans le fichier ./files/out/code_generated.txt");
				
			}
			// TESTS POUR LE LEXER
//			Lexer lexer = new Lexer("./files/in/general.txt");
			// test_exp 
			// TODO : faire aussi le noeud sub avec add.i de - arbre.getNoeud().getIntValue()
//			Lexer lexer = new Lexer("./files/in/test_exp.txt");
//			Lexer lexer2 = new Lexer("./files/in/test_exp.txt");
			
			// test_var OK
//			Lexer lexer = new Lexer("./files/in/test_var.txt");
//			Lexer lexer2 = new Lexer("./files/in/test_var.txt");
			
			//test_multi_var OK
//			Lexer lexer = new Lexer("./files/in/test_multi_var.txt");
//			Lexer lexer2 = new Lexer("./files/in/test_multi_var.txt");
			
			// test condition OK
//			Lexer lexer = new Lexer("./files/in/test_if-else.txt");			
//			Lexer lexer2 = new Lexer("./files/in/test_if-else.txt");
			
			// test boucle OK
//			Lexer lexer = new Lexer("./files/in/test_for.txt");			
//			Lexer lexer2 = new Lexer("./files/in/test_for.txt");
			
			//test_function OK
//			Lexer lexer = new Lexer("./files/in/test_function.txt");			
//			Lexer lexer2 = new Lexer("./files/in/test_function.txt");

			
			// TESTS POUR LE PARSER
//			Parser parser = new Parser(lexer);
//			parser.affichageParser();
//			Parser parserG = new Parser(lexer2);			
//			Generator generator = new Generator(parserG);
			// A exécuter à la racine du projet
			//./MSM/msm -d -d ./files/out/code_generated.txt > ./files/out/out.txt
			
			
			// ant installation
			// sudo apt install ant
		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage() + " stacktrace : " + Arrays.toString(e.getStackTrace()));
		}

	}

}
