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

			this.fichier.write(".start");
			
			//this.parser.affichageParser();
			
			generateExpression(this.parser.expression(), 0);
			
			writeLine("halt");
			
			this.fichier.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void generateExpression(Arbre arbre, int level) throws IOException {
		if (arbre == null) {
			return;
		}
		
		if (arbre.getEnfants() == null) {
			if (arbre.getNoeud().getClasse() == Classe.TOK_CST_INT) {
				writeLine("push.i", arbre.getNoeud().getChargeInt());
			}
			return;
		}

		for (Arbre enfant : arbre.getEnfants()) {
			generateExpression(enfant, level + 1);
		}
		
		if (arbre.getNoeud().getClasse() == Classe.TOK_ADD){
			writeLine("add.i");
			return;
		}
		
		if (arbre.getNoeud().getClasse() == Classe.TOK_MUL){
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
