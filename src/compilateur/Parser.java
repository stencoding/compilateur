package compilateur;

import java.util.ArrayList;

/**
 * 
 * Analyseur syntaxique (FRONTEND)
 * 
 * Priorité au plus haut niveau : primaire->multiplicatif->additif->expression
 * 
 * Niveau primaire :
 * P <- ident | cst_int | (E) | -P
 * Niveau multiplicatif : M <- P (*
 * M | / M | % M | epsilon)
 * Niveau additif : A <- M (+ M | - M | epsilon)
 * Niveau
 * expression : E <- A
 * 
 */
public class Parser {

	private int nvar;
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
	}
	
	public void affichageParser() throws Exception {
//			Arbre.affiche(instruction(), 0);
//			Arbre.affiche(expression(), 0);
			Arbre.affiche(racine(), 0);
	}

	/**
	 * Niveau block I <- I|epsilon
	 * 
	 * 
	 * TODO : à faire => facto
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	/*
	 * public Arbre block() throws Exception {
	 * 
	 * Arbre ins1 = instruction(); if (ins1 == null) { return null; }
	 * 
	 * return ins;
	 * 
	 * }
	 */

	/**
	 * Instruction I <- var Ident : TYPE;| Ident = E; | if (E) I (else I |
	 * epsilon) | while (E) I | {Seq I} | for (Ident = E;E;E) I
	 * 
	 * TODO : à finir while et for
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

			if (lexer.look().getClasse() != Classe.TOK_INT
					&& lexer.look().getClasse() != Classe.TOK_STR) {
				throw new Exception(
						"DEUX_POINTS doit être suivi d'un INT ou d'un STR (<" + lexer.look().getClasse() + ">)");
			}

			Token tokIntOrStr = lexer.next(); // sur int ou str
			Noeud opIntOrStr = Noeud.tokenToNode(tokIntOrStr);
						
			Arbre a2 = new Arbre(opIntOrStr, null);

			// on se positionne sur le point-virgule qui n'est pas à ajouer à
			// l'arbre
			// fin de l'instruction
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

		// Ident = E;
		if (lexer.look().getClasse() == Classe.TOK_IDENT) {

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();
			
			Token tokIdent = lexer.next(); // sur =
			Noeud opIdent = Noeud.tokenToNode(tokIdent);
			
			Arbre en1 = new Arbre(opIdent, null);

			if (lexer.look().getClasse() != Classe.TOK_EGAL) {
				throw new Exception("IDENT non suivie d'un EGAL");
			}
			Token tok = lexer.next(); // sur =
			Noeud op = Noeud.tokenToNode(tok);

			Arbre en2 = expression();

			if (en2 == null) {
				throw new Exception("EGAL non suivi d'une expression");
			}

			// on se positionne sur le point-virgule
			// qui n'est pas à ajouter à l'arbre
			// fin de l'instruction
			if (lexer.next().getClasse() != Classe.TOK_POINT_VIRGULE) {
				throw new Exception("Expression non suivie d'un POINT_VIRGULE");
			}
			enfants.add(en1);
			enfants.add(en2);

			// TODO : à quoi il sert ?????
			Symbole symbole = this.tableSymbole.chercher(en1.getNoeud().getStrValue());

			return new Arbre(op, enfants);
		}

		// if (E) I (else I | epsilon)
		if (lexer.look().getClasse() == Classe.TOK_IF) {

			Token tok = lexer.next(); // sur if
			Noeud op = Noeud.tokenToNode(tok);

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
			Noeud nodeBlock = new Noeud(Categorie.BREAK);
			

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

		// for (Ident = E;E;E) I plutôt Statement (block) ???
		// TODO : à finir !!!!!!!!!!!!!!!!!!!!!!!!
		// for (Affectation;Expression;Affectation)
		if (lexer.look().getClasse() == Classe.TOK_FOR) {

			lexer.next(); // lexer sur le FOR

			if (lexer.next().getClasse() != Classe.TOK_PAR_OUVR) {
				throw new Exception("FOR non suivi d'une PAR_OUVR");
			}

			// Arbre exp0 = expression();
			// FIXME : à voir avec le prof
			// problème car pas de TOK_EGAL pour expression ou alors une
			// expression
			// est en-dessous d'une instruction
			// plutôt instruction par contre le problème est qu'une instruction
			// est suivie
			// d'un point virgule ...
			Arbre exp0 = instruction();

			// TODO : peut-être autoriser initialisation vide ?
			// factoriser
			if (exp0 == null) {
				throw new Exception(
						"Pas de première expression dans les parenthèses FOR");
			}

			Arbre exp1 = expression();

			// TODO : peut-être autoriser initialisation vide ?
			// factoriser
			if (exp1 == null) {
				throw new Exception(
						"Pas de deuxième expression dans les parenthèses FOR");
			}

			if (lexer.next().getClasse() != Classe.TOK_POINT_VIRGULE) {
				throw new Exception("E1 non suivi d'un POINT_VIRGULE");
			}

			// Arbre exp2 = expression();
			// FIXME : à voir avec le prof
			// plutôt instruction par contre le problème est qu'une instruction
			// est suivie
			// d'un point virgule ...
			Arbre exp2 = instruction();

			// TODO : peut-être autoriser initialisation vide ?
			// factoriser
			if (exp2 == null) {
				throw new Exception(
						"Pas de troisième expression dans les parenthèses FOR");
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
				throw new Exception(
						"Pas d'expression dans les parenthèses du WHILE");
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

		return null;
	}

	/**
	 * Niveau primaire P <- ident | cst_int | (E)
	 * 
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre primaire() throws Exception {

		if (lexer.look().getClasse() == Classe.TOK_IDENT) {
			Token tok = lexer.next(); // sur ident
			Noeud op = Noeud.tokenToNode(tok);
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
				throw new Exception(
						"L'opposé n'a pas de valeur (IDENT manquant)");
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

		if (lexer.look().getClasse() == Classe.TOK_MUL
				|| lexer.look().getClasse() == Classe.TOK_DIV
				|| lexer.look().getClasse() == Classe.TOK_MODULO) {
			Token tok = lexer.next(); // sur opérateur arithmétique
			Noeud op = Noeud.tokenToNode(tok);
			Arbre a2 = multiplicatif();

			if (a2 == null) {
				throw new Exception(
						"Il manque la deuxième partie de l'expression");
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

		if (lexer.look().getClasse() == Classe.TOK_ADD
				|| lexer.look().getClasse() == Classe.TOK_SUB) {
			Token tok = lexer.next(); // sur opérateur arithmétique
			Noeud op = Noeud.tokenToNode(tok);
			Arbre a2 = additif();
			if (a2 == null) {
				throw new Exception(
						"Il manque la deuxième partie de l'expression");
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

		if (lexer.look().getClasse() == Classe.TOK_COMPARE
				|| lexer.look().getClasse() == Classe.TOK_DIFF
				|| lexer.look().getClasse() == Classe.TOK_INF
				|| lexer.look().getClasse() == Classe.TOK_SUP
				|| lexer.look().getClasse() == Classe.TOK_INF_EGAL
				|| lexer.look().getClasse() == Classe.TOK_SUP_EGAL) {
			Token tok = lexer.next(); // sur opérateur de comparaison
			Noeud op = Noeud.tokenToNode(tok);
			Arbre a2 = comparatif();
			if (a2 == null) {
				throw new Exception(
						"Il manque la deuxième partie de l'expression");
			}

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();
			enfants.add(a1);
			enfants.add(a2);

			return new Arbre(op, enfants);
		}
		return a1;
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
	 * Niveau X <- I
	 * Noeud racine
	 * 
	 * @return Arbre
	 * @throws Exception
	 */
	public Arbre racine() throws Exception {
		if (lexer.next().getClasse() != Classe.TOK_ACC_OUVR) {
			throw new Exception(
					"Il manque l'accolade de début de programme");
		}
		
		this.nvar = 0;
		
		this.tableSymbole = new TableDeSymbole();
		// nouvelle portée globale pour les variables => table vide
		this.tableSymbole.push();
		
		ArrayList<Arbre> enfant = new ArrayList<Arbre>();
		// TODO : à modifier quand grammaire OK
		// FIXME : le problème ici est que l'on boucle sur Classe.TOK_ACC_FERM,
		// la dernière accolade du programme. Ce qui est normale puisque dans
		// instruction on n'utilise pas expression et donc il ne connait pas ce token
		// et donc n'avance pas quand il est dessus
		// ou alors il faudrait avancer le token même quand il ne le connait pas
		// A VOIR : peut-être corrigé en changeant la grammaire
		int i = 0;
		while(lexer.look().getClasse() != Classe.TOK_EOF) {
			i++;
			if (i==10000) {
				throw new Exception("boucle infinie");
			}
			
			enfant.add(instruction());
			if (lexer.look().getClasse() == Classe.TOK_ACC_FERM) {
				break;
			}
//			enfant.add(expression());
		}
		
		Noeud racine = new Noeud(Categorie.RACINE);
		// on met le nombre total de variables dans le programme
		racine.setIntValue(this.nvar);
		
		if (lexer.next().getClasse() != Classe.TOK_ACC_FERM) {
			throw new Exception(
					"Il manque l'accolade de fin de programme");
		}
		
		// fin portée globale pour les variables
		this.tableSymbole.pop();
			
		return new Arbre(racine, enfant);
	}
}
