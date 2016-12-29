package compilateur;

import java.util.ArrayList;

/**
 * 
 * Analyseur syntaxique (FRONTEND)
 * 
 * Priorité au plus haut niveau : primaire->multiplicatif->additif->expression
 * 
 * Niveau primaire : P <- ident | cst_int | (E) | -P Niveau multiplicatif : M <-
 * P (* M | / M | % M | epsilon) Niveau additif : A <- M (+ M | - M | epsilon)
 * Niveau expression : E <- A
 * 
 */
public class Parser {

	private int nvar;
	private int narg;
	private int nlabel;
	private boolean returnOk;
	private Lexer lexer;
	private TableDeSymbole tableSymbole;

	/**
	 * On récupère l'analyseur lexical
	 * 
	 * @param lexer
	 * @throws Exception
	 */
	public Parser(Lexer lexer) throws Exception {
		this.lexer = lexer;
		this.returnOk = false;
	}

	public void affichageParser() throws Exception {
		Arbre.affiche(racine(), 0);
	}

	/**
	 * Instruction
	 * I <- var Ident : TYPE;
	 * | Affectation 
	 * | if (E) I (else I |epsilon) 
	 * | while (E) I 
	 * | {Seq I} 
	 * | for (Ident = Aff;E;Aff) I
	 * | return E ";"
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre instruction() throws Exception {
		
		// var Ident : TYPE;
		if (lexer.look().getClasse() == Classe.TOK_VAR) {

			Token tok = lexer.next(); // sur var
			Noeud op = Noeud.tokenToNode(tok);

			if (lexer.look().getClasse() != Classe.TOK_IDENT) {
				throw new Exception("VAR non suivie d'un IDENT");
			}

			Token tokIdent = lexer.next(); // sur ident
			Noeud opIdent = Noeud.tokenToNode(tokIdent);
			opIdent.setPosition(this.nvar);
			Arbre a1 = new Arbre(opIdent, null);

			if (lexer.look().getClasse() != Classe.TOK_DEUX_POINTS) {
				throw new Exception("IDENT non suivi d'un DEUX_POINTS");
			}

			lexer.next(); // lexer positionné sur DEUX_POINTS

			if (lexer.look().getClasse() != Classe.TOK_INT && lexer.look().getClasse() != Classe.TOK_STR) {
				throw new Exception(
						"DEUX_POINTS doit être suivi d'un INT ou d'un STR (<" + lexer.look().getClasse() + ">)");
			}

			Token tokIntOrStr = lexer.next(); // sur int ou str
			Noeud opIntOrStr = Noeud.tokenToNode(tokIntOrStr);

			Arbre a2 = new Arbre(opIntOrStr, null);

			if (lexer.next().getClasse() != Classe.TOK_POINT_VIRGULE) {
				throw new Exception("INT ou STR non suivi d'un POINT_VIRGULE");
			}

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();
			enfants.add(a1);
			enfants.add(a2);

			Symbole symbole = this.tableSymbole.definir(a1.getNoeud().getStrValue());
			symbole.setPosition(this.nvar);
			Symbole.setTypeSymbole(a2.getNoeud(), symbole);

			this.nvar++;

			return new Arbre(op, enfants);
		}

		// A;
		if (lexer.look().getClasse() == Classe.TOK_IDENT) {

			Arbre a1 = affectation();

			if (lexer.next().getClasse() != Classe.TOK_POINT_VIRGULE) {
				throw new Exception("Affectation non suivie d'un POINT_VIRGULE");
			}

			return a1;
		}

		// if (E) I (else I | epsilon)
		if (lexer.look().getClasse() == Classe.TOK_IF) {

			Token tok = lexer.next(); // sur if
			Noeud op = Noeud.tokenToNode(tok);
			op.setIntValue(this.nlabel);
			this.nlabel++;

			if (lexer.look().getClasse() != Classe.TOK_PAR_OUVR) {
				throw new Exception("IF non suivi d'une PAR_OUVR");
			}

			if (lexer.look().getClasse() == Classe.TOK_PAR_OUVR) {
				lexer.next(); // lexer sur la parenthèse ouvrante
				Arbre exp = expression();

				if (exp == null) {
					throw new Exception("Pas d'expression dans les parenthèses");
				}

				ArrayList<Arbre> enfants = new ArrayList<Arbre>();
				enfants.add(exp);

				if (lexer.look().getClasse() != Classe.TOK_PAR_FERM) {
					throw new Exception("Expression non suivie d'une PAR_FERM");
				}

				lexer.next(); // Lexer sur l'instruction

				Arbre ins = instruction();

				if (ins == null) {
					throw new Exception("Instruction manquante");
				}

				enfants.add(ins);

				if (lexer.look().getClasse() == Classe.TOK_ELSE) {
					lexer.next(); // Lexer sur else
					Arbre insElse = instruction();

					if (insElse == null) {
						throw new Exception("Instruction du else manquante");
					}

					enfants.add(insElse);
				}

				return new Arbre(op, enfants);
			}

		}

		// {block}
		// TODO : à mettre dans une fonction
		if (lexer.look().getClasse() == Classe.TOK_ACC_OUVR) {
			lexer.next(); // on avance
			Arbre ins = instruction();

			if (ins == null) {
				throw new Exception("Instruction manquante");
			}

			Token block = new Token();
			block.setClasse(Classe.TOK_BLOCK);
			Noeud nodeBlock = new Noeud(Categorie.BLOCK);

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();

			while (ins != null) {
				enfants.add(ins);
				ins = instruction();
			}

			if (lexer.next().getClasse() != Classe.TOK_ACC_FERM) {
				throw new Exception("Accolade fermante manquante");
			}

			return new Arbre(nodeBlock, enfants);
		}

		// for (A;E;A)
		if (lexer.look().getClasse() == Classe.TOK_FOR) {

			lexer.next(); // lexer sur le FOR

			if (lexer.next().getClasse() != Classe.TOK_PAR_OUVR) {
				throw new Exception("FOR non suivi d'une PAR_OUVR");
			}

			Arbre exp0 = affectation();

			if (exp0 == null) {
				throw new Exception("Pas de première expression dans les parenthèses FOR");
			}

			if (lexer.next().getClasse() != Classe.TOK_POINT_VIRGULE) {
				throw new Exception("E1 non suivi d'un POINT_VIRGULE");
			}

			Arbre exp1 = expression();

			if (exp1 == null) {
				throw new Exception("Pas de deuxième expression dans les parenthèses FOR");
			}

			if (lexer.next().getClasse() != Classe.TOK_POINT_VIRGULE) {
				throw new Exception("E1 non suivi d'un POINT_VIRGULE");
			}

			Arbre exp2 = affectation();

			if (exp2 == null) {
				throw new Exception("Pas de troisième expression dans les parenthèses FOR");
			}

			if (lexer.next().getClasse() != Classe.TOK_PAR_FERM) {
				throw new Exception("Pas de PAR_FERM dans le FOR");
			}

			// ------------------ Profondeur = 4 -----------------//
			ArrayList<Arbre> enfantsDepthFour = new ArrayList<Arbre>();
			Noeud nodeSeq2 = new Noeud(Categorie.SEQ);

			// Statement (block)
			Arbre ins = instruction();
			if (ins == null) {
				throw new Exception("Statement manquant FOR");
			}

			enfantsDepthFour.add(ins);
			enfantsDepthFour.add(exp2);

			Arbre arbreDepthFour = new Arbre(nodeSeq2, enfantsDepthFour);

			// ------------------ Profondeur = 3 -----------------//
			ArrayList<Arbre> enfantsDepthThree = new ArrayList<Arbre>();
			Noeud nodeIf = new Noeud(Categorie.IF);
			Noeud nodeBreak = new Noeud(Categorie.BREAK);

			enfantsDepthThree.add(exp1);
			enfantsDepthThree.add(arbreDepthFour);
			enfantsDepthThree.add(new Arbre(nodeBreak, null));

			Arbre arbreDepthThree = new Arbre(nodeIf, enfantsDepthThree);

			// ------------------ Profondeur = 2 -----------------//
			ArrayList<Arbre> enfantsDepthTwo = new ArrayList<Arbre>();
			Noeud nodeLoop = new Noeud(Categorie.LOOP);

			enfantsDepthTwo.add(arbreDepthThree);

			Arbre arbreDepthTwo = new Arbre(nodeLoop, enfantsDepthTwo);

			// ------------------ Profondeur = 1 -----------------//
			ArrayList<Arbre> enfantsDepthOne = new ArrayList<Arbre>();
			Noeud nodeSeq1 = new Noeud(Categorie.SEQ);

			enfantsDepthOne.add(exp0);
			enfantsDepthOne.add(arbreDepthTwo);

			return new Arbre(nodeSeq1, enfantsDepthOne);

		}

		// while (E) I
		if (lexer.look().getClasse() == Classe.TOK_WHILE) {
			lexer.next(); // lexer sur le WHILE

			if (lexer.next().getClasse() != Classe.TOK_PAR_OUVR) {
				throw new Exception("WHILE non suivi d'une PAR_OUVR");
			}

			Arbre exp = expression();
			if (exp == null) {
				throw new Exception("Pas d'expression dans les parenthèses du WHILE");
			}

			if (lexer.next().getClasse() != Classe.TOK_PAR_FERM) {
				throw new Exception("Pas de PAR_FERM dans le WHILE");
			}

			// ------------------ Profondeur = 2 -----------------//
			ArrayList<Arbre> enfantsDepthTwo = new ArrayList<Arbre>();
			Noeud nodeIf = new Noeud(Categorie.IF);
			Noeud nodeBreak = new Noeud(Categorie.BREAK);

			// Statement (block)
			Arbre ins = instruction();
			if (ins == null) {
				throw new Exception("Statement manquant WHILE");
			}

			enfantsDepthTwo.add(exp);
			enfantsDepthTwo.add(ins);

			enfantsDepthTwo.add(new Arbre(nodeBreak, null));

			Arbre arbreDepthTwo = new Arbre(nodeIf, enfantsDepthTwo);

			// ------------------ Profondeur = 1 -----------------//
			ArrayList<Arbre> enfantsDepthOne = new ArrayList<Arbre>();

			Noeud nodeLoop = new Noeud(Categorie.LOOP);

			enfantsDepthOne.add(arbreDepthTwo);

			return new Arbre(nodeLoop, enfantsDepthOne);
		}

		// return E;
		if (lexer.look().getClasse() == Classe.TOK_RETURN) {
			lexer.next(); // lexer sur le return
			Arbre a1 = expression();

			if (a1 == null) {
				throw new Exception("RETURN non suivi d'un expression");
			}

			if (lexer.next().getClasse() != Classe.TOK_POINT_VIRGULE) {
				throw new Exception("Expression non suivie d'un POINT_VIRGULE");
			}
			returnOk = true;
			return a1;
		}
		
		// echo E;
		if (lexer.look().getClasse() == Classe.TOK_ECHO) {
			lexer.next(); // lexer sur le echo
			Arbre a1 = expression();

			if (a1 == null) {
				throw new Exception("ECHO non suivi d'un expression");
			}

			if (lexer.next().getClasse() != Classe.TOK_POINT_VIRGULE) {
				throw new Exception("Expression non suivie d'un POINT_VIRGULE");
			}
			
			Noeud nodeEcho = new Noeud(Categorie.ECHO);
			nodeEcho.setStrValue(a1.getNoeud().getStrValue());
			nodeEcho.setPosition(a1.getNoeud().getPosition());
			
			Arbre arbre = new Arbre(nodeEcho, null);
			return arbre;
		}
		
		return null;
	}

	/**
	 * Niveau primaire P <- ident | ident "(" E* ")"  | cst_int | (E)
	 * 
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre primaire() throws Exception {
		
		if (lexer.look().getClasse() == Classe.TOK_IDENT) {
			Token tok = lexer.next(); // sur ident
			Noeud op = Noeud.tokenToNode(tok);

			// appel à une fonction
			if (lexer.look().getClasse() == Classe.TOK_PAR_OUVR) {
				lexer.next(); // lexer sur la parenthèse ouvrante

				Noeud call = new Noeud(Categorie.CALL);

				ArrayList<Arbre> enfants = new ArrayList<Arbre>();

				enfants.add(new Arbre(op, null));
				// compteur d'argument de la fonction
				int cpt = 0;
				Arbre e = expression();
				if (e != null) {
					enfants.add(e);
					cpt++;
					while (lexer.look().getClasse() == Classe.TOK_VIRGULE) {
						lexer.next(); // on avance
						cpt++;
						enfants.add(expression());
					}
				}
				
				if (lexer.next().getClasse() != Classe.TOK_PAR_FERM) {
					throw new Exception("Pas de parenthèse fermante");
				}
				call.setIntValue(cpt);
				return new Arbre(call, enfants);
			}

			// variable
			Symbole symbole = this.tableSymbole.chercher(tok.getChargeStr());
			op.setPosition(symbole.getPosition());
			
			return new Arbre(op, null);
		}

		if (lexer.look().getClasse() == Classe.TOK_CST_INT) {
			Token tok = lexer.next(); // sur cst_int
			Noeud op = Noeud.tokenToNode(tok);
			return new Arbre(op, null);
		}

		// Opposé : arbre comme pour la soustraction
		// mais avec un seul enfant
		if (lexer.look().getClasse() == Classe.TOK_SUB) {
			Token tok = lexer.next(); // sur sub
			Noeud op = Noeud.tokenToNode(tok);
			Arbre p = primaire();

			if (p == null) {
				throw new Exception("L'opposé n'a pas de valeur (IDENT manquant)");
			}

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();
			enfants.add(p);

			return new Arbre(op, enfants);
		}

		if (lexer.look().getClasse() != Classe.TOK_PAR_OUVR) {
			return null;
		}

		if (lexer.look().getClasse() == Classe.TOK_PAR_OUVR) {
			lexer.next(); // lexer sur la parenthèse ouvrante
			Arbre res = expression();

			if (res == null) {
				throw new Exception("Pas d'expression dans les parenthèses");
			}

			if (lexer.next().getClasse() != Classe.TOK_PAR_FERM) {
				throw new Exception("Pas de parenthèse fermante");
			}

			return res;
		}

		return null;
	}

	/**
	 * Niveau multiplicatif : M <- P (* M | / M | % M | epsilon)
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre multiplicatif() throws Exception {
		Arbre a1 = primaire();

		if (a1 == null) {
			return null;
		}

		if (lexer.look().getClasse() == Classe.TOK_MUL || lexer.look().getClasse() == Classe.TOK_DIV
				|| lexer.look().getClasse() == Classe.TOK_MODULO) {
			Token tok = lexer.next(); // sur opérateur arithmétique
			Noeud op = Noeud.tokenToNode(tok);
			Arbre a2 = multiplicatif();

			if (a2 == null) {
				throw new Exception("Il manque la deuxième partie de l'expression");
			}

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();
			enfants.add(a1);
			enfants.add(a2);

			return new Arbre(op, enfants);
		}

		return a1;
	}

	/**
	 * Niveau additif : A <- M (+ M | - M | epsilon)
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre additif() throws Exception {
		Arbre a1 = multiplicatif();
		if (a1 == null) {
			return null;
		}

		if (lexer.look().getClasse() == Classe.TOK_ADD || lexer.look().getClasse() == Classe.TOK_SUB) {
			Token tok = lexer.next(); // sur opérateur arithmétique
			Noeud op = Noeud.tokenToNode(tok);
			Arbre a2 = additif();
			if (a2 == null) {
				throw new Exception("Il manque la deuxième partie de l'expression");
			}

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();
			enfants.add(a1);
			enfants.add(a2);

			return new Arbre(op, enfants);
		}

		return a1;
	}

	/**
	 * Niveau comparatif C <- A==A | A!=A
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre comparatif() throws Exception {

		Arbre a1 = additif();
		if (a1 == null) {
			return null;
		}

		if (lexer.look().getClasse() == Classe.TOK_COMPARE || lexer.look().getClasse() == Classe.TOK_DIFF
				|| lexer.look().getClasse() == Classe.TOK_INF || lexer.look().getClasse() == Classe.TOK_SUP
				|| lexer.look().getClasse() == Classe.TOK_INF_EGAL || lexer.look().getClasse() == Classe.TOK_SUP_EGAL) {
			Token tok = lexer.next(); // sur opérateur de comparaison
			Noeud op = Noeud.tokenToNode(tok);
			Arbre a2 = comparatif();
			if (a2 == null) {
				throw new Exception("Il manque la deuxième partie de l'expression");
			}

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();
			enfants.add(a1);
			enfants.add(a2);

			return new Arbre(op, enfants);
		}
		return a1;
	}

	/**
	 * affectation ident = E
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre affectation() throws Exception {

		// Ident = E;
		if (lexer.look().getClasse() == Classe.TOK_IDENT) {

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();

			Token tokIdent = lexer.next(); // sur ident
			Noeud opIdent = Noeud.tokenToNode(tokIdent);

			Arbre en1 = new Arbre(opIdent, null);

			if (lexer.look().getClasse() != Classe.TOK_EGAL) {
				throw new Exception(
						"IDENT non suivie d'un EGAL (<" + lexer.look().getClasse() + ">)");
			}
			Token tok = lexer.next(); // sur =
			Noeud op = Noeud.tokenToNode(tok);

			Arbre en2 = expression();

			if (en2 == null) {
				throw new Exception("EGAL non suivi d'une expression");
			}

			Symbole symbole = this.tableSymbole.chercher(en1.getNoeud().getStrValue());
			en1.getNoeud().setPosition(symbole.getPosition());

			enfants.add(en1);
			enfants.add(en2);

			return new Arbre(op, enfants);
		}

		// TODO : à finir => ajouter +=, -=, etc.

		return null;
	}

	/**
	 * Niveau expression E <- C
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre expression() throws Exception {
		return comparatif();
	}

	/**
	 * Niveau fonction F <- Type IDENT "(" Args ")" I Création d'une fonction
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre fonction() throws Exception {

		if (lexer.look().getClasse() != Classe.TOK_INT && lexer.look().getClasse() != Classe.TOK_STR) {
			throw new Exception(
					"Une fonction doit commencer par le type de retour (<" + lexer.look().getClasse() + ">)");
		}
		
		this.nvar = 0;
		this.narg = 0;
		this.nlabel = 0;

		this.tableSymbole = new TableDeSymbole();
		
		// nouvelle portée local pour les variables => table vide
		this.tableSymbole.push();

		ArrayList<Arbre> enfantsFct = new ArrayList<Arbre>();

		// -------- récupération du type ------- //
		Token tokType = lexer.next(); // sur int ou str
		Noeud type = Noeud.tokenToNode(tokType);

		enfantsFct.add(new Arbre(type, null));

		if (lexer.look().getClasse() != Classe.TOK_IDENT) {
			throw new Exception("Une fonction doit avoir un nom (<" + lexer.look().getClasse() + ">)");
		}

		// -------- récupération de l'ident ------- //
		Token tokIdent = lexer.next(); // sur ident
		Noeud ident = Noeud.tokenToNode(tokIdent);

		enfantsFct.add(new Arbre(ident, null));

		if (lexer.next().getClasse() != Classe.TOK_PAR_OUVR) {
			throw new Exception("Un nom de fonction doit être suivi d'une parenthèse ouvrante (<"
					+ lexer.look().getClasse() + ">)");
		}
		
		// initialisation du noeud ARGS
		Noeud args = new Noeud(Categorie.ARGS);
		ArrayList<Arbre> enfantsArg = new ArrayList<Arbre>();
		Arbre arbreArgs = new Arbre(args, null);

		// si pas de parenthèse fermante on est dans liste des arguments
		if (lexer.look().getClasse() != Classe.TOK_PAR_FERM) {
			
			// ------ récupération des arguments ------//
			Token tokArg = lexer.next(); // sur 1er argument
			
			if (tokArg.getClasse() != Classe.TOK_IDENT) {
				throw new Exception("Le paramètre de la fonction doit être un IDENT (<"
						+ tokArg.getClasse() + ">)");
			}			
			
			Noeud nodeArg = Noeud.tokenToNode(tokArg);

			Symbole symbole = this.tableSymbole.definir(nodeArg.getStrValue());
			symbole.setPosition(this.narg);
			Symbole.setTypeSymbole(nodeArg, symbole);

			this.narg++;

			enfantsArg.add(new Arbre(nodeArg, null));

			while (lexer.look().getClasse() == Classe.TOK_VIRGULE) {
				lexer.next(); // lexer sur la virgule
				tokArg = lexer.next(); // sur 1er argument
				
				if (tokArg.getClasse() != Classe.TOK_IDENT) {
					throw new Exception("Le paramètre de la fonction doit être un IDENT (<"
							+ tokArg.getClasse() + ">)");
				}
				
				nodeArg = Noeud.tokenToNode(tokArg);

				symbole = this.tableSymbole.definir(nodeArg.getStrValue());
				symbole.setPosition(this.narg);
				Symbole.setTypeSymbole(nodeArg, symbole);

				enfantsArg.add(new Arbre(nodeArg, null));
				this.narg++;
			}

		}
		
		enfantsFct.add(arbreArgs);
		args.setIntValue(this.narg);		

		this.nvar = this.narg;

		if (lexer.next().getClasse() != Classe.TOK_PAR_FERM) {
			throw new Exception("Pas de parenthèse fermante (<"
					+ lexer.look().getClasse() + ">)");
		}

		// ------ récupération de l'instruction ------//
		// corps de la fonction (BLOCK)
		Arbre instruction = instruction();
		enfantsFct.add(instruction);

		// ------ création du noeud function ------//
		Noeud function = new Noeud(Categorie.FUNCTION);

		// on met le nombre total de variables dans le programme
		function.setIntValue(this.nvar);
		function.setStrValue(tokIdent.getChargeStr());

		// fin portée local pour les variables
		this.tableSymbole.pop();

		return new Arbre(function, enfantsFct);

	}

	/**
	 * Niveau X <- I Noeud racine
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre racine() throws Exception {

		ArrayList<Arbre> enfant = new ArrayList<Arbre>();

		Arbre main = fonction();

		if (!"main".equalsIgnoreCase(main.getEnfants().get(1).getNoeud().getStrValue())) {
			throw new Exception(
					"La fonction principale doit s'appeler 'main' (<" + main.getEnfants().get(1).getNoeud().getStrValue() + ">)");
		}
		enfant.add(main);
		
		int i = 0;
		while (lexer.look().getClasse() != Classe.TOK_EOF) {
			i++;
			if (i == 10000) {
				throw new Exception("boucle infinie");
			}
			Arbre fonction = fonction();
			enfant.add(fonction);
			if(!returnOk) {
				throw new Exception("La fonction " +fonction.getNoeud().getStrValue() + " doit finir par un return");
			}
			returnOk = false;
		}

		Noeud racine = new Noeud(Categorie.RACINE);

		return new Arbre(racine, enfant);
	}
}
