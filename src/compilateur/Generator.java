package compilateur;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Generator {

	private Parser parser;
	private BufferedWriter fichier;

	/**
	 * On récupère l'analyseur syntaxique
	 * 
	 * @param parser
	 * @throws Exception
	 */
	
	// télécharger compilateur : http://perso.limsi.fr/lavergne
	public Generator(Parser parser) throws Exception {
		this.parser = parser;

		try {
			this.fichier = new BufferedWriter(new FileWriter("./files/out/code_generated.txt"));

//			this.fichier.write(".start");
			
			//this.parser.affichageParser();
			
//			generateExpression(this.parser.expression(), 0);
//			generateCode(this.parser.expression());
//			Arbre.affiche(this.parser.racine(), 0);

			generateCode(this.parser.racine());
			
			writeLine("halt");
			
			this.fichier.close();
			
//			Runtime runtime = Runtime.getRuntime();
			//./msm -d -d ../files/out/code_generated.txt 
//	        Process p =runtime.exec(new String[] { "./MSM/msm","../files/out/code_generated.txt"}); // la commande
//	        Thread.sleep(9000); // pause de 9 secondes
//	        p.destroy(); // détruire le processus
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void generateCode(Arbre arbre) throws IOException {
		switch (arbre.getNoeud().getCategorie()) {
			case RACINE:
				this.fichier.write(".start");
				writeLine("jump main");
				break;
			case FUNCTION:
				// on créé les cases mémoires pour toutes les variables du programme
				for(int i = 0; i < arbre.getNoeud().getIntValue() ; i++) {
					// TODO : voir si on doit initialiser à 0 ou à vide ???
					writeLine("push.i", i);
//					writeLine("push.i", arbre.getNoeud().getPosition());
				}
				// boucle sur chaque enfant sous la racine
				int nbEnfants = arbre.getEnfants().size();
				for(int i = 0; i < nbEnfants ; i++) {
					generateCode(arbre.getEnfants().get(i));
				}
				
				break;
			case VAR:
				generateCode(arbre.getEnfants().get(0));
				generateCode(arbre.getEnfants().get(1));
				break;
			case EGAL:
				generateCode(arbre.getEnfants().get(0));
				generateCode(arbre.getEnfants().get(1));
				// TODO : à finir en récupérant sa position
//				writeLine("set", arbre.getNoeud().getPosition());
				break;
			case IDENT:
				writeLine("get", arbre.getNoeud().getPosition());
				break;
			case CST_INT:
				writeLine("push.i", arbre.getNoeud().getIntValue());
				break;
			case ADD:
				generateCode(arbre.getEnfants().get(0));
				generateCode(arbre.getEnfants().get(1));
				writeLine("add.i");
				break;
			case MUL:
				generateCode(arbre.getEnfants().get(0));
				generateCode(arbre.getEnfants().get(1));
				writeLine("mul.i");
				break;
	
			default:
				break;
		}
	}
	
	/**
	 * DEPRECATED
	 * 
	 * @param arbre
	 * @param level
	 * @throws IOException
	 */
	public void generateExpression(Arbre arbre, int level) throws IOException {
		if (arbre == null) {
			return;
		}
		
		if (arbre.getEnfants() == null) {
			if (arbre.getNoeud().getCategorie() == Categorie.CST_INT) {
				writeLine("push.i", arbre.getNoeud().getIntValue());
			}
			return;
		}

		for (Arbre enfant : arbre.getEnfants()) {
			generateExpression(enfant, level + 1);
		}
		
		if (arbre.getNoeud().getCategorie() == Categorie.ADD){
			writeLine("add.i");
			return;
		}
		
		if (arbre.getNoeud().getCategorie() == Categorie.MUL){
			writeLine("mul.i");
			return;
		}
	}
		
	public void writeLine(String instruction) throws IOException {
		writeLine(instruction, "");
	}

	public void writeLine(String instruction, int value) throws IOException {
		this.fichier.newLine();
		this.fichier.write("\t" + instruction + " " + value);
	}
	
	public void writeLine(String instruction, String value) throws IOException {
		this.fichier.newLine();
		this.fichier.write("\t" + instruction + " " + value);
	}

}
