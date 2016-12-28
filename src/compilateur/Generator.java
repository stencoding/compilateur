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
//			
//			writeLine("halt");
			
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
				int nbEnfants = arbre.getEnfants().size();
				this.fichier.write(".start");
				if(nbEnfants > 0) {
					generateCode(arbre.getEnfants().get(0));
				}
				writeLine("halt");
				
				// on commence à partir du deuxième enfant : première fonction après le main
				for(int i = 1; i < nbEnfants ; i++) {
					generateCode(arbre.getEnfants().get(i));
				}
				// boucle sur chaque enfant sous la racine
				break;
				
			case FUNCTION:
				if(!arbre.getEnfants().get(1).getNoeud().getStrValue().equalsIgnoreCase("main")) {
					writeLineWithoutTab("." + arbre.getEnfants().get(1).getNoeud().getStrValue());					
				}
								
				// on créé les cases mémoires pour toutes les variables du programme
				if(arbre.getNoeud().getIntValue() - arbre.getEnfants().get(2).getNoeud().getIntValue() > 0) {
					writeLine("; declaration variable");
					for(int i = 0; i < arbre.getNoeud().getIntValue() - arbre.getEnfants().get(2).getNoeud().getIntValue() ; i++) {	
						writeLine("push.i", 0);
					}
				}
				
				generateCode(arbre.getEnfants().get(3));
				if(arbre.getEnfants().get(1).getNoeud().getStrValue().equalsIgnoreCase("main")) {
					writeLine("ret");
				}
				else {
					// ret dans toutes les fonctions sauf le main car on oblige l'utilisateur à mettre un return
					writeLine("ret");
					writeLine("; return par defaut");
					writeLine("push.i", 0);
					writeLine("ret");
				}
				break;
				
			case BLOCK:
				// boucle sur chaque enfant sous block
				for(int i = 0; i < arbre.getEnfants().size() ; i++) {
					generateCode(arbre.getEnfants().get(i));
				}
				break;
				
			case SEQ:
				generateCode(arbre.getEnfants().get(0));
				generateCode(arbre.getEnfants().get(1));
				break;
				
			case LOOP:
				generateCode(arbre.getEnfants().get(0));
				generateCode(arbre.getEnfants().get(1));
				break;
				
			case ECHO:
				writeLine("get", arbre.getNoeud().getPosition());
				writeLine("out.i");
				break;
				
			case CALL:
				writeLine("prep", arbre.getEnfants().get(0).getNoeud().getStrValue());
				for(int i = 0 ; i < arbre.getNoeud().getIntValue() ; i++) {
					generateCode(arbre.getEnfants().get(i+1));
				}
				writeLine("call", arbre.getNoeud().getIntValue());
				break;
			
			case IF:
				// on génère le code de la condition
				if(arbre.getEnfants().get(0).getNoeud().getCategorie() != Categorie.IDENT) {// peut-être inutile
					generateCode(arbre.getEnfants().get(0));
					writeLine("jumpf", "else_" + arbre.getEnfants().get(0).getNoeud().getIntValue());
				}
				// génère le code du if
				if(arbre.getEnfants().get(1).getNoeud().getCategorie() != Categorie.IDENT) {// peut-être inutile
					generateCode(arbre.getEnfants().get(1));
					writeLine("jump", "end_if_" + arbre.getEnfants().get(0).getNoeud().getIntValue());
				}
				// génère le code du else
				writeLineWithoutTab(".else_" + arbre.getEnfants().get(0).getNoeud().getIntValue());
				if(arbre.getEnfants().size() > 2 && arbre.getEnfants().get(2).getNoeud().getCategorie() != Categorie.IDENT) {// peut-être inutile
					generateCode(arbre.getEnfants().get(2));
				}
				
				// label de fin
				writeLineWithoutTab(".end_if_" + arbre.getEnfants().get(0).getNoeud().getIntValue());
				
				break;
			
			case COMPARE:
				generateCodeForTwoChildren(arbre);
				writeLine("cmpeq.i");
				break;
				
			case DIFF:
				generateCodeForTwoChildren(arbre);
				writeLine("cmpne.i");
				break;
				
			case INF:
				generateCodeForTwoChildren(arbre);
				writeLine("cmplt.i");
				break;
				
			case INF_EGAL:
				generateCodeForTwoChildren(arbre);
				writeLine("cmple.i");
				break;
				
			case SUP:
				generateCodeForTwoChildren(arbre);
				writeLine("cmpgt.i");
				break;
				
			case SUP_EGAL:
				generateCodeForTwoChildren(arbre);
				writeLine("cmpge.i");
				break;
				
			case EGAL:
				generateCodeForTwoChildren(arbre);
				writeLine("set", arbre.getEnfants().get(0).getNoeud().getPosition(), arbre.getEnfants().get(0).getNoeud().getStrValue());
				break;
				
			case IDENT:
				writeLine("get", arbre.getNoeud().getPosition(), arbre.getNoeud().getStrValue());
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
	
	public void generateCodeForTwoChildren(Arbre arbre) throws IOException {
		if(arbre.getEnfants().get(0).getNoeud().getCategorie() != Categorie.IDENT) {
			generateCode(arbre.getEnfants().get(0));
		}
		if(arbre.getEnfants().get(1).getNoeud().getCategorie() != Categorie.IDENT) {
			generateCode(arbre.getEnfants().get(1));
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
	
	public void writeLine(String instruction, String value, String commentaire) throws IOException {
		this.fichier.newLine();
		this.fichier.write("\t" + instruction + " " + value + " ; " + commentaire);
	}

	public void writeLine(String instruction, int value, String commentaire) throws IOException {
		this.fichier.newLine();
		this.fichier.write("\t" + instruction + " " + value + " ; " + commentaire);
	}
	
	public void writeLineWithoutTab(String label) throws IOException {
		this.fichier.newLine();
		this.fichier.write(label);
		
	}

}
