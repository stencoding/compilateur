

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * Generator (BACKEND : tout ce qui est lié au langage cible).
 * Il analyse l'arbre syntaxique et génère le langage cible.
 *
 */
public class Generator {

	private Parser parser;
	private BufferedWriter fichier;
			
	/**
	 * On récupère l'analyseur syntaxique
	 * 
	 * @param parser
	 * @throws Exception
	 */
	public Generator(Parser parser) throws Exception {
		this.parser = parser;

		try {
			this.fichier = new BufferedWriter(new FileWriter("./files/out/code_generated.txt"));

			generateCode(this.parser.racine());
			
			this.fichier.close();
			
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
				int cpt_nlabel = arbre.getNoeud().getIntValue();
				writeLineWithoutTab(".if_" + cpt_nlabel);
				
				// on génère la condition
				generateCode(arbre.getEnfants().get(0));
				writeLine("jumpt", "in_if_" + cpt_nlabel);
								
				if(arbre.getEnfants().size() > 2 && !arbre.getEnfants().get(2).getNoeud().getCategorie().equals(Categorie.BREAK)) {
					generateCode(arbre.getEnfants().get(2));
				}
				writeLine("jump", "end_if_" + cpt_nlabel);
				
				// génère le code si on entre dans le if
				writeLineWithoutTab(".in_if_" + cpt_nlabel);
				generateCode(arbre.getEnfants().get(1));
				
				// on boucle (sale, au plus vite sans trop réfléchir...)
				if(arbre.getEnfants().size() > 2 && arbre.getEnfants().get(2).getNoeud().getCategorie().equals(Categorie.BREAK)) {
					writeLine("jump", "if_" + cpt_nlabel);
				}
				
				// label de fin
				writeLineWithoutTab(".end_if_" + cpt_nlabel);
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
