package compilateur;

import java.util.Arrays;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {

			// TESTS POUR LE LEXER
			
//			Lexer lexer = new Lexer("./files/prog.txt");
//			Lexer lexer = new Lexer("./files/prog2.txt");
//			Lexer lexer = new Lexer("./files/test_for.txt");
			Lexer lexer = new Lexer("./files/test_while.txt");
			/*
			while (lexer.prochain().getClasse() != Classe.TOK_EOF) {
				System.out.println(lexer.prochain());
			}

			for (int i = 0 ; i<10 ; i++) {
				System.out.println(lexer.prochain());
			}			 
			*/
			
			
			// TESTS POUR LA TABLE DES SYMBOLES
			/*
			TableDeSymbole tableDeSymbole = new TableDeSymbole();
						
			// ajout d'une variable x à la table de symbole du bloc global
			tableDeSymbole.definir("x");
			
			// ajout d'une variable y à la table de symbole du bloc global
			tableDeSymbole.definir("y");
			
			tableDeSymbole.definir("z");
			// on tente d'ajouter une variable x à la table de symbole du bloc global
			// cela doit générer une erreur ("Variable déjà présente")
			//tableDeSymbole.definir("x");
			
			// affiche le symbole x car existe
			//System.out.println(tableDeSymbole.chercher("x"));
			
			// affiche une erreur car n'existe pas ("Identificateur inconnu")
			//System.out.println(tableDeSymbole.chercher("z"));
			
			// création d'un nouveau bloc
			tableDeSymbole.push();
			
			// ajout d'une variable x à la table de symbole du 2ème bloc 1er fils du bloc global
			tableDeSymbole.definir("x");
			
			 
			// affiche z
			System.out.println(tableDeSymbole.chercher("z"));
			
			//  affiche une erreur car n'existe pas ("Identificateur inconnu")
			//System.out.println(tableDeSymbole.chercher("w"));
			
			//tableDeSymbole.pop();
			
			// affichage de la table
			//System.out.println(tableDeSymbole);
			
			*/
			
			// TESTS POUR LE PARSER
			Parser parser = new Parser(lexer);
			//Arbre.affiche(parser.expression());
			//System.out.println(parser.expression());
			
			
			
			
			// TODO: 
			// Parser :
			// 			finir instruction()
			//          savoi si on crée un TOK_BLOCK pour if, else, while, for : attente réponse du prof
			
			

		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage() + " stacktrace : " + Arrays.toString(e.getStackTrace()));
		}

	}

}
