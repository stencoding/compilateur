import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * Generator (BACKEND : tout ce qui est lié au langage cible).
 * Il analyse l'arbre syntaxique et génère le langage cible.
 * 
 * @author Mathilde PREVOST & Steve NEGRINE
 */
public class Generator {
	
	/**
     * Le parser du Generator
     * 
     * @see Parser
     */	
	private Parser parser;
	
	/**
     * Le fichier généré par le Generator
     * 
     */	
	private BufferedWriter fichier;
			
	/**
	 * Constructeur Generator.
	 * 
	 * @param parser
     *            Le parser (l'analyseur syntaxique) du Generator.
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
	
	/**
	 * Génère le code.
	 * Fonction récursive
	 * 
	 * @param arbre
     *            L'arbre à générer.
	 * @throws IOException
	 */
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
	
	/**
	 * Génère le code pour les arbres des opérateurs de comparaisons.
	 * Fonction récursive
	 * 
	 * @param arbre
     *            L'arbre à générer.
	 * @param instruction
     *            L'instruction à écrire dans le fichier.
	 * @throws IOException
	 */
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
	
	/**
	 * Ecrit dans le fichier l'instruction
	 * 
	 * @param instruction
     *            L'instruction à écrire dans le fichier
	 * @throws IOException
	 */
	public void writeLine(String instruction) throws IOException {
		writeLine(instruction, "");
	}

	/**
	 * Ecrit dans le fichier l'instruction et sa valeur
	 * 
	 * @param instruction
     *            L'instruction à écrire dans le fichier
	 * @param value
     *            La valeur de l'instruction à écrire dans le fichier
	 * @throws IOException
	 */
	public void writeLine(String instruction, int value) throws IOException {
		this.fichier.newLine();
		this.fichier.write("\t" + instruction + " " + value);
	}
	
	/**
	 * Ecrit dans le fichier l'instruction et sa valeur
	 * 
	 * @param instruction
     *            L'instruction à écrire dans le fichier
	 * @param value
     *            La valeur de l'instruction à écrire dans le fichier
	 * @throws IOException
	 */
	public void writeLine(String instruction, String value) throws IOException {
		this.fichier.newLine();
		this.fichier.write("\t" + instruction + " " + value);
	}
	
	/**
	 * Ecrit dans le fichier l'instruction, sa valeur et un commentaire
	 * 
	 * @param instruction
     *            L'instruction à écrire dans le fichier
	 * @param value
     *            La valeur de l'instruction à écrire dans le fichier
	 * @param commentaire
     *            Le commentaire de l'instruction à écrire dans le fichier
	 * @throws IOException
	 */
	public void writeLine(String instruction, String value, String commentaire) throws IOException {
		this.fichier.newLine();
		this.fichier.write("\t" + instruction + " " + value + " ; " + commentaire);
	}

	/**
	 * Ecrit dans le fichier l'instruction, sa valeur et un commentaire
	 * 
	 * @param instruction
     *            L'instruction à écrire dans le fichier
	 * @param value
     *            La valeur de l'instruction à écrire dans le fichier
	 * @param commentaire
     *            Le commentaire de l'instruction à écrire dans le fichier
	 * @throws IOException
	 */
	public void writeLine(String instruction, int value, String commentaire) throws IOException {
		this.fichier.newLine();
		this.fichier.write("\t" + instruction + " " + value + " ; " + commentaire);
	}

	/**
	 * Ecrit dans le fichier le label de la fonction, sans indentation
	 * 
	 * @param label
     *            Le label de la fonction
	 * @throws IOException
	 */
	public void writeLineWithoutTab(String label) throws IOException {
		this.fichier.newLine();
		this.fichier.write(label);
	}
}