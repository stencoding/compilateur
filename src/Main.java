

import java.io.File;
import java.util.Arrays;

/**
 * 
 * Classe principale du compilateur.
 *
 */
public class Main {

	public static void main(String[] args) {
		if(args.length > 0) {
			String filename = args[0];
			boolean affichage = false;
			if(args.length > 1) {
				String argumentAffichage = args[1];
				if("oui".equalsIgnoreCase(argumentAffichage)) {
				affichage = true;							
				}
			}
			File file = new File(filename);
			try {
				if(file.isFile()) {
					System.out.println("Fichier à traiter : " + filename);
					if(affichage) {
						System.out.println("Affichage de l'arbre : ");
						Lexer lexerAffichage = new Lexer(filename);
						Parser parserAffichage = new Parser(lexerAffichage);
						parserAffichage.affichageParser();
					}
					System.out.println("Analyse du fichier en cours...");
					Lexer lexerGenerateur = new Lexer(filename);
					Parser parserGenerateur = new Parser(lexerGenerateur);			
					new Generator(parserGenerateur);
					System.out.println("Le code a été généré dans le fichier ./files/out/code_generated.txt");	
				}
				else {
					System.out.println("Le fichier saisi ["+filename +"] n'existe pas");
				}
			} catch (Exception e) {
				System.out.println("Erreur : "+ e.getMessage() + " stacktrace : " + Arrays.toString(e.getStackTrace()));
			}
		}
	}
}