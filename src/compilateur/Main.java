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
			// test_exp OK
//			Lexer lexer = new Lexer("./files/in/test_exp.txt");
//			Lexer lexer2 = new Lexer("./files/in/test_exp.txt");
			
			// test_var OK
//			Lexer lexer = new Lexer("./files/in/test_var.txt");
//			Lexer lexer2 = new Lexer("./files/in/test_var.txt");
			
			//test_multi_var OK
//			Lexer lexer = new Lexer("./files/in/test_multi_var.txt");
//			Lexer lexer2 = new Lexer("./files/in/test_multi_var.txt");
			
//			Lexer lexer = new Lexer("./files/in/test_for.txt");

			//test_function OK - A REVOIR
			Lexer lexer = new Lexer("./files/in/test_function.txt");			
			Lexer lexer2 = new Lexer("./files/in/test_function.txt");

			
			// TESTS POUR LE PARSER
			Parser parser = new Parser(lexer);
			parser.affichageParser();
			Parser parserG = new Parser(lexer2);			
			Generator generator = new Generator(parserG);
			// A exécuter à la racine du projet
			//./MSM/msm -d -d ./files/out/code_generated.txt
			// QUESTIONS :
			// PARSER :
			//   - voir pb noté dans TODO
			
			
			//--------- VOIR problème gestion des variables dans la génération du code !!!!!!!
			
			// Finir la gestion des fonctions
			// Finir génération du code : variable, boucle, fonction
			
			// POUR RENDUUUUUU
			// à rendre pour le 4 janvier 23h59
			// par mail => archive contenant tt le code du projet + rapport au format pdf
			// archive tgz ou zip
			// fichier texte README.txt => pour lancer le compilateur, comment le lancer même via l'ide
			// ou lancement en ligne de commande
			// expliquer à quoi servent les fichiers => analyseur syntaxique, ...
			// A METTRE DANS LE RAPPORT PDF:
			// petite page de présentation (gros paragraphe) du compilateur
			// puis doc du langage que l'on va compiler
			// mettre une grammaire exacte => sémantique avec texte autour, genre bouquin de prog
			//     permet d'écrire des programmes dans le langage
			// bilan du compilateur : qu'est-ce qui marche ou pas
			//     voici un prog d'exemple (boucle, tableau...) qui marche
			//     qu'est-ce qui nous plaît dans notre compilateur ou pas
			// envoyer via un mail ou réseau de la fac => lavergne@limsi.fr
			// envoyer via la webmail (zimbra) => si mail envoyé et perdu il peut voir les logs
			//      sujet du mail => compilation
			// mettre l'autre du binôme en copie
			
			//TODO:
			// finir genération du code (fonction, boucle...)
			
			// SI ON A LE TPS : 
			//       +=,-=, ...
			//       tableaux
			// nettoyer code

		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage() + " stacktrace : " + Arrays.toString(e.getStackTrace()));
		}

	}

}
