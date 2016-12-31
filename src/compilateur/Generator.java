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

			generateCode(this.parser.racine());
			
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
				
				// on crée les cases mémoires pour toutes les variables du programme
				if(arbre.getNoeud().getIntValue() - arbre.getEnfants().get(2).getNoeud().getIntValue() > 0) {
					writeLine("; declaration variable");
					for(int i = 0; i < arbre.getNoeud().getIntValue() - arbre.getEnfants().get(2).getNoeud().getIntValue() ; i++) {	
						writeLine("push.i", 0);
					}
				}
				
				generateCode(arbre.getEnfants().get(3));
				// Si la fonction n'est pas le main, on fait le return
				if(!arbre.getEnfants().get(1).getNoeud().getStrValue().equalsIgnoreCase("main")) {
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
				// un seul enfant
				generateCode(arbre.getEnfants().get(0));
				break;
				
			case ECHO:
				writeLine("get", arbre.getNoeud().getPosition(), "affichage de " + arbre.getNoeud().getStrValue());
				writeLine("out.i", "; affichage de " + arbre.getNoeud().getStrValue());
				break;
				
			case CALL:
				writeLine("prep", arbre.getEnfants().get(0).getNoeud().getStrValue());
				for(int i = 0 ; i < arbre.getNoeud().getIntValue() ; i++) {
					generateCode(arbre.getEnfants().get(i+1));
				}
				writeLine("call", arbre.getNoeud().getIntValue());
				break;

			case IF:
				writeLineWithoutTab(".if_" + arbre.getEnfants().get(0).getNoeud().getIntValue());
				
				// on génère la condition
				generateCode(arbre.getEnfants().get(0));
				writeLine("jumpt", "in_if_" + arbre.getEnfants().get(0).getNoeud().getIntValue());
								
				if(arbre.getEnfants().size() > 2 && !arbre.getEnfants().get(2).getNoeud().getCategorie().equals(Categorie.BREAK)) {
					generateCode(arbre.getEnfants().get(2));
				}
				writeLine("jump", "end_if_" + arbre.getEnfants().get(0).getNoeud().getIntValue());
				
				// génère le code si on entre dans le if
				writeLineWithoutTab(".in_if_" + arbre.getEnfants().get(0).getNoeud().getIntValue());
				generateCode(arbre.getEnfants().get(1));
				
				// on boucle (sale, au plus vite sans trop réfléchir...)
				if(arbre.getEnfants().size() > 2 && arbre.getEnfants().get(2).getNoeud().getCategorie().equals(Categorie.BREAK)) {
					writeLine("jump", "if_" + arbre.getEnfants().get(0).getNoeud().getIntValue());
				}
				
				// label de fin
				writeLineWithoutTab(".end_if_" + arbre.getEnfants().get(0).getNoeud().getIntValue());
				break;
			
			case COMPARE:
				generateCodeForTwoChildren(arbre, "get");
				writeLine("cmpeq.i");
				break;
				
			case DIFF:
				generateCodeForTwoChildren(arbre, "get");
				writeLine("cmpne.i");
				break;
				
			case INF:
				generateCodeForTwoChildren(arbre, "get");
				writeLine("cmplt.i");
				break;
				
			case INF_EGAL:
				generateCodeForTwoChildren(arbre, "get");
				writeLine("cmple.i");
				break;
				
			case SUP:
				generateCodeForTwoChildren(arbre, "get");
				writeLine("cmpgt.i");
				break;
				
			case SUP_EGAL:
				generateCodeForTwoChildren(arbre, "get");
				writeLine("cmpge.i");
				break;
				
			case EGAL:
				if(arbre.getEnfants().get(0).getNoeud().getCategorie() != Categorie.IDENT) {
					generateCode(arbre.getEnfants().get(0));
				}
				if(arbre.getEnfants().get(1).getNoeud().getCategorie() != Categorie.IDENT) {
					generateCode(arbre.getEnfants().get(1));
				}

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
			
			case SUB:
				generateCode(arbre.getEnfants().get(0));
				generateCode(arbre.getEnfants().get(1));
				writeLine("sub.i");
				break;
				
			case MUL:
				generateCode(arbre.getEnfants().get(0));
				generateCode(arbre.getEnfants().get(1));
				writeLine("mul.i");
				break;
			
			case DIV:
				generateCode(arbre.getEnfants().get(0));
				generateCode(arbre.getEnfants().get(1));
				writeLine("div.i");
				break;
			
			case MODULO:
				generateCode(arbre.getEnfants().get(0));
				generateCode(arbre.getEnfants().get(1));
				writeLine("mod.i");
				break;
	
			default:
				break;
		}
	}
	
	// TODO : plus propre si on a le tps
	public void generateCodeForTwoChildren(Arbre arbre, String instruction) throws IOException{
		if(arbre.getEnfants().get(0).getNoeud().getCategorie() != Categorie.IDENT) {
			generateCode(arbre.getEnfants().get(0));
		} else {
			writeLine(instruction, arbre.getEnfants().get(0).getNoeud().getPosition(), arbre.getEnfants().get(0).getNoeud().getStrValue());
		}
		if(arbre.getEnfants().get(1).getNoeud().getCategorie() != Categorie.IDENT) {
			generateCode(arbre.getEnfants().get(1));
		} else {
			writeLine(instruction, arbre.getEnfants().get(1).getNoeud().getPosition(), arbre.getEnfants().get(1).getNoeud().getStrValue());
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
