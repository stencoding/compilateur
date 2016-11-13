package compilateur;

import java.beans.Expression;
import java.util.ArrayList;

/**
 * 
 * Analyseur syntaxique (FRONTEND)
 * 
 * Priorité au plus haut niveau : primaire->multiplicatif->additif->expression
 * 
 * Niveau primaire : P <- ident | cst_int | (E) Niveau multiplicatif : M <- P (*
 * M | / M | % M | epsilon) Niveau additif : A <- M (+ M | - M | epsilon) Niveau
 * expression : E <- A
 * 
 */
public class Parser {

	// position courante du parser (save)
	private int position;
	private Token[] tmpTabToken = new Token[10];
	private Lexer lexer;

	/**
	 * On récupère l'analyseur lexical
	 * 
	 * @param lexer
	 * @throws Exception
	 */
	public Parser(Lexer lexer) throws Exception {
		this.lexer = lexer;
		this.position = 0;
		int i = 0;
		// Token prem = lexer.next();
		Arbre.affiche(instruction(), 0);
		// Arbre.afficheJson(instruction(), 0);
		/*
		 * while (prem.getClasse() != Classe.TOK_EOF) {
		 * 
		 * System.out.println(i); tmpTabToken[i] = prem; //
		 * System.out.println("tab[0] " + tmpTabToken[i]); // tmpTabToken[i] =
		 * lexer.next(); prem = lexer.next(); // System.out.println("prem " +
		 * prem); i++; } // System.out.println(tmpTabToken[2]);
		 */
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
	 * Instruction I <- var Ident : TYPE; | Ident = E; | if (E) I (else I |
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

			Token op = lexer.next(); // lexer positionné sur VAR

			if (lexer.look().getClasse() != Classe.TOK_IDENT) {
				throw new Exception("VAR non suivie d'un IDENT");
			}

			Arbre a1 = new Arbre(lexer.next(), null); // IDENT

			if (lexer.look().getClasse() != Classe.TOK_DEUX_POINTS) {
				throw new Exception("IDENT non suivi d'un DEUX_POINTS");
			}

			lexer.next(); // lexer positionné sur DEUX_POINTS

			if (lexer.look().getClasse() != Classe.TOK_INT
					|| lexer.look().getClasse() != Classe.TOK_STR) {
				throw new Exception(
						"DEUX_POINTS non suivi d'un INT ou d'un STR");
			}

			Arbre a2 = new Arbre(lexer.next(), null); // INT ou STR

			// on se positionne sur le point-virgule qui n'est pas à ajouer à
			// l'arbre
			// fin de l'instruction
			if (lexer.next().getClasse() != Classe.TOK_POINT_VIRGULE) {
				throw new Exception("INT ou STR non suivi d'un POINT_VIRGULE");
			}

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();
			enfants.add(a1);
			enfants.add(a2);

			return new Arbre(op, enfants);
		}

		// Ident = E;
		if (lexer.look().getClasse() == Classe.TOK_IDENT) {

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();
			Arbre en1 = new Arbre(lexer.next(), null); // lexer positionné sur
														// IDENT

			if (lexer.look().getClasse() != Classe.TOK_EGAL) {
				throw new Exception("IDENT non suivie d'un EGAL");
			}
			Token op = lexer.next(); // lexer positionné sur =

			Arbre en2 = expression();

			if (en2 == null) {
				throw new Exception("EGAL non suivi d'une expression");
			}

			// on se positionne sur le point-virgule qui n'est pas à ajouter à
			// l'arbre
			// fin de l'instruction
			if (lexer.next().getClasse() != Classe.TOK_POINT_VIRGULE) {
				throw new Exception("Expression non suivie d'un POINT_VIRGULE");
			}
			enfants.add(en1);
			enfants.add(en2);

			return new Arbre(op, enfants);
		}

		// if (E) I (else I | epsilon)
		if (lexer.look().getClasse() == Classe.TOK_IF) {

			Token op = lexer.next(); // lexer sur le IF

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

			ArrayList<Arbre> enfants = new ArrayList<Arbre>();

			while (ins != null) {
				enfants.add(ins);
				ins = instruction();
			}

			if (lexer.next().getClasse() != Classe.TOK_ACC_FERM) {
				throw new Exception("Accolade fermante manquante");
			}

			return new Arbre(block, enfants);

		}

		// for (Ident = E;E;E) I
		// TODO : à finir !!!!!!!!!!!!!!!!!!!!!!!!
		if (lexer.look().getClasse() == Classe.TOK_FOR) {

			lexer.next(); // lexer sur le FOR

			if (lexer.next().getClasse() != Classe.TOK_PAR_OUVR) {
				throw new Exception("FOR non suivi d'une PAR_OUVR");
			}
			
//			Arbre exp0 = expression();
			//FIXME : à voir avec le prof
			// problème car pas de TOK_EGAL pour expression ou alors une expression
			// est au-dessus d'une instruction
			// plutôt instruction par contre le problème est qu'une instruction est suivie
			// d'un point virgule ...
			Arbre exp0 = instruction();

			// TODO : peut-être autoriser initialisation vide ?
			// factoriser
			if (exp0 == null) {
				throw new Exception("Pas de première expression dans les parenthèses FOR");
			}
//			System.out.println(exp0.getNoeud());
//			System.out.println(lexer.look().getClasse());
//			if (lexer.next().getClasse() != Classe.TOK_VIRGULE) {
//				throw new Exception("E0 non suivi d'une VIRGULE");
//			}
			
			Arbre exp1 = expression();

			// TODO : peut-être autoriser initialisation vide ?
			// factoriser
			if (exp1 == null) {
				throw new Exception("Pas de deuxième expression dans les parenthèses FOR");
			}

			if (lexer.next().getClasse() != Classe.TOK_VIRGULE) {
				throw new Exception("E1 non suivi d'une VIRGULE");
			}
			
//			Arbre exp2 = expression();
			//FIXME : à voir avec le prof
			// plutôt instruction par contre le problème est qu'une instruction est suivie
			// d'un point virgule ...
			Arbre exp2 = instruction();

			// TODO : peut-être autoriser initialisation vide ?
			// factoriser
			if (exp2 == null) {
				throw new Exception("Pas de troisième expression dans les parenthèses FOR");
			}
			
//			System.out.println(lexer.look().getClasse());
			if (lexer.next().getClasse() != Classe.TOK_PAR_FERM) {
				throw new Exception("Expression non suivie d'une PAR_FERM");
			}
			
			//------------------ Profondeur = 4 -----------------//
			ArrayList<Arbre> enfantsDepthFour = new ArrayList<Arbre>();
			Token tokenSeq2 = new Token();
			tokenSeq2.setClasse(Classe.TOK_SEQ);
						
			// Statement (block)
			Arbre ins = instruction();
			if (ins == null) {
				throw new Exception("Statement manquant FOR");
			}
			
			enfantsDepthFour.add(ins);
			enfantsDepthFour.add(exp2);
			
			Arbre arbreDepthFour = new Arbre(tokenSeq2, enfantsDepthFour);
			
			//------------------ Profondeur = 3 -----------------//
			ArrayList<Arbre> enfantsDepthThree = new ArrayList<Arbre>();
			Token tokenIf = new Token();
			tokenIf.setClasse(Classe.TOK_IF);
			Token tokenBreak = new Token();
			tokenBreak.setClasse(Classe.TOK_BREAK);
			
			enfantsDepthThree.add(exp1);
			enfantsDepthThree.add(arbreDepthFour);
			enfantsDepthThree.add(new Arbre(tokenBreak, null));
			
			Arbre arbreDepthThree = new Arbre(tokenIf, enfantsDepthThree);
			
			//------------------ Profondeur = 2 -----------------//
			ArrayList<Arbre> enfantsDepthTwo = new ArrayList<Arbre>();
			Token tokenLoop = new Token();
			tokenLoop.setClasse(Classe.TOK_LOOP);
			
			enfantsDepthTwo.add(arbreDepthThree);
			
			Arbre arbreDepthTwo = new Arbre(tokenLoop, enfantsDepthTwo);
			
			//------------------ Profondeur = 1 -----------------//
			ArrayList<Arbre> enfantsDepthOne = new ArrayList<Arbre>();
			Token tokenSeq1 = new Token();
			tokenSeq1.setClasse(Classe.TOK_SEQ);
			
			enfantsDepthOne.add(exp0);
			enfantsDepthOne.add(arbreDepthTwo);

			return new Arbre(tokenSeq1, enfantsDepthOne);

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
			return new Arbre(lexer.next(), null);
		}

		if (lexer.look().getClasse() == Classe.TOK_CST_INT) {
			return new Arbre(lexer.next(), null);
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
			Token op = lexer.next();
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
			Token op = lexer.next();
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
			Token op = lexer.next();
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
}
